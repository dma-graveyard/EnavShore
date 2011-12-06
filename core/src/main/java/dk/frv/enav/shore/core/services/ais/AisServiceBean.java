package dk.frv.enav.shore.core.services.ais;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dk.frv.enav.shore.core.domain.AisVesselPosition;
import dk.frv.enav.shore.core.domain.AisVesselTarget;

@Stateless
public class AisServiceBean implements AisService {
	
	@PersistenceContext(unitName = "enav")
    private EntityManager entityManager;
	
	@Override
	public int getVesselCount() {
		Query query = entityManager.createQuery("SELECT COUNT(*) FROM AisVesselTarget t WHERE t.validTo >= :now");
		query.setParameter("now", new Date());
		Long count = (Long) query.getSingleResult();
		return count.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<AisVesselPosition> getVesselsWithinLatLon(AisRequest aisRequest){
		Query query = entityManager.createQuery("SELECT wp " +
				"FROM AisVesselPosition wp " +
				"WHERE wp.lat > :swLat " +
				"AND wp.lon > :swLon " +
				"AND wp.lat < :neLat " +
				"AND wp.lon < :neLon " +
				"AND wp.validTo >= :now");
		query.setParameter("swLat", aisRequest.getSouthWestLat());
		query.setParameter("swLon", aisRequest.getSouthWestLon());
		query.setParameter("neLat", aisRequest.getNorthEastLat());
		query.setParameter("neLon", aisRequest.getNorthEastLon());
		query.setParameter("now", new Date());
		return query.getResultList();
	}
	
	public OverviewResponse getAisTargets(AisRequest aisRequest) {		
		Query query = entityManager.createQuery("" +
				"SELECT vt.id, vt.vesselClass, vt.aisVesselPosition.cog, vt.aisVesselPosition.sog, vt.aisVesselPosition.lat, vt.aisVesselPosition.lon, vt.aisVesselStatic.shipType " +
				"FROM AisVesselTarget vt " +
				"WHERE vt.aisVesselPosition.lat > :swLat " +
					"AND vt.aisVesselPosition.lon > :swLon " +
					"AND vt.aisVesselPosition.lat < :neLat " +
					"AND vt.aisVesselPosition.lon < :neLon " +
					"AND vt.validTo >= :now " +
					"AND vt.aisVesselPosition.lat IS NOT NULL AND vt.aisVesselPosition.lon IS NOT NULL");

		query.setParameter("swLat", aisRequest.getSouthWestLat());
		query.setParameter("swLon", aisRequest.getSouthWestLon());
		query.setParameter("neLat", aisRequest.getNorthEastLat());
		query.setParameter("neLon", aisRequest.getNorthEastLon());
		query.setParameter("now", new Date());
		
		
		@SuppressWarnings("unchecked")
		List<Object[]> lines = query.getResultList();
		
		OverviewResponse response = new OverviewResponse();
		
		for (Object[] values : lines) {
			// TODO nav status
			response.addShip((Integer)values[0], (String)values[1], (Double)values[2], (Double)values[3], (Double)values[4], (Double)values[5], (Byte)values[6], 0);
		}
		
		//return vesselTargets;
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AisVesselTarget getById(int id) {
		Query query = entityManager.createNamedQuery("AisVesselTarget:getById");
		query.setParameter("id", id);
		List<AisVesselTarget> list = query.getResultList();
		return (list.size() == 1 ? list.get(0) : null);
	}
	
	@Override
	public DetailedAisTarget getTargetDetails(int id) {
		DetailedAisTarget aisTarget = new DetailedAisTarget();
		AisVesselTarget target = getById(id);
		if (target != null) {
			aisTarget.init(target);
		}
		return aisTarget;
	}
	
}
