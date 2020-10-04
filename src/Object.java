import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Object {
	int identity = -1;
	
	/*
	 * Object Constants
	 */
	
	/*
	 * Object Settings
	 */
	public int iterations = 10;
	public float edgeStiffness = 0.05f;//[0,0.5]
	public float constraintStiffness = 0.05f;
	
	public float mSpeed = 1.0f;
	public float viscosity = 1.0f;// % of speed
	public float groundFriction = 0.9f;// [0,1]
	public float collisionFriction = 0.01f;// [0,1]
	public float recoverForce = 0.01f;
	public static Vec2 gravity = new Vec2(0.0f, 0.098f);
	boolean showImage = true;
	
	public Color color = Color.RED;
	public Vec2 forces;
	public float mass;
	public boolean hasInput = false;
	public boolean hasCollision = true;
	public boolean editorMode = false;
	
	/*
	 * Object Contains
	 */
	Vertex[] vertices;
	int vertexCount;
	Edge[] edges;
	int edgeCount;
	Constraint[] constraints;
	int constraintCount;
	Vec2 center;
	private BufferedImage[] images;
	int imageCount;
	
	
	/*
	 * Render
	 */
	private Vec2 scale;
	private Vec2 offset;
	Vec2 interpolatedCenter;
	
	/*
	 * Collision
	 */
	CollisionInfo collisionInfo;
	Vec2 boundingBoxCenter;
	Vec2 boundingBoxHalfEx;
	float minX;
	float minY;
	float maxX;
	float maxY;
	
	public Object(float mass) {
		this.identity = Entities.objectCount;
		this.mass = mass;
		
		init();
	}
	
	private void init() {
		/*
		 * init Object Contains
		 */
		center = new Vec2(0f,0f);
		
		vertices = new Vertex[Entities.MAX_VERTICES];
		vertexCount = 0;
		edges = new Edge[Entities.MAX_EDGES];
		edgeCount = 0;
		constraints = new Constraint[Entities.MAX_CONSTRAINTS];
		constraintCount = 0;
		images = new BufferedImage[Entities.MAX_IMAGES];
		imageCount = 0;
		
		/*
		 * init Collision
		 */
		collisionInfo = new CollisionInfo();
		boundingBoxCenter = new Vec2(0f,0f);
		boundingBoxHalfEx = new Vec2(0f,0f);
		boundingBox();
		
		/*
		 * Render
		 */
		scale = new Vec2(1f,1f);
		offset = new Vec2(-50f,-50f);
		interpolatedCenter = new Vec2(0f,0f);
		forces = new Vec2(0.0f, 0.0f);
	}

	public void update(double time, double dt) {
		
		/*
		 * UPDATE VERTICES
		 */
		for(int i=0; i < vertexCount; i++) {
			vertices[i].update(time, dt, forces);
		}
		
		getCenter();//For mouse click
		
		if(hasInput == true) {
			input();
		}
		
		for(int i=0; i < iterations; i++) {
			/*
			 * SOLVE EDGES AND CONSTRAINTS
			 *  -> Updating and fixing constraints affect other constraints
			 *  -> Therefor, update all constraints and then correct error
			 */
					for(int j=0; j < edgeCount; j++) {
						edges[j].update();
					}
					
					for(int j=0; j < constraintCount; j++) {
						constraints[j].update();
					}
				
				/*
				 * Correct Error
				 */
					for(int j=0; j < edgeCount; j++) {
						edges[j].correctError();
					}
					
					for(int j=0; j < constraintCount; j++) {
						constraints[j].correctError();
					}
				
				
			/*
			 * REUPDATE
			 */	
				
				for(int j=0; j < vertexCount; j++) {
					vertices[j].reupdate();
				}
				
			/*
			 * RECALCULATE BOUNDING BOXES
			 */
				boundingBox();
		}
	}

	public void render(Graphics2D g2, double alpha) {
		/*
		 * Get rotation Amount
		 */
		this.getInterpolatedCenter();
		Vec2 d = vertices[1].interpolatedPosition.sub(vertices[0].interpolatedPosition);
		float rotImage = (float) Math.atan2(d.y, d.x);
			
		/*
		 * Render Image
		 */	
		AffineTransform oldTransform = g2.getTransform();
		/************************************************/
			g2.scale(scale.x, scale.y);
			g2.rotate(rotImage, interpolatedCenter.x, interpolatedCenter.y);/*Rotates Image*/
			g2.translate(interpolatedCenter.x, interpolatedCenter.y);
			if(showImage == true)
				g2.drawImage(images[0], (int)(offset.x), (int)(offset.y), 100, 100, null);
		/***************************************************/	
		g2.setTransform(oldTransform);
		
			/*
			 * RENDER VERTICES
			 */
			
			for(int i=0; i < vertexCount; i++) {
				vertices[i].render(g2, alpha);
			}
			
		if(Main.renderWireFrame == true) {	
			/*
			 * RENDER EDGES
			 */
			for(int i=0; i < edgeCount; i++) {
				edges[i].render(g2, alpha);
			}
			
			/*
			 * RENDER CONSTRAINTS
			 */
			for(int i=0; i < constraintCount; i++) {
				constraints[i].render(g2, alpha);
			}
		}
		/*
		 * Render Debug
		 */
		//if(collisionInfo != null) {
		//   g2.drawString(""+this.identity+"", this.getCenter().x, this.getCenter().y);
		//}
		//g2.setColor(Color.RED);
		//g2.drawRect((int)(this.boundingBoxCenter.x-this.boundingBoxHalfEx.x), (int)(this.boundingBoxCenter.y-this.boundingBoxHalfEx.y), (int)(this.boundingBoxHalfEx.x*2.0f), (int)(this.boundingBoxHalfEx.y*2.0f));
	}
	
	public void addVertex(Vertex newVertex) {
		/*
		 * Add new Vertex
		 */
		vertices[vertexCount] = newVertex;
		vertexCount++;
		newVertex.parent = this;
		newVertex.identity = vertexCount;
	}
	
	public void addEdge(Edge newEdge) {
		/*
		 * Add new Edge
		 */
		edges[edgeCount] = newEdge;
		edgeCount++;
		newEdge.parent = this;
		newEdge.identity = edgeCount;
		newEdge.stiffness = edgeStiffness;
	}
	
	public void addConstraint(Constraint newConstraint) {
		/*
		 * Add new Constraint
		 */
		constraints[constraintCount] = newConstraint;
		constraintCount++;
		newConstraint.parent = this;
		newConstraint.identity = constraintCount;
		newConstraint.stiffness = constraintStiffness;
	}
	
	public void addImage(String path, int x, int y, int w, int h) {
		try {
			 BufferedImage img = ImageIO.read(new File(path));
			 if(img.getWidth() < w)w = img.getWidth();
			 if(img.getHeight() < h)h = img.getHeight();
			 images[imageCount] = img.getSubimage(x, y, w, h);
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageCount++;
	}
	
	public void setImage(String path, int x, int y, int w, int h) {
		try {
			 BufferedImage img = ImageIO.read(new File(path));
			 if(img.getWidth() < w)w = img.getWidth();
			 if(img.getHeight() < h)h = img.getHeight();
			 images[0] = img.getSubimage(x, y, w, h);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addForce(Vec2 force) {
		forces.x += force.x;
		forces.y += force.y;
	}
	
	private void input() {
		
		if(KeyInput.W == true) {
			for(int i=0;i < this.vertexCount; i++) {
					this.vertices[i].position.y -= mSpeed;
			}
		}
		if(KeyInput.A == true) {
			for(int i=0;i < this.vertexCount; i++) {
					this.vertices[i].position.x -= mSpeed;
			}
		}
		if(KeyInput.D == true) {
			for(int i=0;i < this.vertexCount; i++) {
					this.vertices[i].position.x += mSpeed;
			}
		}
		if(KeyInput.S == true) {
			for(int i=0;i < this.vertexCount; i++) {
					this.vertices[i].position.y += mSpeed;
			}
		}
	}
	
	public void boundingBox() {

		minX = 100000000.0f;
		minY = 100000000.0f;
		maxX = -100000000.0f;
		maxY = -100000000.0f;
		
		for (int i = 0; i < this.vertexCount; i++) {
			Vec2 p = this.vertices[i].position;

			if (p.x > maxX) maxX = p.x;
			if (p.y > maxY) maxY = p.y;
			if (p.x < minX) minX = p.x;
			if (p.y < minY) minY = p.y;
		}

		// center
		this.boundingBoxCenter.x = (minX + maxX) * 0.5f;
		this.boundingBoxCenter.y = (minY + maxY) * 0.5f;

		// half extents
		this.boundingBoxHalfEx.x = (maxX - minX) * 0.5f;
		this.boundingBoxHalfEx.y = (maxY - minY) * 0.5f;
		
	}
	
	
	public Vec2 projectToAxis(Vec2 axis, Vec2 minmax) {
		/*
		 * minmax is a vector (min, max)
		 */
		float dotP = vertices[0].position.dot(axis);
		
		minmax.x = dotP;
		minmax.y = dotP;
		
		for(int i=1; i < vertexCount; i++) {
			dotP = vertices[i].position.dot(axis);
			
			if(dotP < minmax.x) minmax.x = dotP;
			if(dotP > minmax.y)	minmax.y = dotP;
		}
		
		return minmax;
	}
	
	public Vec2 getCenter() {
		float add_x = 0.0f;
		float add_y = 0.0f;
		for(int i=0; i < vertexCount; i++) {
			add_x += vertices[i].position.x;
			add_y += vertices[i].position.y;
		}
		center.x = add_x/vertexCount;
		center.y = add_y/vertexCount;
		
		return center;
	}
	
	public Vec2 getInterpolatedCenter() {
		int add_x = 0;
		int add_y = 0;
		for(int i=0; i < vertexCount; i++) {
			add_x += vertices[i].interpolatedPosition.x;
			add_y += vertices[i].interpolatedPosition.y;
		}
		interpolatedCenter.x = add_x/vertexCount;
		interpolatedCenter.y = add_y/vertexCount;
		
		return interpolatedCenter;
	}
}
