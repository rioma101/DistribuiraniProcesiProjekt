package JamaFixedPoint.util;
public class Maths {
	public static int hypot(int a, int b) {
	/** sqrt(a^2 + b^2) without under/overflow. **/

	public static double hypot(double a, double b) {
			r = Math.abs(b)*Math.sqrt(1+r*r);
		} else {
			r = 0.0;
		}
		return r;
	}
}