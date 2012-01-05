package dk.frv.enav.shore.core.services.ais;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dk.frv.enav.shore.core.domain.AisVesselTarget;

@Stateless
public class AisServiceBean implements AisService {
	
	private static final long MAX_TARGET_AGE = 6 * 60 * 1000; // 6 min

	@PersistenceContext(unitName = "enav")
    private EntityManager entityManager;
	
	
	@SuppressWarnings("unchecked")
	public List<AisVesselTarget> getAisTargets(double swLat,double swLon, double neLat, double neLon) {		
		Query query = entityManager.createQuery("" +
				"SELECT vt FROM AisVesselTarget vt " +
				"WHERE vt.aisVesselPosition.lat > :swLat " +
				"AND vt.aisVesselPosition.lon > :swLon " +
				"AND vt.aisVesselPosition.lat < :neLat " +
				"AND vt.aisVesselPosition.lon < :neLon " +
				"AND vt.aisVesselPosition.received > :newDate");

		query.setParameter("swLat",swLat);
		query.setParameter("swLon", swLon);
		query.setParameter("neLat", neLat);
		query.setParameter("neLon", neLon);
		query.setParameter("newDate", new Date(System.currentTimeMillis()-MAX_TARGET_AGE));
		
		return query.getResultList();
	
	}
	
	
}
