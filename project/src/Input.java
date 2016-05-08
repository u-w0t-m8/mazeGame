import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * Listens and interprets the input made by the user
 * from both the main menu and the game itself
 * 
 */
public class Input implements KeyListener {

	private Engine engine;
	
	public Input(Engine engine){
		this.engine = engine;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_UP){
			engine.pressUp();
		}
		else if(keyCode == KeyEvent.VK_DOWN){
			engine.pressDown();
		}
		else if(keyCode == KeyEvent.VK_LEFT){
			engine.pressLeft();
		}
		else if(keyCode == KeyEvent.VK_RIGHT){
			engine.pressRight();
		}
		else if(keyCode == KeyEvent.VK_ENTER){
			engine.pressEnter();
		}
		else if(keyCode == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	

    
}
