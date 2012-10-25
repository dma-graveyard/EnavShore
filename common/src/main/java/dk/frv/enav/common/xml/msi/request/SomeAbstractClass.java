package dk.frv.enav.common.xml.msi.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "someAbstractClass", propOrder = {})
public abstract class SomeAbstractClass {

	private int num = 0;
	
	public SomeAbstractClass() {
		
	}
	
	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
	
}
