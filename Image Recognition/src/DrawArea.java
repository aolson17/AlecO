import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class DrawArea extends JComponent{
	
	
	public static BufferedImage imageDrawn;
	private Graphics2D g2;
	private int currentX, currentY, oldX, oldY;
	BufferedImage bufferedImage;
	
	public DrawArea(){
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter(){
			
			public void mousePressed(MouseEvent e){
				oldX = e.getX();
				oldY = e.getY();
			}
			
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			
			public void mouseDragged(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				
				if (g2 != null){
					g2.drawLine(oldX,oldY,currentX,currentY);
					repaint();
					oldX = currentX;
					oldY = currentY;
					
				}
			}
		});
	}
	
	protected void paintComponent(Graphics g){
		if (imageDrawn == null){
			//image = createImage(getSize().width, getSize().height);
			imageDrawn = new BufferedImage(580,495,BufferedImage.TYPE_INT_RGB);
			g2 = (Graphics2D) imageDrawn.getGraphics();
			//enable antialiasing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		
		g.drawImage(imageDrawn, 0, 0, null);
	}
	
	public void clear() {
		g2.setPaint(Color.white);
		g2.fillRect(0, 0, getSize().width, getSize().height);
		g2.setPaint(Color.black);
		repaint();
	}
	
	public void judge() {
		
	}
	
	public void save(String title){
		
		File outputfile = new File("C:\\Users\\aolso\\workspace\\Image Recognition\\Resources\\Taught\\" + title + ".png");
		try {
			ImageIO.write((RenderedImage) imageDrawn, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
