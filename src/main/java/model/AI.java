package model;

import java.util.ArrayList;
import java.util.List;

import view.BoardGame;
import view.Card;

public class AI extends Player
{
	public static int sleepTime = 1000;

	public AI(BoardGame board, char position)
	{
		super(board, position, false);
		this.name = "AI "+(nextNb-1);
	}
	
	public boolean play()
	{
		List<Card> list = new ArrayList<>();
		Card[] cards = this.getDeck().getCards();
		Card binCard = this.getBoard().getBinDeck().getFirstCard();
		for(int i=0;i<cards.length;i++)
		{
			if(this.getBoard().checkCards(cards[i], binCard))
			{
				List<Card> list2 = getStackCards(i, cards);
				if(list.size() < list2.size())
					list = list2;
			}
		}
		
		if(list.size() > 0)
		{
			for(int i=0;i<list.size();i++)
			{
				list.get(i).setUp(true);
				//this.getBoard().dropToBin();
			}
		}
		else
			return false;
		
		this.getDeck().setupDeck();
		
		return true;
	}

	public static List<Card> getStackCards(int cardIndex, Card[] cards)
	{
		List<Card> list = new ArrayList<>();
		list.add(cards[cardIndex]);
		
		for(int j=0;j<cards.length;j++)
		{
			if(BoardGame.canStackCards(cards[cardIndex], cards[j]))
				if(cardIndex != j)
					list.add(cards[j]);
		}	
		
		
		return list;
	}

}
