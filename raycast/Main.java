package raycast;
import java.awt.Dimension;

import javax.swing.JFrame;

import raycast.Renderer;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame myWindow = new JFrame("RayCast");
		
		
		myWindow.setSize(500, 500);
		Map map = new Map(myWindow.getWidth(), myWindow.getHeight());
		myWindow.add(map);
		myWindow.pack();
		myWindow.setResizable(false);
		myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myWindow.addKeyListener(map);
		
		myWindow.setVisible(true);
		map.startLoop();
	}

}
