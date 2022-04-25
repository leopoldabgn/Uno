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
	public static String RESOURCES_FOLDER = "src/resources/";

	private AI[] bots;
	private Player player;
	private JButton validate = new JButton("GO");
	private Deck gameDeck, binDeck;
	private JLabel playTurn = new JLabel("Player : 0");
	public static Color BACKGROUND_COLOR = new Color(176, 255, 233);
	public static int WIDTH = 1000, HEIGHT = 600, PLAY_TURN = 0;
	private int sens = 1, cardsToAdd = 0, skipPlayers = 0;

	public BoardGame()
	{
		super();
		this.setLayout(null);
		
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(BACKGROUND_COLOR);
		
		gameDeck = new Deck(this, 0);
		
		player = new Player(this);
		
		bots = new AI[3];
		
		bots[0] = new AI(this, 'L');
		bots[1] = new AI(this, 'U');
		bots[2] = new AI(this, 'R');
		
		binDeck = new Deck(this, 1);
		
		binDeck.addCard(Deck.getSomeCards(1, gameDeck, true)[0]);
		
		validate.setBounds(WIDTH/2 - 30, HEIGHT/2 - 30, 60, 60);
		playTurn.setBounds(120, 0, 100, 100);
		
		this.add(player.getDeck());
		for(int i=0;i<3;i++)
			this.add(bots[i].getDeck());
		this.add(gameDeck);
		this.add(binDeck);
		this.add(validate);
		this.add(playTurn);
		
		//BoardGame self = this;
		
		validate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dropToBin();
				if(PLAY_TURN > 0 && bots[PLAY_TURN-1].getDeck().getLength() == 0) {
					bots[PLAY_TURN-1].setFinish(true);
					changeTurn();
				}
				changeTurn();
				if(PLAY_TURN != 0)
				{
					//(new ThreadTest(bots, self)).run();
					/*
					for(int i=0;i<3;i++)
					{
						if(!bots[i].play())
						{
							dropCard();
							bots[i].play();
						}
						
						dropToBin();
						changeTurn();
					}*/
						
					if(bots[PLAY_TURN-1].getDeck().getLength() == 0) {
						bots[PLAY_TURN-1].setFinish(true);
						return;
					}

					if(!bots[PLAY_TURN-1].play())
					{
						dropCard();
						bots[PLAY_TURN-1].play();
					}
				}
				else
				{
					if(player.getDeck().getLength() == 0)
					{
						player.setFinish(true);
						changeTurn();
					}
				}
					
				
			}
		});
		
	}
	
	public void dropCard()
	{
		Card card = gameDeck.getAndDelLastCard();
		if(card != null)
		{
			if(PLAY_TURN == 0)
			{
				player.getDeck().addCard(card);
				player.getDeck().setLastLength(999);
			}
			else
			{
				card.setCardVisible(bots[PLAY_TURN-1].getDeck().
								getFirstCard().isCardVisible());
				bots[PLAY_TURN-1].getDeck().addCard(card);
			}
		}
	}
	
	public void dropToBin()
	{
		Deck deck;
		if(PLAY_TURN == 0)
			deck = player.getDeck();
		else
			deck = bots[PLAY_TURN-1].getDeck();
		
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
			cardsToAdd = cards.length > 1 ? 
					 	 cardsToAdd+cards.length*2:cardsToAdd+2;
			if(PLAY_TURN == 3)
			{
				
			}
			else
			{
				if(checkTypes(bots[PLAY_TURN-1].getDeck().getCards(),"+2"))
				{
					
				}
				else
				{
					for(int i=0;i<cardsToAdd;i++)
						dropCard();
				}
			}
		}
	}
	
	
	
	public boolean checkCards(Card[] cards, Card binCard)
	{
		if(cards.length == 1)
			return checkCards(cards[0], binCard);
		else if(cards.length >= 2 && !checkTypes(cards))
			return false;
		Card temp = cards[0];
		switch(temp.getType())
		{
		case "NUMBER":
				if(compareNumbers(cards) && (temp.getNb() == binCard.getNb()
				|| checkColor(cards, binCard.getColor())))
					return true;
			break;
		case "+4":
			break;
		case "forbidden":
		case "sens":
		default:
			if(checkColor(cards, binCard.getColor()) ||
			   temp.getType() == binCard.getType())
				return true;
			break;
		}
			
		return false;
	}
	
	public boolean checkCards(Card card, Card binCard)
	{
		switch(card.getType())
		{
		case "NUMBER":
			if(card.getNb() == binCard.getNb()
			|| card.getColor() == binCard.getColor())
				return true;
			break;
		case "+4":
			break;
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
		if(PLAY_TURN == 0)
		{
			if(force || player.getDeck().getLastLength() != player.getDeck().getLength()
			   || player.hasFinished())
			{
				if(sens == 1)
					PLAY_TURN += sens;
				else
					PLAY_TURN = 3;
				if(!force)
					player.getDeck().refreshLastLength();
			}
		}
		else if(PLAY_TURN < 3)
		{

			PLAY_TURN += sens;
		}
		else
		{
			if(sens == 1)
				PLAY_TURN = 0;
			else
				PLAY_TURN -= 1;
		}


		if((PLAY_TURN == 0 && player.hasFinished()) || (PLAY_TURN > 0 && bots[PLAY_TURN-1].hasFinished())) {
			changeTurn(true);
		}

		if(skipPlayers > 0)
		{
			skipPlayers--;
			changeTurn(true);
			return;
		}

		playTurn.setText("Player : "+PLAY_TURN);
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
