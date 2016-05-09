

public class Vector {
	private float x;
	private float y;
	private float z;

	Vector() { this(0f, 0f, 0f); }
	Vector(float X, float Y, float Z) {
		x = X;
		y = Y;
		z = Z;
	}

	public double dotProduct(Vector Z) {
		float dot = this.x * Z.x + this.y * Z.y + this.z * Z.z;
		return dot;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getZ() {
		return this.z;
	}

	public Vector multiply(float t) {
		float newX = (float) t * this.x;
		float newY = (float) t * this.y;
		float newZ = (float) t * this.z;
		return new Vector(newX, newY, newZ);
	}
	public Vector addincrement(Vector Z) {
		 this.x += Z.x ;
		 this.y += Z.y ;
		 this.z += Z.z ;
		return this;
	}
	public Vector normalize() {
		float newX = (float) (this.x) / (float)( Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z));
		float newY = (float) (this.y) / (float) (Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z));
		float newZ = (float) (this.z) / (float) (Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z));
		return new Vector(newX, newY, newZ);
	}

	public Vector add(Vector Z) {
		float newX = this.x + Z.x;
		float newY = this.y + Z.y;
		float newZ = this.z + Z.z;
		return new Vector(newX, newY, newZ);
	}

	public Vector subtract(Vector Z) {
		float newX = this.x - Z.x;
		float newY = this.y - Z.y;
		float newZ = this.z - Z.z;
		return new Vector(newX, newY, newZ);
	}
}
