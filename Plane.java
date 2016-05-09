
public class Plane extends shape{
	Vector point;
	Vector normal;
	float t;
	float alpha;
	Vector Point2;
	Plane(Vector point, Vector normal, float alpha) {
		this.point = point;
		this.normal = normal;
		this.alpha=alpha;
	}

	float t(Vector Point,Vector direction) {
        t=(float) (point.subtract(Point).dotProduct(normal))/ (float)(direction.dotProduct(normal));
		return t;
	}

	@Override
	float getalpha() {
		// TODO Auto-generated method stub
		return this.alpha;
	}

	@Override
	Vector getnormal() {
		// TODO Auto-generated method stub
		return this.normal;
	}
	Vector getpoint(){
		return this.Point2;
	}
	void  setpoint(Vector point) {
		// TODO Auto-generated method stub
		this.Point2=point;
	}

}
