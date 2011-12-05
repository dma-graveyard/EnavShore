package dk.frv.enav.shore.core.services.ais;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dk.frv.ais.message.ShipTypeCargo;
import dk.frv.enav.shore.core.domain.AisVesselPosition;
import dk.frv.enav.shore.core.domain.AisVesselTarget;

@Stateless
public class AisServiceBean implements AisService {
	
	private static final long MAX_TARGET_AGE = 6 * 60 * 1000; // 6 min

	@PersistenceContext(unitName = "enav")
    private EntityManager entityManager;
	
	@Override
	public int getVesselCount() {
		Query query = entityManager.createQuery("SELECT COUNT(*) FROM AisVesselTarget t WHERE t.lastReceived > :newDate");
		query.setParameter("newDate", new Date(System.currentTimeMillis()-MAX_TARGET_AGE));
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
				"AND wp.received > :newDate");
		query.setParameter("swLat", aisRequest.getSouthWestLat());
		query.setParameter("swLon", aisRequest.getSouthWestLon());
		query.setParameter("neLat", aisRequest.getNorthEastLat());
		query.setParameter("neLon", aisRequest.getNorthEastLon());
		query.setParameter("newDate", new Date(System.currentTimeMillis()-MAX_TARGET_AGE));
		return query.getResultList();
	}
	
	public List<OverviewAisTarget> getAisTargets(AisRequest aisRequest) {		
		Query query = entityManager.createQuery("" +
				"SELECT vt.mmsi, vt.vesselClass, vt.aisVesselPosition.cog, vt.aisVesselPosition.lat, vt.aisVesselPosition.lon, vt.aisVesselStatic.shipType " +
				"FROM AisVesselTarget vt " +
				"WHERE vt.aisVesselPosition.lat > :swLat " +
					"AND vt.aisVesselPosition.lon > :swLon " +
					"AND vt.aisVesselPosition.lat < :neLat " +
					"AND vt.aisVesselPosition.lon < :neLon " +
					"AND vt.aisVesselPosition.received > :newDate");

		query.setParameter("swLat", aisRequest.getSouthWestLat());
		query.setParameter("swLon", aisRequest.getSouthWestLon());
		query.setParameter("neLat", aisRequest.getNorthEastLat());
		query.setParameter("neLon", aisRequest.getNorthEastLon());
		query.setParameter("newDate", new Date(System.currentTimeMillis()-MAX_TARGET_AGE));
		
		
		@SuppressWarnings("unchecked")
		List<Object[]> lines = query.getResultList();
		
		List<OverviewAisTarget> vesselTargets = new ArrayList<OverviewAisTarget>(lines.size());
		
		for (Object[] values : lines) {
			Integer mmsi = (Integer)values[0];
			String vesselClass = (String)values[1];
			Double cog = (Double)values[2];
			Double lat = (Double)values[3];
			Double lon = (Double)values[4];
			Byte shipType = (Byte)values[5];
			if (cog == null) {
				cog = 0d;
			}			
					
			
			OverviewAisTarget aisTarget = new OverviewAisTarget();
			
			aisTarget.setMmsi(mmsi);			
			aisTarget.setVc(vesselClass);
			aisTarget.setCog((int)Math.round(cog));
			aisTarget.setLat(lat);
			aisTarget.setLon(lon);
			aisTarget.setVc(vesselClass);
			ShipTypeCargo shipTypeCargo = new ShipTypeCargo(shipType);			
			aisTarget.setVt(shipTypeCargo.getShipType().ordinal());
			
			vesselTargets.add(aisTarget);	
		}
		
		return vesselTargets;
	}
	
	@Override
	public DetailedAisTarget getTargetDetails(int mmsi) {
		DetailedAisTarget aisTarget = new DetailedAisTarget();
		AisVesselTarget vesselTarget = entityManager.find(AisVesselTarget.class, mmsi);
		if (vesselTarget != null) {
			aisTarget.init(vesselTarget);
		}
		
		return aisTarget;
	}
	
}
