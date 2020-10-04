import java.awt.Graphics2D;

public class Edge {
	public Object parent;
	public int identity = -1;
	
	public Vertex v1;
	public Vertex v2;
	
	public Vec2 v1v2;
	
	private float targetLength;
	private float currentLength;
	private float delta;
	
	public float stiffness;
	public float stiffnessPower;
	public float distanceRatio;
	
	public Edge(Vertex p1, Vertex p2) {
		this.v1 = p1;
		this.v2 = p2;
		
		targetLength = getLength();
		
		v1v2 = new Vec2(0f,0f);
	}
	
	public void update() {
		/*
		 * Update Constraints
		 */
			
			/*
			 * Gets v1 and v2 Vector
			 */
			v1v2.x = v2.position.x - v1.position.x;
			v1v2.y = v2.position.y - v1.position.y;
			
			/*
			 * Gets Current Distance
			 */
			currentLength = v1v2.getMagnitude();
			
			distanceRatio = (currentLength - targetLength)/targetLength;
			
			delta = (float) ((Math.pow(Math.abs(distanceRatio), stiffnessPower)*(stiffness)) * distanceRatio);
			
			delta = delta * 0.5f;
	}
	
	public void correctError() {
		if(currentLength > targetLength * 2) {
			
		}else {
		
			/*
			 * Correct Error
			 */
			v1.position.x += v1v2.x*delta;
			v1.position.y += v1v2.y*delta;
			
			v2.position.x -= v1v2.x*delta;
			v2.position.y -= v1v2.y*delta;
		}
			
	}

	public void render(Graphics2D g2, double alpha) {
		
		g2.setColor(parent.color);
		g2.drawLine((int)v1.interpolatedPosition.x, (int)v1.interpolatedPosition.y, (int)v2.interpolatedPosition.x, (int)v2.interpolatedPosition.y);
		
		//g2.drawString(this.identity+"", this.v1.position.x-10, this.v1.position.y-10);
	}
	
	public float getLength() {
		float x = v2.position.x - v1.position.x;
		float y = v2.position.y - v1.position.y;
		return (float)Math.sqrt((x*x)+(y*y));
	}
	public float getLength(Vertex p1, Vertex p2) {
		float x = p2.position.x - p1.position.x;
		float y = p2.position.y - p1.position.y;
		return (float)Math.sqrt((x*x)+(y*y));
	}
}
