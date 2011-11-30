package dk.frv.enav.common.xml.nogo.types;

public class BoundingBoxPoint {
private int n;
private int m;
	public BoundingBoxPoint(int n, int m){
		this.n = n;
		this.m = m;
	}
	
	public BoundingBoxPoint(){
		this.n = 0;
		this.m = 0;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}	
	
	 @Override public String toString() {
		 return ("n: " + n + " m: " + m);
	 }
	
}
