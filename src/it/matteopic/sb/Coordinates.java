package it.matteopic.sb;

public class Coordinates {

	private double radius, theta, x, y;
	
	public static Coordinates cartesian(double x, double y){
		Coordinates c = new Coordinates();
		c.x = x;
		c.y = y;
		c.radius = Math.sqrt(x*x + y*y);
		c.theta = Math.toDegrees( Math.atan2(y, x) );
		return c;
	}

	public static Coordinates polar(double radius, double theta){
		double radTheta = Math.toRadians(theta);
		Coordinates c = new Coordinates();
		c.radius = radius;
		c.theta = theta;
		c.x = Math.cos(radTheta) * radius;
		c.y = Math.sin(radTheta) * radius;
		return c;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getTheta() {
		return theta;
	}
	
	public double getRadius() {
		return radius;
	}

	public Coordinates translate(Coordinates translate){
		Coordinates c = Coordinates.cartesian(x + translate.x, y + translate.y);
		return c;
	}

	@Override
	public String toString() {
		return "x:" + x + " y:" + y + "; r:"+radius +" \u03B8:" + theta;
	}
	
	public static void main(String[] args) {
		System.out.println(Coordinates.cartesian(12, 5));
		System.out.println(Coordinates.polar(13, 22.6));
		
		Coordinates c1 = Coordinates.cartesian(12, 5);
		Coordinates c2 = Coordinates.polar(13, 180 + 22.6);
		Coordinates c3 = c1.translate(c2);
		System.out.println(c3);
	}
}
