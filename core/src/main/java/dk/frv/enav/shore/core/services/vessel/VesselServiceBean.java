package dk.frv.enav.shore.core.services.vessel;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dk.frv.enav.shore.core.domain.Vessel;

@Stateless
public class VesselServiceBean implements VesselService {

	@PersistenceContext(unitName = "enav")
    private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public Vessel getVessel(int mmsi) {
		Query query = entityManager.createNamedQuery("Vessel:getByMMSI");
		query.setParameter("mmsi", mmsi);
		List<Vessel> list = query.getResultList();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public Vessel getOrCreate(int mmsi) {
		Vessel vessel = getVessel(mmsi);
		if (vessel == null) {
			vessel = new Vessel();
			vessel.setMmsi(mmsi);
			entityManager.persist(vessel);
		}
		return vessel;
	}

}
