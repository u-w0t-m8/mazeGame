import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * Listens and interprets the input made by the user
 * from both the main menu and the game itself
 * 
 */
public class Input implements KeyListener {

	public Input(){
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		//System.exit(0);			
		//if( (keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) )
		// If user pressed any of the keys program terminates
		if(keyCode == KeyEvent.VK_SPACE)
		{
				
		}
		// TODO Auto-generated method stub
		
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
