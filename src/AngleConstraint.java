import java.awt.Color;
import java.awt.Graphics2D;

public class AngleConstraint {
	public Object parent;
	public int identity = -1;
	
	/*
	 * Constraint Settings
	 */
	public int iterations = 10;
	public float stiffness = 0.1f;

	public Vertex v1;
	public Vertex v2;
	public Vertex v3;
	public Vertex v4;
	
	Vec2 v1v2;
	Vec2 v3v4;

	float targetAngle;
	float currentAngle;
	
	public AngleConstraint(Edge e1, Constraint l1, float angle) {
		this.v1 = e1.v1;
		this.v2 = e1.v2;
		this.v3 = l1.v1;
		this.v4 = l1.v2;
		this.targetAngle = (float) (angle * Math.PI/180.0f);
		
		v1v2 = new Vec2(0.0f, 0.0f);
		v3v4 = new Vec2(0.0f, 0.0f);
	}
	public AngleConstraint(Edge e1, Edge e2, float angle) {
		this.v1 = e1.v1;
		this.v2 = e1.v2;
		this.v3 = e2.v1;
		this.v4 = e2.v2;
		this.targetAngle = (float) (angle * Math.PI/180.0f);
		
		v1v2 = new Vec2(0.0f, 0.0f);
		v3v4 = new Vec2(0.0f, 0.0f);
	}
	public AngleConstraint(Constraint l1, Constraint l2, float angle) {
		this.v1 = l1.v1;
		this.v2 = l1.v2;
		this.v3 = l2.v1;
		this.v4 = l2.v2;
		this.targetAngle = (float) (angle * Math.PI/180.0f);
		
		v1v2 = new Vec2(0.0f, 0.0f);
		v3v4 = new Vec2(0.0f, 0.0f);
	}
	
	public void update() {
		/*
		 * Update Constraints
		 */
		for(int i=0; i < iterations; i++) {
			v1v2.x = v1.position.x - v2.position.x;
			v1v2.y = v1.position.y - v2.position.y;
			v3v4.x = v4.position.x - v3.position.x;
			v3v4.y = v4.position.y - v3.position.y;
			
			float dot = v1v2.dot(v3v4);
			float det = v1v2.det(v3v4);
			
			currentAngle = (float) Math.atan2(det, dot);
					
			float theta = stiffness * (currentAngle - targetAngle);
			
			theta = theta * 0.5f;
			
			float oldX1 = v1v2.x;
			v1v2.x = (float) (Math.cos(theta)*v1v2.x - Math.sin(theta)*v1v2.y);
			v1v2.y = (float) (Math.sin(theta)*oldX1 + Math.cos(theta)*v1v2.y);
			
			float oldX2 = v3v4.x;
			v3v4.x = (float) (Math.cos(-theta)*v3v4.x - Math.sin(-theta)*v3v4.y);
			v3v4.y = (float) (Math.sin(-theta)*oldX2 + Math.cos(-theta)*v3v4.y);
		}
	}
	
	public void correctError() {
		v1.position.x = v2.position.x - v1v2.x;
		v1.position.y = v2.position.y - v1v2.y;
		v4.position.x = v3.position.x - v3v4.x;
		v4.position.y = v3.position.y - v3v4.y;
	}
	
	public void render(Graphics2D g2, double alpha) {
		
		g2.setColor(Color.GREEN);
		g2.drawOval((int)v2.interpolatedPosition.x-2, (int)v2.interpolatedPosition.y-2, 4, 4);
		//g2.drawString(this.identity+"", this.v1.position.x+10, this.v1.position.y+10);
	}
	
	public float getLength(Vertex p1, Vertex p2) {
		float x = p2.position.x - p1.position.x;
		float y = p2.position.y - p1.position.y;
		return (float)Math.sqrt((x*x)+(y*y));
	}
	public float getLength(Vertex p1, Vec2 v2) {
		Vec2 center = p1.parent.getCenter();
		float x = (center.x + v2.x) - p1.position.x;
		float y = (center.y + v2.y) - p1.position.y;
		return (float)Math.sqrt((x*x)+(y*y));
	}
}
