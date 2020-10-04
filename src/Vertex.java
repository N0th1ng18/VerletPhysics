import java.awt.Color;
import java.awt.Graphics2D;

public class Vertex {
	public Object parent;
	int identity = -1;
	
	Boolean fixed = false;
	
	Vec2 position;
	Vec2 oldPosition;
	
	Vec2 ghostVector;
	
	Vec2 temp;
	
	Vec2 interpolatedPosition;
	
	Vec2 startingPosition;
	private int iterations = 10;
	
	public Vertex(float x, float y, float oldX, float oldY) {
		position = new Vec2(x, y);
		oldPosition = new Vec2(oldX, oldY);
		
		temp = new Vec2(0,0);
		interpolatedPosition = new Vec2(0,0);
		
		startingPosition = new Vec2(x, y);
	}
	
	public void update(double time, double dt, Vec2 forces) {
		/*
		 * 			  Original Verlet:
		 * xi+1 = xi + (xi - xi-1) + a * dt * dt
		 * 	
		 * 			Time-Corrected Verlet:
		 * xi+1 = xi + (xi - xi-1) * (dti / dti-1) + a * dti * dti
		 * 		
		 */
		/**********************************/
		temp.x = position.x;
		temp.y = position.y;
		
		position.x += (position.x - oldPosition.x) * parent.viscosity;
		position.y += (position.y - oldPosition.y) * parent.viscosity;
		
		position.x += forces.x;
		position.y += forces.y;
		
		
		oldPosition.x = temp.x;
		oldPosition.y = temp.y;
		/**********************************/
		
		//
		// screen limits
		//
		if(parent.hasCollision == true) {
			if (position.y < 0f) {
				position.y = 0f;
			}
			else if (position.y > Main.HEIGHT-100) {
				position.x -= (position.x - oldPosition.x) * parent.groundFriction;
				position.y = Main.HEIGHT-100;
			}
	
			if (position.x < 0f) {
				position.x = 0f;
			}
			else if (position.x > Main.WIDTH) {
				position.x = Main.WIDTH;
			}
		}
		
		/*
		 * Fixed Point
		 */
		if(fixed == true) {
			position.x = startingPosition.x;
			position.y = startingPosition.y;
			oldPosition.x = startingPosition.x;
			oldPosition.y = startingPosition.y;
		}
		
		if(parent.editorMode == true) {
			//oldPosition.x = position.x;
			//oldPosition.y = position.y;
			position.x = temp.x;
			position.y = temp.y;
		}
	}
	
	public void reupdate() {
		for(int i=0; i < iterations ; i++) {
			//
			// screen limits
			//
			if(parent.hasCollision == true) {
				if (position.x < 0) {
					position.x = 0;
				} else if (position.x > Main.WIDTH) {
					position.x = Main.WIDTH;
				}
				
				if (position.y < 0) {
					position.y = 0;
				} else if (position.y > Main.HEIGHT-100) {
					position.y = Main.HEIGHT-100;
				}
			}
			
			
			/*
			 * Fixed Point
			 */
			if(fixed == true) {
				position.x = startingPosition.x;
				position.y = startingPosition.y;
				oldPosition.x = startingPosition.x;
				oldPosition.y = startingPosition.y;
			}
		}
	}
	
	public void render(Graphics2D g2, double alpha) {
		interpolatedPosition.x = (position.x * (float)alpha) + (oldPosition.x * (1 - (float)alpha));
		interpolatedPosition.y = (position.y * (float)alpha) + (oldPosition.y * (1 - (float)alpha));
		
		//g2.setColor(parent.color);
		//g2.drawString(this.identity+"", interpolatedPosition.x-4, interpolatedPosition.y-4);
		//g2.drawOval((int)(interpolatedPosition.x-2), (int)(interpolatedPosition.y-2), 4, 4);
		//g2.setColor(Color.green);
		//g2.drawOval((int)(oldPosition.x-2), (int)(oldPosition.y-2), 4, 4);
	}
}
