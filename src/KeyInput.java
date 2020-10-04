
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener{
	/*
	 * Movement
	 */
	public static boolean W;
	public static boolean A;
	public static boolean S;
	public static boolean D;
	
	public KeyInput() {
		W = false;
		A = false;
		S = false;
		D = false;
	}
	
	public void keyPressed(KeyEvent e) {
		
		switch(e.getKeyCode()) {
			case KeyEvent.VK_W:
				W = true;
				break;
			case KeyEvent.VK_A:
				A = true;
				break;
			case KeyEvent.VK_S:
				S = true;
				break;
			case KeyEvent.VK_D:
				D = true;
				break;
			case KeyEvent.VK_DELETE:
				if(MouseInput.selected != null) {
					Main.entities.removeObject(MouseInput.selected);
					MouseInput.deselect();
				}
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
		}
		
	}

	public void keyReleased(KeyEvent e) {
		
			switch(e.getKeyCode()) {
			case KeyEvent.VK_W:
				W = false;
				break;
			case KeyEvent.VK_A:
				A = false;
				break;
			case KeyEvent.VK_S:
				S = false;
				break;
			case KeyEvent.VK_D:
				D = false;
				break;
		}
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
