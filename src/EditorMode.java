import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.SpringLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.border.LineBorder;

public class EditorMode{
	public static File file = null;
	
	private static final Color windowColor = Color.LIGHT_GRAY;
	private static final float windowOpacity = 0.9f;
	
	/*
	 * createObject -> Save Past Inputs
	 */
	private static String fcLabelS = "Unselected";
	private static String tf00S = "00";
	private static String tf0S = "0";
	private static String tf002S = "60";
	private static String tf02S = "50";
	private static String tf1S = "6";
	private static String tf2S = "60.0";
	private static String tf3S = "100.0";
	private static String tf4S = "100.0";
	private static String tf5S = "100.0";
	private static String tf6S = "2.0";
	private static String tf7S = "10";
	private static String tf8S = "0.05";
	private static String tf9S = "0.05";
	private static String tf10S = "1.0";
	private static String tf11S = "1.0";
	private static String tf12S = "0.3";
	private static String tf13S = "0.01";
	/*
	 * 
	 */
	
	public static JMenuBar editorMenu() {
		JMenuBar menuBar = new JMenuBar();
		//ImageIcon icon = new ImageIcon("filePath");
		
		/*
		 * File Menu Items
		 */
		JMenu file = new JMenu("File");
				
			JMenuItem quit = new JMenuItem("Exit");
				quit.addActionListener((ActionEvent event) -> {
					Main.running = false;
				});
			
		/*
		 * Add File Menu Items	
		 */
		file.add(quit);
		
		/*
		 * Add To MenuBaR
		 */
		menuBar.add(file);
		
		return menuBar;
	}
	
	public static void rightClickMenu(MouseEvent arg0) {
		JPopupMenu menu = new JPopupMenu();
		menu.add(new JLabel("MENU"));
		
		menu.addSeparator();
			JMenuItem createObject = new JMenuItem("> Create Object");
				createObject.addActionListener((ActionEvent event) -> {
					createObject();
				});
		menu.add(createObject);
		
			
			
		if(MouseInput.selected != null) {	
			menu.addSeparator();
			menu.add(new JLabel("SELECTED OBJECT"));
			menu.addSeparator();
					JMenuItem deselect = new JMenuItem("> Deselect");
						deselect.addActionListener((ActionEvent event) -> {
							MouseInput.deselect();
						});	
				menu.add(deselect);
						JMenuItem edit = new JMenuItem("> Edit");
						edit.addActionListener((ActionEvent event) -> {
							edit(MouseInput.selected);
						});	
				menu.add(edit);
			
			menu.addSeparator();
		}

		
		menu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
	}
	
	public static void edit(Object selected) {
		/*
		 * make it look up objects settings instead of last input
		 */
		selected.editorMode = true;
		selected.hasCollision = false;
		MouseInput.freezeSelected = true;
		
		JFrame frame = new JFrame();
		frame.setTitle("Edit");
		Vec2 frameSize = new Vec2(400.0f, 400.0f);
		frame.setSize((int)frameSize.x, (int)frameSize.y);
		frame.setLocation((Main.WIDTH/2)-((int)frameSize.x/2), (Main.HEIGHT/2)-((int)frameSize.y/2));
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.setLayout(new BorderLayout());
		frame.setFocusable(true);
		frame.setBackground(windowColor);
		frame.setOpacity(windowOpacity);
		//frame.addMouseListener(Main.mouseInput);
		/*
		 * Frame Setup
		 */
			JPanel northPane = new JPanel();
			northPane.setBackground(windowColor);
				JLabel title = new JLabel("Edit");
			northPane.add(title, BorderLayout.CENTER);
			northPane.addMouseListener(Main.mouseInput);
			
			JPanel objectPane = new JPanel();
			objectPane.setBackground(windowColor);
			SpringLayout layout = new SpringLayout();
			objectPane.setLayout(layout);
				/*
				 * Center Pane
				 */
					JButton reset = new JButton("Reset Position");
					reset.setBackground(windowColor);
					reset.setFocusPainted(false);
					reset.addActionListener((ActionEvent event) -> {
						for(int i=0; i < selected.vertexCount; i++) {
							selected.vertices[i].position.x = selected.vertices[i].startingPosition.x;
							selected.vertices[i].position.y = selected.vertices[i].startingPosition.y;
							selected.vertices[i].oldPosition.x = selected.vertices[i].startingPosition.x;
							selected.vertices[i].oldPosition.y = selected.vertices[i].startingPosition.y;
						}
					});
					objectPane.add(reset);
				layout.putConstraint(SpringLayout.NORTH, reset, 5, SpringLayout.NORTH, objectPane);
				layout.putConstraint(SpringLayout.WEST, reset, 5, SpringLayout.WEST, objectPane);
				
					JButton saveSPos = new JButton("Save Position");
					saveSPos.setBackground(windowColor);
					saveSPos.setFocusPainted(false);
					saveSPos.addActionListener((ActionEvent event) -> {
						for(int i=0; i < selected.vertexCount; i++) {
							selected.vertices[i].startingPosition.x = selected.vertices[i].position.x;
							selected.vertices[i].startingPosition.y = selected.vertices[i].position.y;
						}
					});
					objectPane.add(saveSPos);
				layout.putConstraint(SpringLayout.NORTH, saveSPos, 5, SpringLayout.NORTH, objectPane);
				layout.putConstraint(SpringLayout.WEST, saveSPos, 5, SpringLayout.EAST, reset);
				
					JLabel label00 = new JLabel("ImgX: ");
					JTextField tf00 = new JTextField();
					tf00.setColumns(5);
					tf00.setText(tf00S);
					tf00.setBackground(windowColor);
					tf00.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label00);
				layout.putConstraint(SpringLayout.NORTH, label00, 5, SpringLayout.SOUTH, reset);
				layout.putConstraint(SpringLayout.WEST, label00, 25, SpringLayout.WEST, objectPane);
				objectPane.add(tf00);
				layout.putConstraint(SpringLayout.NORTH, tf00, 5, SpringLayout.SOUTH, reset);
				layout.putConstraint(SpringLayout.WEST, tf00, 5, SpringLayout.EAST, label00);
				
