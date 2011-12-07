package dk.frv.enav.common.util;

public class Point {
	public int n;
	public int m;
	public boolean isProcessed = false;

	public Point(int n, int m) {
		this.n = n;
		this.m = m;
	}

	@Override
	public String toString() {
		return ("(" + n + "," + m + "):" + this.isProcessed);
	}
}