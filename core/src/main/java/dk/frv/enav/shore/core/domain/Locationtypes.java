package dk.frv.enav.shore.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "locationtypes")
public class Locationtypes implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private Integer no;
	private String type;
	
	public Locationtypes() {
		
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

	@Column(name="no", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	@Column(name="type", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
