package raycast;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.*;

class CDistance{
	public float m_fDistance = 0;
	public boolean isACorner = false;
}

public class Map extends Renderer implements KeyListener {
	private float fPlayerX = 49, fPlayerY = 40;
	private float fPlayerW = 0.2f, fPlayerH = 0.2f;
	private final float fSpeed = 0.05f;
	private float fAngle = 0.0f, fDepth = 6.5f;
	private float fDirX = (float) Math.cos(fAngle), fDirY = (float) Math.sin(fAngle);
	private static final float fFov = 60.0f;
	private int rayCount = 60;
	private String mapData;
	private final static int tileR = 20, tileC = 20;
	private static int tileW, tileH;
	private float fMaxDistance;
	private int chX, chY;
	private boolean isInMapView = false;
	
	Map(int width, int height) {
		super(width, height);
		
		mapData = "####################"
				+ "#                  #"
				+ "#                  #"
				+ "#                  #"
				+ "#                  #"
				+ "#     ########     #"
				+ "#         #        #"
				+ "#         #        #"
				+ "#         #        #"
				+ "#                  #"
				+ "####               #"
				+ "#                  #"
				+ "#                  #"
				+ "#                  #"
				+ "#        ######    #"
				+ "#                  #"
				+ "#          ###     #"
				+ "#                  #"
				+ "#                  #"
				+ "####################";
		
		
		this.width = width;
		this.height = height;
		tileW = this.width / tileR;
		tileH = this.height / tileC;
		
		fPlayerX = width / 2;
		fPlayerY = height / 2;
		
		double fRadians = Math.toRadians(fAngle);
		fDirX = (float) Math.toDegrees(Math.cos(fRadians));
		fDirY = (float) Math.toDegrees(Math.sin(fRadians));
		
		float mX = (fDirX * fDepth + fPlayerX) / tileW;
		float mY = (fDirY * fDepth + fPlayerY) / tileH;
		float bX = fPlayerX / tileW;
		float bY = fPlayerY / tileH;
		
		fMaxDistance = (float) Math.sqrt(Math.pow(mX - bX, 2) + Math.pow(mY - bY, 2));
		setVisible(true);
		setPreferredSize(new Dimension(this.width, this.height));
		setDoubleBuffered(true);
		this.setBackground(new Color(255,255,0));
		setLayout(null);
		
	}
	
	private CDistance getfDistance(float angle) {
		
		double fRadians = Math.toRadians(angle);
		float fdirX = (float) Math.toDegrees(Math.cos(fRadians));
		float fdirY = (float) Math.toDegrees(Math.sin(fRadians));
		
		float x1 = fPlayerX, y1 = fPlayerY;
		float x2 = fdirX * fDepth + fPlayerX, y2 = fdirY * fDepth + fPlayerY;
		float fDifX = x2 - x1, fDifY = y2 - y1;
		
		int stepCount;
		
		if(Math.abs(fDifX) > Math.abs(fDifY)) {
			stepCount = (int)Math.abs(fDifX);
		}
		else {
			stepCount = (int)Math.abs(fDifY);
		}
		
		float RayX = fPlayerX, RayY = fPlayerY;
		CDistance distance = new CDistance();
		distance.m_fDistance = fMaxDistance;
		float fX = 0.0f, fY = 0.0f;
		float fValueX = fDifX / stepCount, fValueY = fDifY / stepCount;
		
		for(int i = 0; i < stepCount; i++) {
			
			RayX += fValueX;
			RayY += fValueY;
			
			int x = (int)(RayX / tileW), y = (int)(RayY / tileH);
			
			if(mapData.charAt(x + y * tileR) == '#') {
				
				fX = RayX;
				fY = RayY;
				chX = x;
				chY = y;
				x1 = fPlayerX / tileW;
				y1 = fPlayerY / tileH;
				x2 = RayX / tileW;
				y2 = RayY / tileH;
				
				float rX = (float)(RayX / tileW) - x, rY = ((float)(RayY / tileH)) - y;
				
				if(rY < 0.1 || rX < 0.1)distance.isACorner = true;
				
				distance.m_fDistance = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
				
				
				break;
			}
			
		}
		
		if(fX <= 0 || fY <= 0) {
			fX = RayX;
			fY = RayY;
		}
		
		
		if(isInMapView)
			render.drawLine((int)(fPlayerX + (tileW * fPlayerW) / 2), (int)(fPlayerY + (tileH * fPlayerH) / 2), (int)(fX), (int)(fY));

		
		
		
		return distance;
		
	}
	
