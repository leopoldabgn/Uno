package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Player;

public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private BoardGame boardGame;
	
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
		
		setHomeView();

		this.setVisible(true);
	}

	public void setHomeView() {
		this.getContentPane().removeAll();

		HomeView home = new HomeView(this);
		this.getContentPane().add(home);
		
		revalidate();
		repaint();
	}

	public void setBoardGame() {
		this.getContentPane().removeAll();
		JPanel pan = new JPanel();
		pan.setOpaque(false);
		boardGame = new BoardGame(this);
		pan.add(boardGame);

		this.getContentPane().add(pan);

		revalidate();
		repaint();
	}

	public void setRankingView(Player[] players) {
		this.getContentPane().removeAll();
		JPanel pan = new JPanel();
		pan.add(new RankingView(players));
		setupPan(pan);
		this.getContentPane().add(pan);
		revalidate();
		repaint();
	}

    public void setupPan(JPanel pan) {
		pan.setOpaque(false);
        pan.setLayout(new GridLayout());
    	Dimension screen = this.getSize();
        int w = 1920, h = 1010;
        int top = ((int)screen.getHeight() * 170) / h;
        int left = ((int)screen.getWidth() * 500) / w;
        pan.setBorder(new EmptyBorder(top, left, top, left));
	}

}
