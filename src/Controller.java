import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * Created by Abraham Campbell on 15/01/2020.
 *   Copyright (c) 2020  Abraham Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
   
   (MIT LICENSE ) e.g do what you want with this :-) 
 */ 

//Singeton pattern
public class Controller implements KeyListener {
        
	   private static boolean KeyAPressed= false;
	   private static boolean KeyDPressed= false;
	   private static boolean KeySpacePressed= false;

	   private static boolean KeyLeftPressed= false;
	   private static boolean KeyRightPressed= false;
	   private static boolean KeyUpPressed= false;
	   
	   private static final Controller instance = new Controller();
	   
	 public Controller() { 
	}
	 
	 public static Controller getInstance(){
	        return instance;
	    }
	   
	@Override
	// Key pressed , will keep triggering 
	public void keyTyped(KeyEvent e) { 
		 
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{ 
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_A:setKeyAPressed(true);break;
			case KeyEvent.VK_D:setKeyDPressed(true);break;
			case KeyEvent.VK_SPACE:setKeySpacePressed(true);break;
			case KeyEvent.VK_LEFT: setKeyLeftPressed(true); break;
			case KeyEvent.VK_RIGHT: setKeyRightPressed(true); break;
			case KeyEvent.VK_UP: setKeyUpPressed(true); break;
		    default:
		    	//System.out.println("Controller test:  Unknown key pressed");
		        break;
		}  
		
	 // You can implement to keep moving while pressing the key here . 
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{ 
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_A:setKeyAPressed(false);break;
			case KeyEvent.VK_D:setKeyDPressed(false);break;
			case KeyEvent.VK_SPACE:setKeySpacePressed(false);break;
			case KeyEvent.VK_LEFT: setKeyLeftPressed(false); break;
			case KeyEvent.VK_RIGHT: setKeyRightPressed(false); break;
			case KeyEvent.VK_UP: setKeyUpPressed(false); break;
		    default:
		    	//System.out.println("Controller test:  Unknown key pressed");
		        break;
		}  
		 //upper case 
	
	}


	public boolean isKeyAPressed() {
		return KeyAPressed;
	}


	public void setKeyAPressed(boolean keyAPressed) {
		KeyAPressed = keyAPressed;
	}


	public boolean isKeyDPressed() {
		return KeyDPressed;
	}


	public void setKeyDPressed(boolean keyDPressed) {
		KeyDPressed = keyDPressed;
	}

	public boolean isKeySpacePressed() {
		return KeySpacePressed;
	}

	public void setKeySpacePressed(boolean keySpacePressed) {
		KeySpacePressed = keySpacePressed;
	}

	public boolean isKeyLeftPressed() {
		return KeyLeftPressed;
	}

	public void setKeyLeftPressed(boolean keyLeftPressed) {
		KeyLeftPressed = keyLeftPressed;
	}

	public boolean isKeyRightPressed() {
		return KeyRightPressed;
	}

	public void setKeyRightPressed(boolean keyRightPressed) {
		KeyRightPressed = keyRightPressed;
	}

	public boolean isKeyUpPressed() {
		return KeyUpPressed;
	}

	public void setKeyUpPressed(boolean keyUpPressed) {
		KeyUpPressed = keyUpPressed;
	}
	 
}
