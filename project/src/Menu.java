import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Listens for
 *
 */
public class Menu {

	//private BufferedImage titleImage = ImageIO.read(new File("/imgs/wobcke.jpg"));
	
	private List<String> items;
	private int selected = 0;
	private int width = 800; 	// hard coded width & height needs to be replaced by 
	private int height = 600; 	// something which passes in the resolution size
	private JFrame menuFrame;

	private Border border;
	
	
	/**
	 * Constructor for menu
	 * NOTE: things we might need for a menu
	 * we may need to pass in some sort of input so we can "click the start button"
	 * may need to grab arguments from input class
	 */
	public Menu() {	
		
		initialiseMenuFrame();

	}
	
	/**
	 * Renders the menu frame
	 * 
	 * 
	 */
	public void initialiseMenuFrame() {

		//Creates a new window 800 x 600]
		menuFrame = new JFrame("4W3$oM3 Maze Game YEAAA pls buy DLC");
        menuFrame.setSize(800, 600);
        menuFrame.setLocationRelativeTo(null);
		menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel title = new JPanel();
		title.setLayout(new BorderLayout());
		title.setBorder(border);
		title.setPreferredSize(new Dimension(width, height/2));
		title.setBorder(BorderFactory.createLineBorder(Color.red));

		
		menuFrame.add(title);
		menuFrame.setVisible(true);
	}
	
	
	/**
	 * Select specific button
	 */
	public void selector(){
		
		
	}
	
	public void addItems(String... text){
		for (String s: text){
			items.add(s);
		}
	}
	
	public String getSelectedItem(){
		return items.get(selected);
	}
	
	public String getItem(int index){
		return items.get(index);
	}
	
	public void up(){
		selected--;
		if (selected < 0){
			selected = items.size()-1;
		}
	}
	
	public void down(){
		if (selected >= items.size()){
			selected = 0;
		}
	}

}
