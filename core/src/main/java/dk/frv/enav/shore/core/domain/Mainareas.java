package dk.frv.enav.shore.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mainareas")
public class Mainareas implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String areaEnglish;
	private String areaDanish;
	
	public Mainareas() {
		
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

	@Column(name="area_english", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getAreaEnglish() {
		return areaEnglish;
	}

	public void setAreaEnglish(String areaEnglish) {
		this.areaEnglish = areaEnglish;
	}
	
	@Column(name="area_danish", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getAreaDanish() {
		return areaDanish;
	}
	
	public void setAreaDanish(String areaDanish) {
		this.areaDanish = areaDanish;
	}
	

}
