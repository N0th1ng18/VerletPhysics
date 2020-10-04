import java.awt.Graphics2D;

public class Entities {
	
	public static int MAX_OBJECTS = 100000;
	public static int MAX_VERTICES = 100000;
	public static int MAX_EDGES = 100000;
	public static int MAX_CONSTRAINTS = 100000;
	public static int MAX_LONECONSTRAINTS = 100000;
	public static int MAX_IMAGES = 100;
	public static int MAX_ANGLECONSTRAINTS = 100000;
	
	public static int collisionIterations = 20;
	
	/*Objects*/
	public static Object[] objects;
	public static int objectCount = 0;
	
	public Entities() {
		init();
	}
	
	private void init() {
		/*Objects*/
		/*LEFT HANDED SYSTEM*/
		objects = new Object[MAX_OBJECTS];
	}
	
	public void update(double time, double dt) {
		/*
		 * UPDATE OBJECTS
		 */
		for(int i=0; i < objectCount; i++) {
			objects[i].update(time, dt);
		}
		
		/*
		 * COLLISION DETECTION
		 */
		for(int k=0; k < collisionIterations; k++) {
			for(int i=0; i < objectCount - 1; i++) {
				Object b0 = objects[i];
				
				for(int j=i+1; j < objectCount; j++) {
					Object b1 = objects[j];
					if(SATCollisionDetection.detectCollision(b0, b1) == true) {
						SATCollisionDetection.processCollision();
					}
				}
			}
		}
	}
	
	public void render(Graphics2D g2, double alpha) {
		for(int i=0; i < objectCount; i++) {
			objects[i].render(g2, alpha);
		}
	}
	
	public void addObject(Object o) {
		objects[objectCount] = o;
		objectCount++;
	}
	public void removeObject(Object o) {
		objects[o.identity] = null;
		objectCount--;
		
		/*
		 * Iterate through objects array to fill in the removed object.
		 */
		for(int i=o.identity; i < objects.length - o.identity; i++) {
			if(objects[i] == null && objects[i+1] == null) {
				break;
			}
			
			objects[i] = objects[i+1];
			objects[i+1] = null;
			objects[i].identity -= 1;
		}
	}
	
}
