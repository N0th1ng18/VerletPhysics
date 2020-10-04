import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class Main extends Canvas{
	/*
	 * Objects can become flat!!!
	 * 
	 * correct image on object
	 */
	
	/*
	 * Settings
	 */
	public static boolean editorMode = true;
	public static boolean menuBar = false;
	/*
	 *****************************************
	 */
	private static final long serialVersionUID = 1L;
	public int FPS = 0;
	public int UPDATES = 0;
	private double updatesPerSecond = 60.0;
	public static boolean running = true;
	private boolean showFPS = true;
	public static int WIDTH = 1920;
	public static int HEIGHT = 1080;
	public static boolean fullscreen = true;
	public static boolean renderWireFrame = true;
	
	public static JFrame frame;
	
	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mouseInput = new MouseInput();
	
	/*Pixels -> 0xAARRGGBB*/
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	//private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public static Entities entities = new Entities();
	
	public static void main(String[] args) {
		Main main = new Main();
		
		main.init();
		main.loop();
		main.terminate();
	}
	


	private void init() {
		setMinimumSize(new Dimension(WIDTH,HEIGHT));
		setMaximumSize(new Dimension(WIDTH,HEIGHT));
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		
		/*Input*/
		addKeyListener(keyInput);
		addMouseListener(mouseInput);
		 
		frame = new JFrame();
		frame.setTitle("2dGame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		this.setFocusable(true);
		if(editorMode == true && menuBar == true) {
			HEIGHT = HEIGHT - 23;
			frame.setJMenuBar(EditorMode.editorMenu());
		}
		if(fullscreen == true) {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			frame.setUndecorated(true);
		}else {
			frame.pack();
		}
		frame.setFocusable(true);
		frame.setVisible(true);	
		
	}

	private void loop() {
		/**********************************************/
		double time = 0.0;
		double dt = 1.0/updatesPerSecond;
		
		double accumulator = 0.0;
		double alpha = accumulator / dt;
		
		double newFrameTime = getNanoTimeInSeconds();
		double oldFrameTime = getNanoTimeInSeconds();
		double frameTime;
		/***********************************************/
		
		/*FPS*/
		double fps = getNanoTimeInSeconds();
		int renderCounter = 0;
		int updateCounter = 0;
		/***************************************/
		
		while(running) {
			newFrameTime = getNanoTimeInSeconds();
			frameTime = newFrameTime - oldFrameTime;
			oldFrameTime = newFrameTime;
			
			if(frameTime > 2.5) {
				frameTime = 2.5;
			}
			
			accumulator += frameTime;/*Adding length of frame*/
			
			while(accumulator >= dt) {
				
				update(time, dt);
				
				if(showFPS == true) {
					updateCounter++;
				}
				
				time += dt;
				accumulator -= dt;/*subtracting dt intervals*/
			}
			
			alpha = accumulator / dt;
			
			render(alpha);
			
			MouseInput.update();
			
			if(showFPS == true) {
				renderCounter++;
			}
			
			if(showFPS == true && (getNanoTimeInSeconds() - fps) >= 1.0) {
				FPS = renderCounter;
				UPDATES = updateCounter;
				renderCounter = 0;
				updateCounter = 0;
				fps = getNanoTimeInSeconds();
			}
		}
	}
	
	private void update(double time, double dt) {
		entities.update(time, dt);
	}
	
	private void render(double alpha) {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D)g;
	    RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_RENDERING,
	             RenderingHints.VALUE_RENDER_QUALITY);
	    g2.setRenderingHints(rh);
		/*****DRAW HERE***************/
	    g2.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
	    
		entities.render(g2, alpha);
		
		/*
		 * FPS
		 */
		if(showFPS == true) {
			g2.setColor(Color.GREEN);
			g2.drawString(FPS+"",0, 10);
			g2.drawString(UPDATES+"",0, 22);
		}
		/*****************************/
		g2.dispose();
		g.dispose();
		bs.show();
	}
	
	private void terminate() {
		System.exit(0);
	}
	
	private double getNanoTimeInSeconds() {
		return System.nanoTime()/(1000000000.0);
	}
}
