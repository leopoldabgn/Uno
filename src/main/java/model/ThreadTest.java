package model;

import view.BoardGame;

public class ThreadTest extends Thread {
	
	private AI[] bots;
	private BoardGame board;
	
	public ThreadTest(AI[] tab, BoardGame board)
	{
		this.board = board;
		this.bots = tab;
	}
	
	public void run() {
		for(int i=0;i<3;i++)
		{
			if(!bots[i].play())
			{
				board.dropCard();
				bots[i].play();
			}
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			board.revalidate();
			board.repaint();
			board.dropToBin();
			if(bots[i].getDeck().getLength() == 0)
				bots[i].setFinish(true);
			board.changeTurn();
			board.penalityCards();
		}
	}

}