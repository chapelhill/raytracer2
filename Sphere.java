
public class Sphere extends shape {
	float radius;
	Vector Center;
	float intersect;
	float alpha;
	Vector Normal;
	Vector Point;
	float error;

	Sphere(float radius, Vector Center, float alpha) {
		this.radius = radius;
		this.Center = Center;
		this.alpha = alpha;

	}

	public float intersection(Vector v, Vector d, float radius) {
		d=d.normalize();
		return (float) (((v.dotProduct(d) * v.dotProduct(d))) - (float)((v.dotProduct(v) - radius * radius)));
	}

	boolean hit(Vector v, Vector eyeRay) {
		Float intersect = intersection(v, eyeRay, this.radius);
		if (intersect >=0.00001f) {
			this.intersect = intersect;
			return true;
		} else {
			return false;
		}
	}
	

	public float addintersection(Vector v, Vector d, float intersect) {
		return (float) (v.dotProduct(d.multiply(-1f))) + (float)  Math.sqrt(intersect);
	}

	public Vector normal(Vector Vv) {
		Vector n = Vv.subtract(this.Center);
		n = n.normalize();
		this.Normal = n;
		return n;

	}

	public float subtractintersection(Vector v, Vector d, float intersect) {
		return (float) -(v.dotProduct(d)) - (float)  Math.sqrt(intersect);
	}

	public float t(Vector v, Vector eyeRay) {
		float t1 = addintersection(v, eyeRay, this.intersect);
		float t2 = subtractintersection(v, eyeRay, this.intersect);
		float t = Math.min(t1, t2);
		return t;
	}

	public float getalpha() {
		return this.alpha;
	}

	public Vector getnormal() {
		return this.Normal;
	}

	public Vector getpoint() {
		return this.Point;
	}

	public void setpoint(Vector point) {
		this.Point = point;
	}

}