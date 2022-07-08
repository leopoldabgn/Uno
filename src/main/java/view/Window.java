package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Player;

public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private MainPanel mainPan;
	//private BoardGame boardGame;

	public Window(int w, int h)
	{
		super();
		this.setMinimumSize(new Dimension(w, h));
		this.setTitle("Uno");
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(w, h));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPan = new MainPanel();
		mainPan.setLayout(getContentPane().getLayout());
		setContentPane(mainPan);
		
		setHomeView();

		this.setVisible(true);
	}

	public void setHomeView() {
		this.getContentPane().removeAll();
		mainPan.changeImage("uno_menu_background.jpg");

		HomeView home = new HomeView(this);
		this.getContentPane().add(home);
		
		revalidate();
		repaint();
	}

	public void setSettingsView() {
		this.getContentPane().removeAll();
		mainPan.changeImage("uno_menu_background.jpg");

		SettingsView settings = new SettingsView(this);
		this.getContentPane().add(settings);
		
		revalidate();
		repaint();
	}

	public void setBoardGame() {
		setBoardGame(new BoardGame(this));
	}

	public void setBoardGame(BoardGame boardGame) {
		this.getContentPane().removeAll();
		mainPan.changeImage("uno_background.jpg");
		JPanel pan = new JPanel();
		pan.setOpaque(false);
		pan.setLayout(new GridBagLayout());
		pan.add(boardGame);

		this.getContentPane().add(pan);

		revalidate();
		repaint();
	}

	public void setRankingView(Player[] players) {
		this.getContentPane().removeAll();
		mainPan.changeImage("uno_menu_background.jpg");
		JPanel pan = new JPanel();
		pan.setOpaque(false);
		pan.add(new RankingView(this, players));
		setupPan(pan);
		this.getContentPane().add(pan);
		revalidate();
		repaint();
	}

    public void setupPan(JPanel pan) {
		pan.setLayout(new GridLayout());
    	Dimension screen = this.getSize();
        int w = 1920, h = 1010;
        int top = ((int)screen.getHeight() * 170) / h;
        int left = ((int)screen.getWidth() * 500) / w;
        pan.setBorder(new EmptyBorder(top, left, top, left));
	}

	public void quit() {
		this.dispose();
		System.exit(0);
	}

	private class MainPanel extends JPanel {

		private Image backImg;

		public MainPanel() {
			backImg = getImage("uno_menu_background.jpg");
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(backImg != null)
				g.drawImage(backImg, 0, 0, getWidth(), getHeight(), null);
		}

		public void changeImage(String name) {
			backImg = getImage(name);
		} 

	}

	public static Image getImage(final String pathAndFileName) {
		/*
		final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
		return Toolkit.getDefaultToolkit().getImage(url);
		*/
		
		//return new ImageIcon("src/main/resources/"+pathAndFileName).getImage();
		Image img = null;
		try {
			img = ImageIO.read(new File("resources/"+pathAndFileName));
		} catch (IOException e) {
			try {
				img = ImageIO.read(new File("src/main/resources/"+pathAndFileName));
			} catch (IOException e1) {}
		}
		
		return img;
	}

}
