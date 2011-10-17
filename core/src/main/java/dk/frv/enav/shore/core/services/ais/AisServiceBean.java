package dk.frv.enav.shore.core.services.ais;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dk.frv.enav.shore.core.domain.AisClassAPosition;
import dk.frv.enav.shore.core.domain.AisVesselPosition;
import dk.frv.enav.shore.core.domain.AisVesselStatic;
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
	
	public List<PublicAisTarget> getPublicAisTargets(AisRequest aisRequest) {		
		Query query = entityManager.createQuery("" +
				"SELECT vt, vt.aisVesselPosition, vt.aisVesselStatic, casp " +
				"FROM AisVesselTarget vt " +
				"LEFT OUTER JOIN vt.aisVesselPosition.aisClassAPosition as casp " +
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
		
		List<PublicAisTarget> publicShips = new ArrayList<PublicAisTarget>(lines.size());
		
		for (Object[] values : lines) {
			AisVesselTarget aisVessel = (AisVesselTarget) values[0];
			AisVesselPosition aisVesselPosition = (AisVesselPosition) values[1];
			AisVesselStatic aisVesselStatic = (AisVesselStatic) values[2];
			AisClassAPosition aisClassAPosition = (AisClassAPosition) values[3];
			
			Double heading = null;
			if((heading = aisVesselPosition.getCog()) == null) {
				if((heading = aisVesselPosition.getHeading()) == null)
					heading = 0d;
			}
			
			byte navStatus = -1;
			if(aisClassAPosition != null)
				navStatus = aisClassAPosition.getNavStatus();
			
			short length = (short) (aisVesselStatic.getDimBow() + aisVesselStatic.getDimStern());
			byte width = (byte) (aisVesselStatic.getDimPort() + aisVesselStatic.getDimStarboard());
			Byte vesselType; 
			if((vesselType = aisVesselStatic.getShipType()) == null)
				vesselType = 0;
			
			Double sog;
			if((sog = aisVesselPosition.getSog()) == null)
				sog = 0d;
			
			PublicAisTarget newPublicTarget = new PublicAisTarget(
					aisVessel.getLastReceived().getTime(),
					aisVesselPosition.getLat(), 
					aisVesselPosition.getLon(), 
					heading,
					navStatus,
					AisVesselStatic.getVesselType(vesselType),
					length,
					width,
					sog);
			publicShips.add(newPublicTarget);	
		}
		
		return publicShips;
	}
}
