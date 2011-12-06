package dk.frv.enav.shore.core.services.ais;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

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

	@Override
	public OverviewResponse getOverview(OverviewRequest overviewRequest) {
		String sql = "SELECT vt.id, vt.vesselClass, vt.aisVesselPosition.cog, vt.aisVesselPosition.sog, vt.aisVesselPosition.lat, vt.aisVesselPosition.lon, vt.aisVesselStatic.shipType, casp.navStatus "
				+ "FROM AisVesselTarget vt "
				+ "LEFT OUTER JOIN vt.aisVesselPosition.aisClassAPosition AS casp "
				+ "WHERE vt.aisVesselPosition.lat > :swLat "
				+ "AND vt.aisVesselPosition.lon > :swLon "
				+ "AND vt.aisVesselPosition.lat < :neLat "
				+ "AND vt.aisVesselPosition.lon < :neLon "
				+ "AND vt.validTo >= :now "
				+ "AND vt.aisVesselPosition.lat IS NOT NULL AND vt.aisVesselPosition.lon IS NOT NULL ";
		
		if (overviewRequest.getCountries().size() > 0) {
			String[] ors = new String[overviewRequest.getCountries().size()];
			for (int i=0; i < ors.length; i++) {
				ors[i] = "vt.country = :country" + i;
			}
			sql += "\nAND (";
			sql += StringUtils.join(ors, " OR ");
			sql += ")";
		}
		System.out.println("sql: " + sql);

		Query query = entityManager.createQuery(sql);
		query.setParameter("swLat", overviewRequest.getSwLat());
		query.setParameter("swLon", overviewRequest.getSwLon());
		query.setParameter("neLat", overviewRequest.getNeLat());
		query.setParameter("neLon", overviewRequest.getNeLon());
		query.setParameter("now", new Date());
		if (overviewRequest.getCountries().size() > 0) {
			for (int i=0; i < overviewRequest.getCountries().size(); i++) {
				System.out.println("country: " + overviewRequest.getCountries().get(i));
				query.setParameter("country" + i, overviewRequest.getCountries().get(i));
			}
		}

		@SuppressWarnings("unchecked")
		List<Object[]> lines = query.getResultList();

		OverviewResponse response = new OverviewResponse();

		for (Object[] values : lines) {
			response.addShip((Integer) values[0], (String) values[1], (Double) values[2], (Double) values[3], (Double) values[4],
					(Double) values[5], (Byte) values[6], (Byte)values[7]);
		}

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
