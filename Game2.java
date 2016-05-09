
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.JFrame;

public class Game2 extends JFrame implements GLEventListener {
	final static String name = "[name]";

	final int width = 512;
	final int height = 512;
	float l = -.1f;
	float r = .1f;
	float b = -0.1f;
	float t = 0.1f;
	float d = 0.1f;
	Buffer scene;

	public Game2() {
		super(name + "'s Raytracer");
		this.scene = renderScene();
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities caps = new GLCapabilities(profile);
		GLCanvas canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);
		this.setName(name + "'s Raytracer");
		this.getContentPane().add(canvas);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		canvas.requestFocusInWindow();
	}

	float v;
	float u;
	int y;
	int x;
	public Buffer renderScene() {
		float[] data = new float[width * height * 3];
		Vector e = new Vector(0, 0, 0);
		for ( y = 0; y < height; y++) {
			for ( x = 0; x < width; x++) {
				v = l + (r - l) * (y + .5f) / 512f;
				u = b + (t - b) * (x + .5f) / 512f;
//if(x==240 && y ==80){
//System.out.println("Hello");	
//}
				Vector D = new Vector(u, v, -d);
				
				D = D.normalize();
				Ray r = new Ray(e, D);
				int i = (y * height) + x;
				i *= 3;
				int depth = 0;

				data[i + 0] = gamma(Trace(r, depth, false).getX()); // red
				data[i + 1] = gamma(Trace(r, depth, false).getY()); // green
				data[i + 2] = gamma(Trace(r, depth, false).getZ()); // blue
			
			}

		}
		return FloatBuffer.wrap(data);
	}

	public float gamma(float i) {
		return (float) Math.pow(i, 1 / 2.2f);
	}

	Vector centerRed = new Vector(-4f, 0, -7f);
	Vector centerGreen = new Vector(0, 0, -7f);
	Vector centerBlue = new Vector(4f, 0, -7f);
	Vector kdGreen = new Vector(0, .5f, 0);
	Vector kdRed = new Vector(1f, 0, 0);
	Vector kdBlue = new Vector(0, 0, 1f);
	Vector kdPlane = new Vector(1f, 1f, 1f);
	Vector KsGreen = new Vector(.5f, .5f, .5f);
	Vector KsPlane = new Vector(0, 0, 0);
	Vector KsRed = new Vector(0, 0, 0);
	Vector KsBlue = new Vector(0, 0, 0);
	Vector KaRed = new Vector(.2f, 0f, 0f);
	Vector KaGreen = new Vector(0f, .2f, 0f);
	Vector KaBlue = new Vector(0f, 0f, .2f);
	Vector KaPlane = new Vector(.2f, .2f, .2f);
	Sphere green = new Sphere(2f, centerGreen, 0);
	Sphere red = new Sphere(1f, centerRed, 0);
	Sphere blue = new Sphere(1f, centerBlue, .8f);
	Plane plane = new Plane(new Vector(0, -2f, 0), new Vector(0, 1f, 0), .5f);
	Vector lightSource = new Vector(-4, 4, -3);

	public Vector Trace(Ray r, int depth, boolean bluehit) {
		Vector totalColor = new Vector(0f, 0f, 0f);
		Vector v2 = r.Point.subtract(centerGreen);
		Vector v1 = r.Point.subtract(centerRed);
		Vector v3 = r.Point.subtract(centerBlue);

		shape current = null;
		float planeT = plane.t(r.Point, r.Direction);
		Vector Vv = null;
		Sphere array[] = new Sphere[3];

		Sphere whichOne = null;

		array[1] = red;
		array[2] = green;
		array[0] = blue;

		for (int i = 0; i <= 2; i++) {
			Vector v = r.Point.subtract(array[i].Center);
			if (array[i].hit(v, r.Direction)) {
				whichOne = array[i];
			}
		}

		if (whichOne == blue && !bluehit ) {
			current = blue;
			Float t = blue.t(v3, r.Direction);
			Vv = r.Direction.multiply(t).add(r.Point);
			blue.setpoint(Vv);
			Vector Normal = blue.normal(Vv);
			//blue.setpoint(Vv);
			Vector blueshading = shade(t, Normal, r.Direction, kdBlue, KsBlue, KaBlue, 0, r.Point);
			Float shadowBlue = shadow(Vv, green);
			if (shadowBlue > 0) {
				totalColor = totalColor.add(KaBlue);
			} else {
				totalColor = totalColor.addincrement(blueshading);
			}
		} else if (whichOne == green && x<420) {
			current = green;
			Float t = green.t(v2, r.Direction);
			Vv = r.Direction.multiply(t).add(r.Point);
			green.setpoint(Vv);
			Vector Normal = green.normal(Vv);
			Vector greenshading = shade(t, Normal, r.Direction, kdGreen, KsGreen, KaGreen, 32, r.Point);
			totalColor = totalColor.add(greenshading);
		} else if (whichOne == red) {
			current = red;
			float t = red.t(v1, r.Direction);
			Vv = r.Direction.multiply(t).add(r.Point);
			red.setpoint(Vv);
			Vector Normal = red.normal(Vv);
			// Vv = Vv.add(Normal).multiply(.001f);
			Vector redshading = shade(t, Normal, r.Direction, kdRed, KsRed, KaRed, 0, r.Point);
			totalColor = totalColor.add(redshading);
		} else if (planeT > 0.0001f) {
			current = plane;
			Vector planeshading = shade(planeT, plane.normal, r.Direction, kdPlane, KsPlane, KaPlane, 0, r.Point);
			Vector PointonPlane = r.Direction.multiply(planeT).add(r.Point);
			plane.setpoint(PointonPlane);
			float greenShadow = shadow(PointonPlane, green);
			float blueShadow = shadow(PointonPlane, blue);
			float redShadow = shadow(PointonPlane, red);
			if (greenShadow > 0) {
				totalColor = totalColor.add(KaPlane);
			} else if (blueShadow > 0) {
				totalColor = totalColor.add(KaPlane);
			} else if (redShadow > 0) {
				totalColor = totalColor.add(KaPlane);
			} else {
				totalColor = totalColor.add(planeshading);
			}
		} else {
			current = null;
			totalColor = new Vector(0f, 0f, 0f);
		}

		// Vector reflectioncolor = new Vector(0f, 0f, 0f);
		if (current != null) {
			if ((depth < 2) && current.getalpha() > 0) {
				// r.Direction = r.Direction.normalize();
				Vector reflectiondir = reflectionraydirection(current.getnormal().multiply(1f),r.Direction.multiply(1f));
				//reflectiondir = reflectiondir.normalize();
				Ray rayy = new Ray(current.getpoint(), reflectiondir);
				Vector reflectioncolor = null;
//				if(x==240 && y ==80){
//					System.out.println(r.Direction.getX());
//					System.out.println(r.Direction.getY());
//					System.out.println(r.Direction.getZ());
//				}
				if (current == blue) {
					reflectioncolor = Trace(rayy, depth + 1, true);
				} else {
					reflectioncolor = Trace(rayy, depth + 1, false);
				}
				totalColor = totalColor.multiply(1 - current.getalpha()).add(reflectioncolor.multiply(current.getalpha()));
			}
		}
		return totalColor;
	}

	public float shadow(Vector ray, Sphere circle) {
		Vector center = circle.Center;
		float radius = circle.radius;
		Vector lightSource = new Vector(-4, 4, -3);
		Vector ShadowRay = lightSource.subtract(ray);
		Vector NewpointatLightsource = lightSource.subtract(center);
		ShadowRay = ShadowRay.normalize();
		return circle.intersection(NewpointatLightsource, ShadowRay, radius);
	}

	public Vector shade(float t, Vector Normal, Vector eyeRay, Vector kd, Vector Ks, Vector Ka, float spectular,
			Vector Point) {
		Vector lightSource = new Vector(-4, 4, -3);
		Vector Vv = eyeRay.multiply(t).add(Point);
		Vector light1 = lightSource.subtract(Vv);
		light1 = light1.normalize();
		float nxl = (float) Normal.dotProduct(light1);
		nxl = Math.max(nxl, 0);
		Vv = Vv.normalize().multiply(-1f);
		Vector H = light1.add(Vv);
		H = H.normalize();
		Vector Ld;
		Ld = kd.multiply(nxl);
		Float Nxh = (float) Normal.dotProduct(H);
		Nxh = Math.max(Nxh, 0);
		Nxh = (float) Math.pow(Nxh, spectular);
		Vector Ls = Ks.multiply(Nxh);
		return Ld.add(Ls).add(Ka);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glDrawPixels(width, height, GL2.GL_RGB, GL2.GL_FLOAT, this.scene);
		gl.glFlush();
	}

	public Vector reflectionraydirection(Vector n, Vector v) {
		v = v.normalize();
		n = n.normalize();
		v = v.multiply(1f);
		double h = n.dotProduct(v);
		Vector N = n.multiply((float) h * 2);
		N = v.subtract(N);
		return N;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
	}

	@Override
	public void init(GLAutoDrawable arg0) {
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
	}

	public static void main(String[] args) {
		Game2 game = new Game2();
	}
}