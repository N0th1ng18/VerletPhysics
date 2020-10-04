
public class Vec2 {
	public float x,y;
	
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void reset() {
		this.x = 0;
		this.y = 0;
	}
	
	public boolean normalize() {
		float d = getMagnitude();
		if(d != 0.0f) {
			x = x/d;
			y = y/d;
			return true;
		}
		return false;
	}
	
	public float getMagnitude() {
		return (float)Math.sqrt((x*x)+(y*y));
	}
	
	public float dot(Vec2 vector) {
		return (this.x*vector.x)+(this.y*vector.y);
	}
	
	public float det(Vec2 vector) {
		return (this.x*vector.y)-(this.y*vector.x);
	}
	
	
	public float squareDist(Vec2 v) {
		float dx = this.x - v.x;
		float dy = this.y - v.y;
		return dx * dx + dy * dy;
	}
	
	public Vec2 perpendicular(Vec2 v) {
		this.x = -v.y;
		this.y = v.x;
		return this;
	}
	public Vec2 rotate(float angle) {
		float x = this.x;
		float y = this.y;
		
		this.x = (float) (x*Math.cos(angle) - y*Math.sin(angle));
		this.y = (float) (y*Math.cos(angle) + x*Math.sin(angle));
		
		return this;
	}
	
	public Vec2 add(Vec2 v2) {
		Vec2 a1 = new Vec2(this.x, this.y);
		a1.x = a1.x + v2.x;
		a1.y = a1.y + v2.y;
		return a1;
	}
	public Vec2 sub(Vec2 v2) {
		Vec2 a1 = new Vec2(this.x, this.y);
		a1.x = a1.x - v2.x;
		a1.y = a1.y - v2.y;
		return a1;
	}
	public Vec2 mul(Vec2 v2) {
		Vec2 a1 = new Vec2(this.x, this.y);
		a1.x = a1.x * v2.x;
		a1.y = a1.y * v2.y;
		return a1;
	}
	public Vec2 div(Vec2 v2) {
		Vec2 a1 = new Vec2(this.x, this.y);
		a1.x = a1.x / v2.x;
		a1.y = a1.y / v2.y;
		return a1;
	}
}
