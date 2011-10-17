package dk.frv.enav.shore.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "organisationregister")
public class Organisationregister implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String organisation;
	
	public Organisationregister() {
		
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="organisation", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	
}
