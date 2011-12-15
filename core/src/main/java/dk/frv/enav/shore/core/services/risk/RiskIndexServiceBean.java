package dk.frv.enav.shore.core.services.risk;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dk.frv.enav.common.xml.risk.request.RiskRequest;
import dk.frv.enav.common.xml.risk.response.RiskResponse;
import dk.frv.enav.shore.core.domain.AisVesselTarget;
import dk.frv.enav.shore.core.domain.RiskIndexes;
import dk.frv.enav.shore.core.services.ServiceException;
import dk.frv.enav.shore.core.services.ais.AisService;

@Stateless
public class RiskIndexServiceBean implements RiskIndexService {
	@EJB
	AisService aisService;

	@PersistenceContext(unitName = "enav")
	private EntityManager entityManager;

	@Override
	public RiskResponse getRiskIndexes(RiskRequest req) throws ServiceException {

		
		List<AisVesselTarget> list = aisService.getAisTargets(req.getLatMin() , req.getLonMin(),req.getLatMax(),req.getLonMax());

		RiskResponse resp = new RiskResponse();
		for (AisVesselTarget target : list) {
			RiskIndexes risk = findLastByMmsi(target.getMmsi());
			if (risk != null) {
				resp.add(risk.getCommonRiskObject());
			}
		}
		return resp;
	}

	public RiskIndexes findLastByMmsi(Integer mmsi) {
		Query query = entityManager
				.createQuery("SELECT r FROM RiskIndexes r WHERE r.mmsi = :mmsi order by datetimeCreated");
		query.setParameter("mmsi", mmsi);
		List<RiskIndexes> list = query.getResultList();
		if (!list.isEmpty()) {
			return list.iterator().next();
		}
		return null;

	}
}
