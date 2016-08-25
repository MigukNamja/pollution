package miguknamja.utils;

public class Color {
	public float r, g, b;

	public Color() {
		super();
		r = g = b = 0; // black
	}

	public Color(float r, float g, float b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Color(double r, double g, double b) {
		super();
		this.r = (float)r;
		this.g = (float)g;
		this.b = (float)b;
	}
	
	@Override
	public String toString() {
		return "r(" + r + ").g(" + g + ").b(" + b + ")"; 
	}
}
