package model;

import view.BoardGame;
import view.Card;
import view.Deck;

public class Player 
{
	protected static int nextNb = 1;

	private BoardGame board;
	private Deck deck;
	protected String name = "Player";
	private int rank = -1;
	
	public Player(BoardGame board)
	{
		this.name += " "+(nextNb++);
		this.board = board;
		Card[] deck = Deck.getSomeCards(7, board.getGameDeck(), true);
		this.deck = new Deck(7, deck);
	}
	
	public Player(BoardGame board, char position)
	{
		this.name += " "+(nextNb++);
		this.board = board;
		Card[] deck = Deck.getSomeCards(7, board.getGameDeck(), true);
		this.deck = new Deck(7, deck, position, true);
	}
	
	public Player(BoardGame board, char position, boolean visible)
	{
		this.name += " "+(nextNb++);
		this.board = board;
		Card[] deck = Deck.getSomeCards(7, board.getGameDeck(), visible);
		this.deck = new Deck(7, deck, position, visible);
	}
	
	public boolean canPlay() {
		if(hasFinished())
			return false;
		Card binCard = board.getBinDeck().getFirstCard();
		Card[] cards = deck.getCards();
		if(cards == null || cards.length == 0)
			return false;
		for(Card c : cards)
			if(board.checkCards(c, binCard))
				return true;
		return false;
	}

	public Deck getDeck()
	{
		return this.deck;
	}

	public BoardGame getBoard() 
	{
		return board;
	}

	public boolean hasFinished() 
	{
		return rank != -1;
	}

	public boolean isPlayer()
	{
		return getClass().equals(Player.class);
	}

	public boolean isAI()
	{
		return (this instanceof AI);
	}

	public String getName()
	{
		return name;
	}

	public void setFinish(int rank) {
		setRank(rank);
	}

	public void setRank(int rank)
	{
		this.rank = rank;
	}

	public int getRank()
	{
		return rank;
	}

}
