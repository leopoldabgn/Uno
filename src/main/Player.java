package main;

public class Player 
{
	
	private BoardGame board;
	private Deck deck;
	private boolean finish = false;
	
	public Player(BoardGame board)
	{
		this.board = board;
		Card[] deck = Deck.getSomeCards(7, board.getGameDeck(), true);
		this.deck = new Deck(7, deck);
	}
	
	public Player(BoardGame board, char position)
	{
		this.board = board;
		Card[] deck = Deck.getSomeCards(7, board.getGameDeck(), true);
		this.deck = new Deck(7, deck, position, true);
	}
	
	public Player(BoardGame board, char position, boolean visible)
	{
		this.board = board;
		Card[] deck = Deck.getSomeCards(7, board.getGameDeck(), visible);
		this.deck = new Deck(7, deck, position, visible);
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
		return finish;
	}

	public void setFinish(boolean finish) 
	{
		this.finish = finish;
	}
}
