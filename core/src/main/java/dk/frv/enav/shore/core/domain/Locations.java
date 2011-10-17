package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "locations")
public class Locations implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String locationType;
	private Mainareas mainarea;
	private String subarea;
	private List<Points> points;
	
	public Locations() {
		
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

	@Column(name="name", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="locationType", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "areaId", unique = false, nullable = true, insertable = true, updatable = true)
	public Mainareas getMainarea() {
		return mainarea;
	}
	
	public void setMainarea(Mainareas mainarea) {
		this.mainarea = mainarea;
	}

	@Column(name="subarea", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getSubarea() {
		return subarea;
	}

	public void setSubarea(String subarea) {
		this.subarea = subarea;
	}
	
	@OneToMany(mappedBy = "location", cascade = {})
	public List<Points> getPoints() {
		return points;
	}
	
	public void setPoints(List<Points> points) {
		this.points = points;
	}

}
