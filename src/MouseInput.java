
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener{
	public static float x,y;
	public static boolean leftPressed;
	public static boolean rightPressed;
	public static boolean middlePressed;
	public static boolean entered;
	
	public static boolean freezeSelected = false;
	
	public static Object selected;
	public static Color oldColor;
	
	public static Color SELECTED_COLOR = Color.MAGENTA;
	
	private static Vec2 dragStart = new Vec2(0.0f, 0.0f);
	private static boolean dragBool = false;
	private static boolean rotateBool = false;
	private static double currentAngle = 0.0;
	private static double oldAngle = 0.0;
	private static double deltaAngle = 0.0f;
	
	public MouseInput() {
		selected = null;
	}
	
	public static void select(Object o) {
		selected = o;
		oldColor = selected.color;
		selected.color = SELECTED_COLOR;
		selected.hasInput = true;
	}
	
	public static void deselect() {
		if(selected != null) {
			selected.color = oldColor;
			selected.hasInput = false;
			selected = null;
		}
	}
	
	public static void update() {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation(); 
		int mainFrame_x = Main.frame.getComponent(0).getLocationOnScreen().x;
		int mainFrame_y = Main.frame.getComponent(0).getLocationOnScreen().y;
		
		x = mouseLocation.x - mainFrame_x;
		y = mouseLocation.y - mainFrame_y;
		
		/*
		 * ***************************************************************************
		 * Editor Mode
		 * ***************************************************************************
		 */
		if(Main.editorMode == true) {
			/*
			 * Find Closest Object
			 */
			Object closestObject = null;
			float closestDistance = 1000000000000f;
			if(MouseInput.leftPressed == true && Entities.objectCount != 0) {
				for(int i=0; i < Entities.objectCount; i++) {
					Vec2 d = new Vec2(Entities.objects[i].center.x - x, Entities.objects[i].center.y - y);
					
					if(d.getMagnitude() < closestDistance) {
						closestDistance = d.getMagnitude();
						closestObject = Entities.objects[i];
					}
				}
			}
			
			/*
			 * Select Closest Object
			 */
			if(closestObject != null) {
				if(selected == null) {
					//Set Selected
					select(closestObject);
				}else {
					if(closestObject.identity != selected.identity && freezeSelected == false){
						//Closest Object and Selected Object Are Different
						deselect();
						select(closestObject);
					}
				}
			}
			
			/*
			 * ***************************************************************************
			 * Drag Object
			 * ***************************************************************************
			 */
			if(selected != null) {
				if(selected.editorMode == true) {
					boolean mouseOverSelected = SATCollisionDetection.mouseCollision(new Vec2(x, y), selected);
					
					/*
					 * Mouse Over Selected Object
					 */
					
					/*
					 * Middle Mouse over Selected Object Rotation
					 */
					if(middlePressed == true) {
						if(mouseOverSelected == true) {
							rotateBool = true;
						}else {
							rotateBool = false;
						}
					}else {
						rotateBool = false;
					}
					
					//if rotateBool is true
					//rotate Object based on center to mouse(x,y) vec
					if(rotateBool == true) {
						Vec2 center = selected.center;
						currentAngle = Math.atan2(y - center.y, x - center.x);
						deltaAngle = currentAngle - oldAngle;
						oldAngle  = currentAngle;
						
						for(int i=0; i < selected.vertexCount; i++) {
							Vec2 oldPos = new Vec2(selected.vertices[i].position.x, selected.vertices[i].position.y);
							Vec2 newPos = new Vec2(0.0f, 0.0f);
							
							oldPos = oldPos.sub(center);
							
							newPos.x = (float) (oldPos.x * Math.cos(deltaAngle) - oldPos.y * Math.sin(deltaAngle));
							newPos.y = (float) (oldPos.x * Math.sin(deltaAngle) + oldPos.y * Math.cos(deltaAngle));

							newPos = newPos.add(center);
							
							selected.vertices[i].position.x = newPos.x;
							selected.vertices[i].position.y = newPos.y;
						}
					}
					
					/*
					 * Left Click on Selected Object
					 */
					if(leftPressed == true) {
						/*
						 * Collision Detect Mouse Click to Selected Object
						 */
						if(mouseOverSelected == true) {
							dragBool = true;
						}else {
							dragBool = false;
						}
					}else {
						dragBool = false;
					}
					
					Vec2 sd = new Vec2(x - dragStart.x, y - dragStart.y);
					dragStart.x = x;
					dragStart.y = y;
					
					if(dragBool == true) {
						
						for(int i=0; i < selected.vertexCount; i++) {
							selected.vertices[i].position.x =  selected.vertices[i].position.x + sd.x;
							selected.vertices[i].position.y =  selected.vertices[i].position.y + sd.y;
							selected.vertices[i].oldPosition.x = selected.vertices[i].position.x;
							selected.vertices[i].oldPosition.y = selected.vertices[i].position.y;
							
						}
					}
					
				}
			}
			
		}
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		if(arg0.getButton() == 1) {
			leftPressed = true;
		}
		
		if(arg0.getButton() == 2) {
			if(selected != null && selected.editorMode == true) {
				oldAngle = Math.atan2(y - selected.center.y, x - selected.center.x);
			}
			
			middlePressed = true;
		}
		
		if(arg0.getButton() == 3) {
			if(Main.editorMode == true && MouseInput.entered == true) {
				KeyInput.W = false;
				KeyInput.A = false;
				KeyInput.S = false;
				KeyInput.D = false;
				EditorMode.rightClickMenu(arg0);
			}
			rightPressed = true;
		}
		
		x = arg0.getX();
		x = arg0.getY();
	}

	public void mouseReleased(MouseEvent arg0) {
		if(arg0.getButton() == 1) {
			leftPressed = false;
		}
		if(arg0.getButton() == 2) {
			middlePressed = false;
		}
		if(arg0.getButton() == 3) {
			rightPressed = false;
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		entered = true;
	}

	public void mouseExited(MouseEvent arg0) {
		entered = false;
	}

	public void mouseClicked(MouseEvent arg0) {
		
	}

}
