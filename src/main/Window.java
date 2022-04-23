package main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
		
		JPanel pan = new JPanel();
		pan.setBackground(BoardGame.BACKGROUND_COLOR);
		boardGame = new BoardGame();
		pan.add(boardGame);
		
		
		this.getContentPane().add(pan);
		
		this.setVisible(true);
	}
}