	private void castRays() {
		float inc = fFov / rayCount;
		
		
		render.setColor(Color.MAGENTA);
		float fStartAngle = (fAngle - (fFov / 2));
		
		for(int i = 1; i <= rayCount; i++) {
			
			getfDistance(fStartAngle + (inc * i));
		}
		render.setColor(Color.BLUE);
		
		
		double fRadians = Math.toRadians(fAngle);
		fDirX = (float) Math.toDegrees(Math.cos(fRadians));
		fDirY = (float) Math.toDegrees(Math.sin(fRadians));
		render.drawLine((int)(fPlayerX + (tileW * fPlayerW) / 2), (int)(fPlayerY + (tileH * fPlayerH) / 2), (int)(fDirX * fDepth + fPlayerX), (int)(fDirY * fDepth + fPlayerY));
		
	}
	
	
	
	
	
	private void renderTiles() {
		
		for(int col = 0; col < tileC; col++) {
			for(int row = 0; row < tileR; row++) {
				char c = mapData.charAt( row + col * tileR);
				if(c != ' ') {
					
					render.setColor(Color.red);
					render.fillRect(row * tileW, col * tileH,tileW, tileH);
					
				}
				
					
				render.setColor(Color.green);
				render.drawRect(row * tileW, col * tileH,tileW, tileH);
				
				if(row == chX && col == chY) {
					render.setColor(Color.white);
					render.fillRect(row * tileW, col * tileH,tileW, tileH);
				}
			}
		}
	}
	
	private void firstPersonView() {
		float inc = fFov / rayCount;
		float fColumnW = width / rayCount;
		
		float fShapeMaxH = height / 2;
		
		
		float fStartAngle = (fAngle - (fFov / 2));
		float h = height;
		
		for(int i = 1; i <= rayCount; i++) {
			float fTempAngle = fStartAngle + inc * i;
			float fFishEye = (float)Math.cos(Math.toRadians(fAngle - fTempAngle));
			CDistance distance = getfDistance(fTempAngle);
			float fDistance = (float) (distance.m_fDistance * fFishEye);
			float fScaledDistance = (fMaxDistance - fDistance) / fMaxDistance;
			
			
			char color = (char)(255 * fScaledDistance);
			if(color < 0)color = 0;
			if(color > 255) color = 255;


			
			float fShapeH = fShapeMaxH * fScaledDistance;
			float fOffSet = fShapeH / 2;
			if(!distance.isACorner)
				render.setColor(new Color(0,0, color));
			else
				render.setColor(Color.black);
			render.fillRect((int) ((i) * fColumnW), (int)(height/2 - fOffSet), (int)fColumnW, (int) (fShapeH) );
			
			
		}
		
		
		
		double fRadians = Math.toRadians(fAngle);
		fDirX = (float) Math.toDegrees(Math.cos(fRadians));
		fDirY = (float) Math.toDegrees(Math.sin(fRadians));
		
		
	}
	
	
	
	public void update() {
		
		if(isInMapView) {
			render.setColor(getBackground());
			render.fillRect(0, 0, width, height);
			renderTiles();
			
			render.setColor(Color.BLUE);
			render.fillRect((int)fPlayerX, (int)fPlayerY, (int)(tileW * fPlayerW), (int)(tileH * fPlayerH));
			
			
			castRays();
			
		}
		else {
			render.setColor(Color.gray);
			render.fillRect(0, 0, width, height);
			firstPersonView();
		}
		
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(e.getExtendedKeyCode()) {
		case KeyEvent.VK_W:
			fPlayerY += fSpeed * fDirY;	
			fPlayerX += fSpeed * fDirX;	
			break;
		case KeyEvent.VK_S:
			fPlayerY -= fSpeed * fDirY;	
			fPlayerX -= fSpeed * fDirX;	
			break;
		case KeyEvent.VK_A:
			fPlayerX -= fSpeed * 2;	
			break;
		case KeyEvent.VK_D:
			fPlayerX += fSpeed * 2;	
			break;
		case KeyEvent.VK_UP:
			fAngle -= 1.5f;
			//if(fAngle < 0)fAngle = 0;
			break;
		case KeyEvent.VK_DOWN:
			fAngle += 1.5f;
			//if(fAngle > 360)fAngle = 360;
			break;
		case KeyEvent.VK_SPACE:
			isInMapView = !isInMapView;
			break;
		}
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
