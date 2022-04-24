package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Card extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Image img;
	private boolean up = false, visible = true;
	private String type = "NUMBER";
	private char color = 0;
	private int nb = -1, position = 0;
	public static int WIDTH = 85, HEIGHT = 135, COEFF_UP = HEIGHT/3;
	
	public Card(Image img, String type)
	{
		super();
		this.img = img;
		this.type = type;
		setup();
	}
	
	public Card(Image img, String type, char color)
	{
		super();
		this.img = img;
		this.type = type;
		this.color = color;
		setup();
	}
	
	public Card(Image img, char color, int nb)
	{
		super();
		this.img = img;
		this.nb = nb;
		this.color = color;
		setup();
	}
	
	public void setup()
	{
		this.setBounds(0, 0, WIDTH, HEIGHT);
		this.setOpaque(false);
		this.addMouseListener(new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e)
			{
				if(position == 0) // Ce code ne s'execute donc que pour les cartes du joueur. Pas pour les bots.
				{
					if(!up)
					{
						setLocation(getX(), getY()-COEFF_UP);
						up = true;
					}
					else
					{
						setLocation(getX(), getY()+COEFF_UP);
						up = false;
					}
				}
			}
			
			public void mouseEntered(MouseEvent e) {
				repaint();
			}
			
		});
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(img == null)
			return;
		BufferedImage image;
		int w = WIDTH, h = HEIGHT;
		
		if(visible)
			image = getBufferedImage(img, BufferedImage.TRANSLUCENT);
		else
			image = getBufferedImage(getBackCard(), BufferedImage.TRANSLUCENT);
		
		if(position != 0)
		{			
			for(int i=0;i<position;i++)
				image = rotate(image, 90);
			
			if(position % 2 != 0)
			{
				w = HEIGHT;
				h = WIDTH;
				this.setSize(w, h);
			}
		}
		
		g.drawImage(image, 0, 0, w, h, null);
	}
	
	public BufferedImage rotate(BufferedImage image, int deg)
	{
		final double rads = Math.toRadians(deg);
		final double sin = Math.abs(Math.sin(rads));
		final double cos = Math.abs(Math.cos(rads));
		final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
		final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
		final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		final AffineTransform at = new AffineTransform();
		at.translate(w / 2, h / 2);
		at.rotate(rads,0, 0);
		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		rotateOp.filter(image,rotatedImage);
		return rotatedImage;
	}
	
	public BufferedImage getBufferedImage(Image i, int type)
	{
		BufferedImage image = new BufferedImage(i.getWidth(null), i.getHeight(null), type);
		Graphics g2 = image.getGraphics();
		g2.drawImage(i, 0, 0, null);
		g2.dispose();
		
		return image;
	}
	
	public static Image getBackCard()
	{
		return new ImageIcon(BoardGame.RESOURCES_FOLDER+"cards/backCard.png").getImage();
	}
	
	public void setCardVisible(boolean bool)
	{
		this.visible = bool;
	}

	public boolean isCardVisible()
	{
		return visible;
	}
	
	public int getNb() {
		return nb;
	}

	public char getColor() {
		return color;
	}

	public String getType() {
		return type;
	}
	
	public void setUp(boolean bool)
	{
		this.up = bool;
	}
	
	public boolean isUp()
	{
		return up;
	}
	
	public Image getImage()
	{
		return img;
	}

	public int getPosition() 
	{
		return position;
	}

	public void setPosition(int position) 
	{
		if(position > 3)
		{
			switch(position)
			{
			case 'L':
				position = 1;
				break;
			case 'U':
				position = 2;
				break;
			case 'R':
				position = 3;
				break;
			default:
				position = 0;
				break;
			}
		}
		else if(position < 0)
			position = 0;
			
		this.position = position;
	}
	
}
