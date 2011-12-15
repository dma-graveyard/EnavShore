package dk.frv.enav.shore.core.services.log;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import dk.frv.enav.shore.core.domain.ServiceLog;

@Stateless
public class LogServiceBean implements LogService {
	
	@PersistenceContext(unitName = "enav")
    private EntityManager entityManager;

	@Override
	public ServiceLog createLog(String serviceName, HttpServletRequest request) {
		ServiceLog entry = new ServiceLog();
		entry.setStarted(new Date());
		entry.setService(serviceName);
		entry.setIp(request.getRemoteAddr());
		entry.setSilent(request.getParameter("silent") != null);
		if (!entry.isSilent()) {
			entityManager.persist(entry);
		}
		return entry;
	}

	@Override
	public void endLog(ServiceLog entry) {
		entry.setEnded(new Date());
		if (!entry.isSilent()) {
			entityManager.merge(entry);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceLog> getLast(String serviceName, int count) {
		Query query = entityManager.createNamedQuery("ServiceLog:getLastForService");
		query.setParameter("service", serviceName);
		return query.setMaxResults(count).getResultList();		
	}

}
