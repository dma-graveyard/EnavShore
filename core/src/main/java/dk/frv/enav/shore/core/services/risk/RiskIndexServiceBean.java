package dk.frv.enav.shore.core.services.risk;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dk.frv.enav.common.xml.risk.request.RiskRequest;
import dk.frv.enav.common.xml.risk.response.RiskList;
import dk.frv.enav.common.xml.risk.response.RiskResponse;
import dk.frv.enav.shore.core.domain.AisVesselTarget;
import dk.frv.enav.shore.core.domain.Risk;
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

		List<AisVesselTarget> list = aisService.getAisTargets(req.getLatMin(), req.getLonMin(), req.getLatMax(),
				req.getLonMax());

		RiskResponse resp = new RiskResponse();

		for (AisVesselTarget target : list) {
			
			RiskList risks = new RiskList();
			risks.setMmsi(target.getMmsi());
			
			for (Risk r : findLastByMmsi(target.getMmsi())) {
				risks.addRisk(r.getCommonRiskObject());
			}
			
			resp.add(risks);
		}
		return resp;
	}

	@SuppressWarnings("unchecked")
	public List<Risk> findLastByMmsi(Integer mmsi) {
		Query query = entityManager.createNativeQuery(
				"select id, mmsi, risk_proba, consequence_index,accident_type,time_created  from risks r1 where (mmsi, accident_type,time_created) in "
						+ "( select mmsi, accident_type, max(time_created) from risks r2 "
						+ "where mmsi = :mmsi and time_created > now()- INTERVAL 10 MINUTE group by accident_type)",
				Risk.class);
		query.setParameter("mmsi", mmsi);

		return query.getResultList();

	}
}
