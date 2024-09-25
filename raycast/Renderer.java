package raycast;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;



public class Renderer extends JPanel implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6215359783656242211L;
	public static int width, height;
	private Image frontBuffer, backBuffer;
	public Graphics render;
	boolean RunProgram = true;
	Thread runningThread;
	Renderer(int width, int height){
		
		
		
		
		this.width = width;
		this.height = height;
		setVisible(true);
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		setLayout(null);
		
		frontBuffer = createImage(width, height);
		backBuffer = createImage(width, height);
		runningThread = new Thread(this);
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(frontBuffer, 0, 0, this);
		g2.dispose();
	}
	
	protected void update() {}
	
	public void startLoop() {
		runningThread.start();
	}

	@Override
	public void run() {
		
		while(RunProgram) {
			
			backBuffer = createImage(width, height);
			if(backBuffer == null)continue;
			render = backBuffer.getGraphics();
			
			update();
			
			Image temp = backBuffer;
			backBuffer = frontBuffer;
			frontBuffer = temp;
			render.dispose();
			repaint();
		}
	}
}
