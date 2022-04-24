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
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				board.dropToBin();
				board.changeTurn();
			}
	  }

	}