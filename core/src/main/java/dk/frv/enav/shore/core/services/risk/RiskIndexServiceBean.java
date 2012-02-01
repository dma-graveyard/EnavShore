package dk.frv.enav.shore.core.services.risk;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dk.dma.aisservices.core.domain.AisVesselTarget;
import dk.dma.aisservices.core.services.ais.AisService;
import dk.frv.enav.common.xml.risk.request.RiskRequest;
import dk.frv.enav.common.xml.risk.response.RiskList;
import dk.frv.enav.common.xml.risk.response.RiskResponse;
import dk.frv.enav.shore.core.domain.Risk;
import dk.frv.enav.shore.core.services.RemoteServiceFactory;
import dk.frv.enav.shore.core.services.ServiceException;

@Stateless
public class RiskIndexServiceBean implements RiskIndexService {

	@PersistenceContext(unitName = "enav")
	private EntityManager entityManager;

	@Override
	public RiskResponse getRiskIndexes(RiskRequest req) throws ServiceException {
		AisService aisService = RemoteServiceFactory.getAisService();		

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
		Query query = entityManager
				.createNativeQuery(
						"SELECT id, mmsi,  risk_index_normalized,   consequence,   probability,   accident_type,   time_created FROM"
								+ "    risks r1 WHERE mmsi = :mmsi and time_created > (select max(time_created) from risks WHERE  mmsi = r1.mmsi) - INTERVAL 10 SECOND "
								+ "and time_created > now() - INTERVAL 10 MINUTE ", Risk.class);
		query.setParameter("mmsi", mmsi);

		return query.getResultList();
	}
}
