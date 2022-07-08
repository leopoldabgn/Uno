package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.AI;
import model.Player;

public class BoardGame extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Window window;

	private Player[] players;
	private int nextRank = 1;
	private JButton validate = new JButton("GO");
	private Deck gameDeck, binDeck;
	private JLabel playTurn = new JLabel("");
	public static Color BACKGROUND_COLOR = new Color(176, 255, 233);
	public static int WIDTH = 1000, HEIGHT = 600, PLAY_TURN = 0;
	private int sens = 1, cardsToAdd = 0, skipPlayers = 0;
	private boolean twoMoreCards, fourMoreCards;

	public BoardGame(Window window, Player[] players)
	{
		this(window);
	}

	public BoardGame(Window window)
	{
		setOpaque(false);
		this.window = window;
		this.setLayout(null);
		
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(BACKGROUND_COLOR);
		
		gameDeck = new Deck(this, 0);
/*
		players = new Player[4];
		char[] pos = new char[]{'B', 'L', 'U', 'R'};
		for(int i=0;i<players.length;i++) {
			players[i] = new AI(this, pos[i]);
			this.add(players[i].getDeck());
		}
*/
		binDeck = new Deck(this, 1);
		
		binDeck.addCard(Deck.getSomeCards(1, gameDeck, true)[0]);
		
		validate.setBounds(WIDTH/2 - 30, HEIGHT/2 - 30, 60, 60);
		playTurn.setBounds(120, 0, 100, 100);
		
		this.add(gameDeck);
		this.add(binDeck);
		this.add(validate);
		this.add(playTurn);
		
		//BoardGame self = this;
		
		validate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getActualPlayer().isPlayer())
					dropToBin(); // if(dropToBin())... changeTurn
				
				if(getActualPlayer().isAI())
				{
					//(new ThreadTest(bots, BoardGame.this)).run();
					AI bot = (AI)getActualPlayer();
					if(!bot.play())
					{
						dropCard();
						bot.play();
					}
					try {
						Thread.sleep(AI.sleepTime);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					dropToBin();
				}

				if(getActualPlayer().getDeck().getLength() == 0) {
					getActualPlayer().setFinish(nextRank++);
					if(nextRank == players.length) {
						changeTurn();
						getActualPlayer().setFinish(nextRank);
						window.setRankingView(players);
						return;
					}
				}

				changeTurn();
				penalityCards();
				if(getActualPlayer().isAI()) {
					new Thread(new Runnable() {
						public void run() {
							validate.doClick();
						}
					}).start();
				}
			}
		});
		
	}

	public void setupPlayers(Player[] players) {
		if(players == null)
			return;
		this.players = players;
		for(Player p : players)
			this.add(p.getDeck());
		playTurn.setText(getActualPlayer().getName());
		playTurn.setForeground(Color.WHITE);
	}

	public void dropCard()
	{
		if(getActualPlayer().canPlay())
			return;
		Card card = gameDeck.getAndDelLastCard();
		if(card != null)
		{
			if(getActualPlayer().isPlayer())
			{
				getActualPlayer().getDeck().addCard(card);
				getActualPlayer().getDeck().setLastLength(999);
			}
			else
			{
				card.setCardVisible(getActualPlayer().getDeck().
								getFirstCard().isCardVisible());
				getActualPlayer().getDeck().addCard(card);
			}
		}
	}
	
	public void dropToBin()
	{
		Deck deck = getActualPlayer().getDeck();
		Card binCard = binDeck.getFirstCard();
		Card[] cards = sortCards(deck.getUpCards(), binCard);
		if(cards == null)
			return;
		if(checkCards(cards, binCard))
		{
			applyChangements(cards);
			deck.delUpCards();
			for(Card card : cards)
				binDeck.addCard(card);
		}
	}
	
	public static Card[] sortCards(Card[] cards, Card binCard)
	{
		if(cards == null)
			return null;
		List<Card> list = new ArrayList<>();
		Card[] tab;
		for(int i=0;i<cards.length;i++)
		{
			if(cards[i].getColor() == binCard.getColor())
				list.add(0, cards[i]);
			else
				list.add(cards[i]);
		}

		tab = new Card[list.size()];
		for(int i=0;i<list.size();i++)
			tab[i] = list.get(i);
		return tab;
	}
	
	public void applyChangements(Card[] cards)
	{
		String type = cards[0].getType();
		if(type.equals("sens"))
		{
			if(((cards.length+1)%2)== 0)
				sens *= -1;
		}
		else if(type.equals("forbidden"))
		{
			skipPlayers = cards.length;
		}
		else if(type.equals("+2"))
		{
			cardsToAdd += cards.length*2;
			twoMoreCards = true;
		}
		else if(type.equals("+4"))
		{
			cardsToAdd += cards.length*4;
			fourMoreCards = true;
		}
	}
	
	public boolean checkCards(Card[] cards, Card binCard)
	{
		if(cards.length == 1)
			return checkCards(cards[0], binCard);
		else if(cards.length >= 2 && !checkTypes(cards))
			return false;
		Card temp = cards[0];
		if(twoMoreCards && !temp.getType().equals("+2"))
			return false;
		else if(fourMoreCards && !temp.getType().equals("+4"))
			return false;
		switch(temp.getType())
		{
		case "NUMBER":
				if(compareNumbers(cards) && (temp.getNb() == binCard.getNb()
				|| checkColor(cards, binCard.getColor())))
					return true;
			break;
		case "+4":
			return true;
		case "+2":
		case "forbidden":
		case "sens":
		default:
			return checkColor(cards, binCard.getColor()) || temp.getType() == binCard.getType();
		}
			
		return false;
	}
	
	public boolean checkCards(Card card, Card binCard)
	{
		if(twoMoreCards && !card.getType().equals("+2"))
			return false;
		else if(fourMoreCards && !card.getType().equals("+4"))
			return false;
		switch(card.getType())
		{
		case "NUMBER":
			if(card.getNb() == binCard.getNb()
			|| card.getColor() == binCard.getColor())
				return true;
			break;
		case "+4":
			return true;
		case "colorChanger":
			break;
		case "forbidden":
		case "sens":
		default:
			if(card.getType() == binCard.getType() ||
			   card.getColor() == binCard.getColor())
				return true;
			break;
		}
			
		return false;
	}
	
	public static boolean canStackCards(Card card, Card card2)
	{
		String type = card.getType();
		
		if(type.equals("NUMBER"))
		{
			if(card.getNb() == card2.getNb())
				return true;
		}
		else if(!type.equals("COLOR_CHANGER")) // BLABLABLA
		{
			if(type.equals(card2.getType()))
				return true;
		}
		
		return false;
	}
	
	public static boolean checkTypes(Card[] cards) // Teste si toutes les cartes sont du meme type
	{
		String type = cards[0].getType();
		for(int i=0;i<cards.length;i++)
			if(!cards[i].getType().equals(type))
				return false;
		return true;
	}
	
	public static boolean checkTypes(Card[] cards, String type) //Test si au moins une carte est du meme type que le 2eme arg
	{
		for(int i=0;i<cards.length;i++)
			if(cards[i].getType().equals(type))
				return true;
		return false;
	}
	
	public static boolean checkColor(Card[] cards, char color)
	{
		
		for(int i=0;i<cards.length;i++)
			if(cards[i].getColor() == color)
				return true;
		return false;
	}
	
	public static boolean compareColors(Card[] cards)
	{
		char color = cards[0].getColor();
		for(int i=0;i<cards.length;i++)
			if(cards[i].getColor() != color)
				return false;
		return true;
	}
	
	public static boolean compareNumbers(Card[] cards)
	{
		int nb = cards[0].getNb();
		for(int i=0;i<cards.length;i++)
			if(cards[i].getNb() != nb)
				return false;
		return true;
	}

	public void changeTurn()
	{
		changeTurn(false);
	}

	public void changeTurn(boolean force)
	{
		Player player = getActualPlayer();
		if(player.isPlayer()) {
			if(!force && player.getDeck().getLastLength() == player.getDeck().getLength()
			&& !player.hasFinished())
				return;
		}

		PLAY_TURN += sens;
		PLAY_TURN %= players.length;
		if(PLAY_TURN < 0)
			PLAY_TURN += players.length;
		
		player = getActualPlayer();
		if(player.isPlayer() && !force)
			player.getDeck().refreshLastLength();

		if(player.hasFinished())
			changeTurn(true);

		if(skipPlayers > 0)
		{
			skipPlayers--;
			changeTurn(true);
			return;
		}

		playTurn.setText(getActualPlayer().getName());
	}
	
	public boolean hasCard(Player player, String type) {
		Deck deck = player.getDeck();
		for(Card card : deck.getCards())
			if(card.getType().equals(type))
				return true;
		return false;
	}

	public void penalityCards() {
		if(getActualPlayer().hasFinished())
			return;
		Deck deck = getActualPlayer().getDeck();
		if(twoMoreCards) {
			if(!hasCard(getActualPlayer(), "+2")) {
				for(int i=0;i<cardsToAdd;i++) {
					Card card = gameDeck.getAndDelLastCard();
					if(card == null) // Vider la binDeck(poubelle) dans le gameDeck(pioche) ?
						break;
					if(getActualPlayer() instanceof AI)
						card.setCardVisible(false);
					deck.addCard(card);
				}
				deck.setLastLength(deck.getLength());
				twoMoreCards = false;
				cardsToAdd = 0;
			}
		}
		else if(fourMoreCards) {
			if(!hasCard(getActualPlayer(), "+4")) {
				for(int i=0;i<cardsToAdd;i++) {
					Card card = gameDeck.getAndDelLastCard();
					if(card == null)
						break;
					if(getActualPlayer() instanceof AI)
						card.setCardVisible(false);
					deck.addCard(card);
				}
				fourMoreCards = false;
				cardsToAdd = 0;
				changeTurn(); // Pas le droit de jouer quand on se prend un +4
			}
		}
	}

	public Player getActualPlayer() {
		return players[PLAY_TURN];
	}

	public Deck getBinDeck()
	{
		return binDeck;
	}
	
	public Deck getGameDeck()
	{
		return gameDeck;
	}
	
}