					JLabel label0 = new JLabel("ImgY: ");
					JTextField tf0 = new JTextField();
					tf0.setColumns(5);
					tf0.setText(tf0S);
					tf0.setBackground(windowColor);
					tf0.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label0);
				layout.putConstraint(SpringLayout.NORTH, label0, 5, SpringLayout.SOUTH, reset);
				layout.putConstraint(SpringLayout.WEST, label0, 38, SpringLayout.EAST, tf00);
				objectPane.add(tf0);
				layout.putConstraint(SpringLayout.NORTH, tf0, 5, SpringLayout.SOUTH, reset);
				layout.putConstraint(SpringLayout.WEST, tf0, 5, SpringLayout.EAST, label0);
				
					JLabel label002 = new JLabel("IWidth: ");
					JTextField tf002 = new JTextField();
					tf002.setColumns(5);
					tf002.setText(tf002S);
					tf002.setBackground(windowColor);
					tf002.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label002);
				layout.putConstraint(SpringLayout.NORTH, label002, 5, SpringLayout.SOUTH, label00);
				layout.putConstraint(SpringLayout.WEST, label002, 18, SpringLayout.WEST, objectPane);
				objectPane.add(tf002);
				layout.putConstraint(SpringLayout.NORTH, tf002, 5, SpringLayout.SOUTH, label00);
				layout.putConstraint(SpringLayout.WEST, tf002, 5, SpringLayout.EAST, label002);
				
					JLabel label02 = new JLabel("IHeight: ");
					JTextField tf02 = new JTextField();
					tf02.setColumns(5);
					tf02.setText(tf02S);
					tf02.setBackground(windowColor);
					tf02.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label02);
				layout.putConstraint(SpringLayout.NORTH, label02, 5, SpringLayout.SOUTH, label00);
				layout.putConstraint(SpringLayout.WEST, label02, 27, SpringLayout.EAST, tf002);
				objectPane.add(tf02);
				layout.putConstraint(SpringLayout.NORTH, tf02, 5, SpringLayout.SOUTH, label00);
				layout.putConstraint(SpringLayout.WEST, tf02, 5, SpringLayout.EAST, label02);
				
				
					JFileChooser fc = new JFileChooser("src");
					JLabel fcLabel = new JLabel(fcLabelS);
					fcLabel.setText(fcLabelS);
					JButton fcButton = new JButton("Image File");
					fcButton.setAlignmentX(JLabel.LEFT_ALIGNMENT);
					fcButton.setBackground(windowColor);
					fcButton.setFocusPainted(false);
					fcButton.addActionListener((ActionEvent event) -> {
						if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION ) {
							file = fc.getSelectedFile();
						}
						if(file != null) {
							fcLabel.setText(file.getPath());
						}
					});
				objectPane.add(fcLabel);
				layout.putConstraint(SpringLayout.NORTH, fcLabel, 11, SpringLayout.SOUTH, label002);
				layout.putConstraint(SpringLayout.WEST, fcLabel, 5, SpringLayout.EAST, fcButton);
				objectPane.add(fcButton);
				layout.putConstraint(SpringLayout.NORTH, fcButton, 7, SpringLayout.SOUTH, label002);
				layout.putConstraint(SpringLayout.WEST, fcButton, 5, SpringLayout.WEST, objectPane);
					
					JLabel label5 = new JLabel("Mass: ");
					JTextField tf5 = new JTextField();
					tf5.setColumns(5);
					tf5.setText(tf5S);
					tf5.setBackground(windowColor);
					tf5.setBorder(new LineBorder(Color.BLACK));
					
				objectPane.add(label5);
				layout.putConstraint(SpringLayout.NORTH, label5, 5, SpringLayout.SOUTH, fcButton);
				layout.putConstraint(SpringLayout.WEST, label5, 23, SpringLayout.WEST, objectPane);
				objectPane.add(tf5);
				layout.putConstraint(SpringLayout.NORTH, tf5, 5, SpringLayout.SOUTH, fcButton);
				layout.putConstraint(SpringLayout.WEST, tf5, 5, SpringLayout.EAST, label5);
				
					JLabel label6 = new JLabel("MSpeed: ");
					JTextField tf6 = new JTextField();
					tf6.setColumns(5);
					tf6.setText(tf6S);
					tf6.setBackground(windowColor);
					tf6.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label6);
				layout.putConstraint(SpringLayout.NORTH, label6, 5, SpringLayout.SOUTH, label5);
				layout.putConstraint(SpringLayout.WEST, label6, 8, SpringLayout.WEST, objectPane);
				objectPane.add(tf6);
				layout.putConstraint(SpringLayout.NORTH, tf6, 5, SpringLayout.SOUTH, label5);
				layout.putConstraint(SpringLayout.WEST, tf6, 5, SpringLayout.EAST, label6);
					
					JLabel label7 = new JLabel("Iterations: ");
					JTextField tf7 = new JTextField();
					tf7.setColumns(5);
					tf7.setText(tf7S);
					tf7.setBackground(windowColor);
					tf7.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label7);
				layout.putConstraint(SpringLayout.NORTH, label7, 5, SpringLayout.SOUTH, label6);
				layout.putConstraint(SpringLayout.WEST, label7, 1, SpringLayout.WEST, objectPane);
				objectPane.add(tf7);
				layout.putConstraint(SpringLayout.NORTH, tf7, 5, SpringLayout.SOUTH, label6);
				layout.putConstraint(SpringLayout.WEST, tf7, 4, SpringLayout.EAST, label7);
				
					JLabel label8 = new JLabel("EdgeStiff: ");
					JTextField tf8 = new JTextField();
					tf8.setColumns(5);
					tf8.setText(tf8S);
					tf8.setBackground(windowColor);
					tf8.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label8);
				layout.putConstraint(SpringLayout.NORTH, label8, 5, SpringLayout.SOUTH, label5);
				layout.putConstraint(SpringLayout.WEST, label8, 15, SpringLayout.EAST, tf6);
				objectPane.add(tf8);
				layout.putConstraint(SpringLayout.NORTH, tf8, 5, SpringLayout.SOUTH, label5);
				layout.putConstraint(SpringLayout.WEST, tf8, 5, SpringLayout.EAST, label8);
				
					JLabel label9 = new JLabel("ConStiff: ");
					JTextField tf9 = new JTextField();
					tf9.setColumns(5);
					tf9.setText(tf9S);
					tf9.setBackground(windowColor);
					tf9.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label9);
				layout.putConstraint(SpringLayout.NORTH, label9, 5, SpringLayout.SOUTH, label6);
				layout.putConstraint(SpringLayout.WEST, label9, 21, SpringLayout.EAST, tf6);
				objectPane.add(tf9);
				layout.putConstraint(SpringLayout.NORTH, tf9, 5, SpringLayout.SOUTH, label6);
				layout.putConstraint(SpringLayout.WEST, tf9, 5, SpringLayout.EAST, label9);
				
					JLabel label10 = new JLabel("Viscosity: ");
					JTextField tf10 = new JTextField();
					tf10.setColumns(5);
					tf10.setText(tf10S);
					tf10.setBackground(windowColor);
					tf10.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label10);
				layout.putConstraint(SpringLayout.NORTH, label10, 5, SpringLayout.SOUTH, label9);
				layout.putConstraint(SpringLayout.WEST, label10, 2, SpringLayout.WEST, objectPane);
				objectPane.add(tf10);
				layout.putConstraint(SpringLayout.NORTH, tf10, 5, SpringLayout.SOUTH, label9);
				layout.putConstraint(SpringLayout.WEST, tf10, 5, SpringLayout.EAST, label10);
				
					JLabel label12 = new JLabel("GFriction: ");
					JTextField tf12 = new JTextField();
					tf12.setColumns(5);
					tf12.setText(tf12S);
					tf12.setBackground(windowColor);
					tf12.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label12);
				layout.putConstraint(SpringLayout.NORTH, label12, 5, SpringLayout.SOUTH, label10);
				layout.putConstraint(SpringLayout.WEST, label12, 4, SpringLayout.WEST, objectPane);
				objectPane.add(tf12);
				layout.putConstraint(SpringLayout.NORTH, tf12, 5, SpringLayout.SOUTH, label10);
				layout.putConstraint(SpringLayout.WEST, tf12, 5, SpringLayout.EAST, label12);
				
					JLabel label13 = new JLabel("ColFriction: ");
					JTextField tf13 = new JTextField();
					tf13.setColumns(5);
					tf13.setText(tf13S);
					tf13.setBackground(windowColor);
					tf13.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label13);
				layout.putConstraint(SpringLayout.NORTH, label13, 5, SpringLayout.SOUTH, label10);
				layout.putConstraint(SpringLayout.WEST, label13, 6, SpringLayout.EAST, tf12);
				objectPane.add(tf13);
				layout.putConstraint(SpringLayout.NORTH, tf13, 5, SpringLayout.SOUTH, label10);
				layout.putConstraint(SpringLayout.WEST, tf13, 5, SpringLayout.EAST, label13);
				
				/*
				 * 
				 */
			
			JPanel bottomPane = new JPanel();
			bottomPane.setBackground(windowColor);
				JButton update = new JButton("Update");
				update.setBackground(windowColor);
				update.setFocusPainted(false);
				update.addActionListener((ActionEvent event) -> {
					/*
					 * Updates Object Info
					 */
					 
					MouseInput.selected.mass = Float.parseFloat(tf5.getText());
					if(file != null)
					MouseInput.selected.setImage(file.getPath(), Integer.parseInt(tf00.getText().replaceAll("\\s","")), Integer.parseInt(tf0.getText().replaceAll("\\s","")), Integer.parseInt(tf002.getText().replaceAll("\\s","")), Integer.parseInt(tf02.getText().replaceAll("\\s","")));
					
					for(int i=0; i < selected.edgeCount; i++) {
						selected.edges[i].stiffness = Float.parseFloat(tf8.getText().replaceAll("\\s",""));
					}
					for(int i=0; i < selected.constraintCount; i++) {
						selected.constraints[i].stiffness = Float.parseFloat(tf9.getText().replaceAll("\\s",""));
					}
					selected.mSpeed = Float.parseFloat(tf6.getText());
					selected.viscosity = Float.parseFloat(tf10.getText().replaceAll("\\s",""));
					selected.groundFriction = Float.parseFloat(tf12.getText().replaceAll("\\s",""));
					selected.collisionFriction = Float.parseFloat(tf13.getText().replaceAll("\\s",""));
					selected.iterations = Integer.parseInt(tf7.getText());
					
					/*
					 * Save Inputs
					 */
					if(file != null) {
						fcLabelS = file.getPath();
					}
					
					tf00S = tf00.getText();
					tf0S = tf0.getText();
					tf002S = tf002.getText();
					tf02S = tf02.getText();
					tf5S = tf5.getText();
					tf6S = tf6.getText();
					tf7S = tf7.getText();
					tf8S = tf8.getText();
					tf9S = tf9.getText();
					tf10S = tf10.getText();
					tf12S = tf12.getText();
					tf13S = tf13.getText();
					/*
					 * 
					 */
					
				});
			
				JButton addOK = new JButton("Export Object");
				addOK.setBackground(windowColor);
				addOK.setFocusPainted(false);
				addOK.addActionListener((ActionEvent event) -> {
					selected.editorMode = false;
					selected.hasCollision = true;
					MouseInput.freezeSelected = false;
					frame.dispose();
					/*
					 * Export Object
					 */
				});
				
				JButton cancel = new JButton("OK");
				cancel.setBackground(windowColor);
				cancel.setFocusPainted(false);
				cancel.addActionListener((ActionEvent event) -> {
					selected.editorMode = false;
					selected.hasCollision = true;
					MouseInput.freezeSelected = false;
					frame.dispose();
				});
			bottomPane.add(update);
			bottomPane.add(addOK);
			bottomPane.add(cancel);
		
		/*
		 * ******************************************************
		 */
		
		frame.add(northPane, BorderLayout.NORTH);
		frame.add(objectPane, BorderLayout.CENTER);
		frame.add(bottomPane, BorderLayout.PAGE_END);
		frame.setVisible(true);
	}
	
	public static void createObject() {
		JFrame frame = new JFrame();
		frame.setTitle("Create Object");
		Vec2 frameSize = new Vec2(400.0f, 300.0f);
		frame.setSize((int)frameSize.x, (int)frameSize.y);
		frame.setLocation((Main.WIDTH/2)-((int)frameSize.x/2), (Main.HEIGHT/2)-((int)frameSize.y/2));
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.setLayout(new BorderLayout());
		frame.setOpacity(windowOpacity);

		/*
		 * Frame Setup
		 */
		
			JPanel northPane = new JPanel();
			northPane.setBackground(windowColor);
				JLabel title = new JLabel("Create Object");
			northPane.add(title, BorderLayout.CENTER);
		
			/*
			 * Center Pane
			 */
			JPanel objectPane = new JPanel();
			objectPane.setBackground(windowColor);
			SpringLayout layout = new SpringLayout();
			objectPane.setLayout(layout);
			
					JLabel label00 = new JLabel("ImgX: ");
					JTextField tf00 = new JTextField();
					tf00.setColumns(5);
					tf00.setText(tf00S);
					tf00.setBackground(windowColor);
					tf00.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label00);
				layout.putConstraint(SpringLayout.NORTH, label00, 5, SpringLayout.NORTH, objectPane);
				layout.putConstraint(SpringLayout.WEST, label00, 25, SpringLayout.WEST, objectPane);
				objectPane.add(tf00);
				layout.putConstraint(SpringLayout.NORTH, tf00, 5, SpringLayout.NORTH, objectPane);
				layout.putConstraint(SpringLayout.WEST, tf00, 5, SpringLayout.EAST, label00);
				
					JLabel label0 = new JLabel("ImgY: ");
					JTextField tf0 = new JTextField();
					tf0.setColumns(5);
					tf0.setText(tf0S);
					tf0.setBackground(windowColor);
					tf0.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label0);
				layout.putConstraint(SpringLayout.NORTH, label0, 5, SpringLayout.NORTH, objectPane);
				layout.putConstraint(SpringLayout.WEST, label0, 38, SpringLayout.EAST, tf00);
				objectPane.add(tf0);
				layout.putConstraint(SpringLayout.NORTH, tf0, 5, SpringLayout.NORTH, objectPane);
				layout.putConstraint(SpringLayout.WEST, tf0, 5, SpringLayout.EAST, label0);
				
					JLabel label002 = new JLabel("IWidth: ");
					JTextField tf002 = new JTextField();
					tf002.setColumns(5);
					tf002.setText(tf002S);
					tf002.setBackground(windowColor);
					tf002.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label002);
				layout.putConstraint(SpringLayout.NORTH, label002, 5, SpringLayout.SOUTH, label00);
				layout.putConstraint(SpringLayout.WEST, label002, 18, SpringLayout.WEST, objectPane);
				objectPane.add(tf002);
				layout.putConstraint(SpringLayout.NORTH, tf002, 5, SpringLayout.SOUTH, label00);
				layout.putConstraint(SpringLayout.WEST, tf002, 5, SpringLayout.EAST, label002);
				
					JLabel label02 = new JLabel("IHeight: ");
					JTextField tf02 = new JTextField();
					tf02.setColumns(5);
					tf02.setText(tf02S);
					tf02.setBackground(windowColor);
					tf02.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label02);
				layout.putConstraint(SpringLayout.NORTH, label02, 5, SpringLayout.SOUTH, label00);
				layout.putConstraint(SpringLayout.WEST, label02, 27, SpringLayout.EAST, tf002);
				objectPane.add(tf02);
				layout.putConstraint(SpringLayout.NORTH, tf02, 5, SpringLayout.SOUTH, label00);
				layout.putConstraint(SpringLayout.WEST, tf02, 5, SpringLayout.EAST, label02);
				
				
					JFileChooser fc = new JFileChooser("src");
					JLabel fcLabel = new JLabel(fcLabelS);
					fcLabel.setText(fcLabelS);
					JButton fcButton = new JButton("Image File");
					fcButton.setAlignmentX(JLabel.LEFT_ALIGNMENT);
					fcButton.setBackground(windowColor);
					fcButton.setFocusPainted(false);
					fcButton.addActionListener((ActionEvent event) -> {
						if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION ) {
							file = fc.getSelectedFile();
						}
						if(file != null) {
							fcLabel.setText(file.getPath());
						}
					});
				objectPane.add(fcLabel);
				layout.putConstraint(SpringLayout.NORTH, fcLabel, 11, SpringLayout.SOUTH, label002);
				layout.putConstraint(SpringLayout.WEST, fcLabel, 5, SpringLayout.EAST, fcButton);
				objectPane.add(fcButton);
				layout.putConstraint(SpringLayout.NORTH, fcButton, 7, SpringLayout.SOUTH, label002);
				layout.putConstraint(SpringLayout.WEST, fcButton, 5, SpringLayout.WEST, objectPane);
					
					JLabel label1 = new JLabel("Vertices: ");
					JTextField tf1 = new JTextField();
					tf1.setColumns(5);
					tf1.setText(tf1S);
					tf1.setBackground(windowColor);
					tf1.setBorder(new LineBorder(Color.BLACK));
					JLabel label2 = new JLabel("EdgeWidth: ");
					JTextField tf2 = new JTextField();
					tf2.setColumns(5);
					tf2.setText(tf2S);
					tf2.setBackground(windowColor);
					tf2.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label1);
				layout.putConstraint(SpringLayout.NORTH, label1, 5, SpringLayout.SOUTH, fcButton);
				layout.putConstraint(SpringLayout.WEST, label1, 6, SpringLayout.WEST, objectPane);
				objectPane.add(tf1);
				layout.putConstraint(SpringLayout.NORTH, tf1, 5, SpringLayout.SOUTH, fcButton);
				layout.putConstraint(SpringLayout.WEST, tf1, 5, SpringLayout.EAST, label1);
				objectPane.add(label2);
				layout.putConstraint(SpringLayout.NORTH, label2, 5, SpringLayout.SOUTH, fcButton);
				layout.putConstraint(SpringLayout.WEST, label2, 5, SpringLayout.EAST, tf1);
				objectPane.add(tf2);
				layout.putConstraint(SpringLayout.NORTH, tf2, 5, SpringLayout.SOUTH, fcButton);
				layout.putConstraint(SpringLayout.WEST, tf2, 5, SpringLayout.EAST, label2);
				
					JLabel label3 = new JLabel("Xpos: ");
					JTextField tf3 = new JTextField();
					tf3.setColumns(5);
					tf3.setText(tf3S);
					tf3.setBackground(windowColor);
					tf3.setBorder(new LineBorder(Color.BLACK));
					JLabel label4 = new JLabel("Ypos: ");
					JTextField tf4 = new JTextField();
					tf4.setColumns(5);
					tf4.setText(tf4S);
					tf4.setBackground(windowColor);
					tf4.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label3);
				layout.putConstraint(SpringLayout.NORTH, label3, 5, SpringLayout.SOUTH, label1);
				layout.putConstraint(SpringLayout.WEST, label3, 25, SpringLayout.WEST, objectPane);
				objectPane.add(tf3);
				layout.putConstraint(SpringLayout.NORTH, tf3, 5, SpringLayout.SOUTH, label1);
				layout.putConstraint(SpringLayout.WEST, tf3, 5, SpringLayout.EAST, label3);
				objectPane.add(label4);
				layout.putConstraint(SpringLayout.NORTH, label4, 5, SpringLayout.SOUTH, label1);
				layout.putConstraint(SpringLayout.WEST, label4, 38, SpringLayout.EAST, tf3);
				objectPane.add(tf4);
				layout.putConstraint(SpringLayout.NORTH, tf4, 5, SpringLayout.SOUTH, label1);
				layout.putConstraint(SpringLayout.WEST, tf4, 5, SpringLayout.EAST, label4);
					
					JLabel label5 = new JLabel("Mass: ");
					JTextField tf5 = new JTextField();
					tf5.setColumns(5);
					tf5.setText(tf5S);
					tf5.setBackground(windowColor);
					tf5.setBorder(new LineBorder(Color.BLACK));
					JCheckBox cb = new JCheckBox("Gravity");
					cb.setBackground(windowColor);
					cb.setSelected(true);
					cb.setFocusPainted(false);
				objectPane.add(label5);
				layout.putConstraint(SpringLayout.NORTH, label5, 5, SpringLayout.SOUTH, label3);
				layout.putConstraint(SpringLayout.WEST, label5, 23, SpringLayout.WEST, objectPane);
				objectPane.add(tf5);
				layout.putConstraint(SpringLayout.NORTH, tf5, 5, SpringLayout.SOUTH, label3);
				layout.putConstraint(SpringLayout.WEST, tf5, 5, SpringLayout.EAST, label5);
				objectPane.add(cb);
				layout.putConstraint(SpringLayout.NORTH, cb, 2, SpringLayout.SOUTH, label3);
				layout.putConstraint(SpringLayout.WEST, cb, 26, SpringLayout.EAST, tf5);
				
					JLabel label6 = new JLabel("MSpeed: ");
					JTextField tf6 = new JTextField();
					tf6.setColumns(5);
					tf6.setText(tf6S);
					tf6.setBackground(windowColor);
					tf6.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label6);
				layout.putConstraint(SpringLayout.NORTH, label6, 5, SpringLayout.SOUTH, label5);
				layout.putConstraint(SpringLayout.WEST, label6, 8, SpringLayout.WEST, objectPane);
				objectPane.add(tf6);
				layout.putConstraint(SpringLayout.NORTH, tf6, 5, SpringLayout.SOUTH, label5);
				layout.putConstraint(SpringLayout.WEST, tf6, 5, SpringLayout.EAST, label6);
					
					JLabel label7 = new JLabel("Iterations: ");
					JTextField tf7 = new JTextField();
					tf7.setColumns(5);
					tf7.setText(tf7S);
					tf7.setBackground(windowColor);
					tf7.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label7);
				layout.putConstraint(SpringLayout.NORTH, label7, 5, SpringLayout.SOUTH, label6);
				layout.putConstraint(SpringLayout.WEST, label7, 1, SpringLayout.WEST, objectPane);
				objectPane.add(tf7);
				layout.putConstraint(SpringLayout.NORTH, tf7, 5, SpringLayout.SOUTH, label6);
				layout.putConstraint(SpringLayout.WEST, tf7, 4, SpringLayout.EAST, label7);
				
					JLabel label8 = new JLabel("EdgeStiff: ");
					JTextField tf8 = new JTextField();
					tf8.setColumns(5);
					tf8.setText(tf8S);
					tf8.setBackground(windowColor);
					tf8.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label8);
				layout.putConstraint(SpringLayout.NORTH, label8, 5, SpringLayout.SOUTH, label5);
				layout.putConstraint(SpringLayout.WEST, label8, 15, SpringLayout.EAST, tf6);
				objectPane.add(tf8);
				layout.putConstraint(SpringLayout.NORTH, tf8, 5, SpringLayout.SOUTH, label5);
				layout.putConstraint(SpringLayout.WEST, tf8, 5, SpringLayout.EAST, label8);
				
					JLabel label9 = new JLabel("ConStiff: ");
					JTextField tf9 = new JTextField();
					tf9.setColumns(5);
					tf9.setText(tf9S);
					tf9.setBackground(windowColor);
					tf9.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label9);
				layout.putConstraint(SpringLayout.NORTH, label9, 5, SpringLayout.SOUTH, label6);
				layout.putConstraint(SpringLayout.WEST, label9, 21, SpringLayout.EAST, tf6);
				objectPane.add(tf9);
				layout.putConstraint(SpringLayout.NORTH, tf9, 5, SpringLayout.SOUTH, label6);
				layout.putConstraint(SpringLayout.WEST, tf9, 5, SpringLayout.EAST, label9);
				
					JLabel label10 = new JLabel("Viscosity: ");
					JTextField tf10 = new JTextField();
					tf10.setColumns(5);
					tf10.setText(tf10S);
					tf10.setBackground(windowColor);
					tf10.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label10);
				layout.putConstraint(SpringLayout.NORTH, label10, 5, SpringLayout.SOUTH, label9);
				layout.putConstraint(SpringLayout.WEST, label10, 2, SpringLayout.WEST, objectPane);
				objectPane.add(tf10);
				layout.putConstraint(SpringLayout.NORTH, tf10, 5, SpringLayout.SOUTH, label9);
				layout.putConstraint(SpringLayout.WEST, tf10, 5, SpringLayout.EAST, label10);
				
					JLabel label11 = new JLabel("StiffPower: ");
					JTextField tf11 = new JTextField();
					tf11.setColumns(5);
					tf11.setText(tf11S);
					tf11.setBackground(windowColor);
					tf11.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label11);
				layout.putConstraint(SpringLayout.NORTH, label11, 5, SpringLayout.SOUTH, label9);
				layout.putConstraint(SpringLayout.WEST, label11, 6, SpringLayout.EAST, tf6);
				objectPane.add(tf11);
				layout.putConstraint(SpringLayout.NORTH, tf11, 5, SpringLayout.SOUTH, label9);
				layout.putConstraint(SpringLayout.WEST, tf11, 5, SpringLayout.EAST, label11);
				
					JLabel label12 = new JLabel("GFriction: ");
					JTextField tf12 = new JTextField();
					tf12.setColumns(5);
					tf12.setText(tf12S);
					tf12.setBackground(windowColor);
					tf12.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label12);
				layout.putConstraint(SpringLayout.NORTH, label12, 5, SpringLayout.SOUTH, label10);
				layout.putConstraint(SpringLayout.WEST, label12, 4, SpringLayout.WEST, objectPane);
				objectPane.add(tf12);
				layout.putConstraint(SpringLayout.NORTH, tf12, 5, SpringLayout.SOUTH, label10);
				layout.putConstraint(SpringLayout.WEST, tf12, 5, SpringLayout.EAST, label12);
				
					JLabel label13 = new JLabel("ColFriction: ");
					JTextField tf13 = new JTextField();
					tf13.setColumns(5);
					tf13.setText(tf13S);
					tf13.setBackground(windowColor);
					tf13.setBorder(new LineBorder(Color.BLACK));
				objectPane.add(label13);
				layout.putConstraint(SpringLayout.NORTH, label13, 5, SpringLayout.SOUTH, label10);
				layout.putConstraint(SpringLayout.WEST, label13, 6, SpringLayout.EAST, tf12);
				objectPane.add(tf13);
				layout.putConstraint(SpringLayout.NORTH, tf13, 5, SpringLayout.SOUTH, label10);
				layout.putConstraint(SpringLayout.WEST, tf13, 5, SpringLayout.EAST, label13);
			/*
			 * 	
			 */
				
			JPanel bottomPane = new JPanel();
			bottomPane.setBackground(windowColor);
				JButton addOK = new JButton("Add Object");
				addOK.setBackground(windowColor);
				addOK.setFocusPainted(false);
				addOK.addActionListener((ActionEvent event) -> {
					Object box = new Object((Float.parseFloat(tf5.getText().replaceAll("\\s",""))));
					
					if(file != null)
					box.addImage(file.getPath(), Integer.parseInt(tf00.getText().replaceAll("\\s","")), Integer.parseInt(tf0.getText().replaceAll("\\s","")), Integer.parseInt(tf002.getText().replaceAll("\\s","")), Integer.parseInt(tf02.getText().replaceAll("\\s","")));
					
					VertexEdgeObject ve = GeometryGenerator.genGeometry((Integer.parseInt(tf1.getText().replaceAll("\\s",""))), Float.parseFloat(tf2.getText().replaceAll("\\s","")), Float.parseFloat(tf3.getText().replaceAll("\\s","")), Float.parseFloat(tf4.getText().replaceAll("\\s","")));
					
					for(int k=0; k< ve.vertexCount; k++) {
						box.addVertex(ve.vertices[k]);
					}
					for(int k=0; k< ve.edgeCount; k++) {
						box.addEdge(ve.edges[k]);
					}
					for(int k=0; k< ve.constraintCount; k++) {
						box.addConstraint(ve.constraints[k]);
					}
					
					for(int i=0; i < box.edgeCount; i++) {
						box.edges[i].stiffness = Float.parseFloat(tf8.getText().replaceAll("\\s",""));
						box.edges[i].stiffnessPower = Float.parseFloat(tf11.getText());
					}
					for(int i=0; i < box.constraintCount; i++) {
						box.constraints[i].stiffness = Float.parseFloat(tf9.getText().replaceAll("\\s",""));
						box.constraints[i].stiffnessPower = Float.parseFloat(tf11.getText());
					}
					
					if(cb.isSelected()) {
						box.addForce(Object.gravity);
					}
					box.mSpeed = Float.parseFloat(tf6.getText());
					box.viscosity = Float.parseFloat(tf10.getText().replaceAll("\\s",""));
					box.groundFriction = Float.parseFloat(tf12.getText().replaceAll("\\s",""));
					box.collisionFriction = Float.parseFloat(tf13.getText().replaceAll("\\s",""));
					box.iterations = Integer.parseInt(tf7.getText());
					
					Main.entities.addObject(box);
					
					/*
					 * Save Inputs
					 */
					if(file != null) {
						fcLabelS = file.getPath();
					}
					
					tf00S = tf00.getText();
					tf0S = tf0.getText();
					tf002S = tf002.getText();
					tf02S = tf02.getText();
					tf1S = tf1.getText();
					tf2S = tf2.getText();
					tf3S = tf3.getText();
					tf4S = tf4.getText();
					tf5S = tf5.getText();
					tf6S = tf6.getText();
					tf7S = tf7.getText();
					tf8S = tf8.getText();
					tf9S = tf9.getText();
					tf10S = tf10.getText();
					tf11S = tf11.getText();
					tf12S = tf12.getText();
					tf13S = tf13.getText();
					/*
					 * 
					 */
				});
				
				JButton cancel = new JButton("OK");
				cancel.setBackground(windowColor);
				cancel.setFocusPainted(false);
				cancel.addActionListener((ActionEvent event) -> {
					frame.dispose();
				});
			bottomPane.add(addOK);
			bottomPane.add(cancel);
			
		/*
		 * ******************************************************
		 */
		
		frame.add(northPane, BorderLayout.NORTH);
		frame.add(objectPane, BorderLayout.CENTER);
		frame.add(bottomPane, BorderLayout.PAGE_END);
		frame.setVisible(true);
	}
}
