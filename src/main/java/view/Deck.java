package view;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Deck extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private List<Card> cards = new ArrayList<>();
	private char position = 'B';
	private int type = -1;
	private BoardGame board;
	private int lastLength = 0;
	
	public Deck(BoardGame board, int type) // If type == 0 alors c la pioche. Sinon c la poubelle.
	{
		super();
		this.board = board;
		this.type = type;
		this.setBackground(BoardGame.BACKGROUND_COLOR);
		position = 0;
		if(type == 0)
			setupGameDeck();
		else if(type == 1)
			setupBin();
	}
	
	public Deck(int nb, Card[] gameDeck)
	{
		super();
		this.setBackground(BoardGame.BACKGROUND_COLOR);
		cards = toList(gameDeck);
		this.lastLength = cards.size();
		setupDeck();		
	}
	
	public Deck(int nb, Card[] gameDeck, char position, boolean visible)
	{
		super();
		this.setBackground(BoardGame.BACKGROUND_COLOR);
		this.position = position;
		cards = toList(gameDeck);
		this.lastLength = cards.size();
		setupDeck();		
	}

	public void setupGameDeck()
	{
		this.setLayout(null);
		int w = Card.WIDTH;
		int h = Card.HEIGHT;
		cards = toList(scrambleDeck(getDefaultDeck()));
		
		this.setBounds(300, BoardGame.HEIGHT/2 - h/2, w+30, h);
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				board.dropCard();
			}
		});
	}
	
	public void setupBin()
	{
		this.setLayout(null);
		int w = Card.WIDTH;
		int h = Card.HEIGHT;
		
		this.setBounds(BoardGame.WIDTH/2 + 30 + 50, BoardGame.HEIGHT/2 - h/2, w+60, h);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int x = 0;
		if(type == 0)
		{
			for(int i=0;i<3;i++)
			{
				g.drawImage(Card.getBackCard(), x, 0, Card.WIDTH, Card.HEIGHT, null);
				x += 10;
			}
		}
		else if(type == 1 && cards.size() > 0)
		{
			for(int i=3;i>=0;i--)
			{
				if(cards.size() > i)
				{
					g.drawImage(cards.get(i).getImage(), x, 0, Card.WIDTH, Card.HEIGHT, null);
					x += 20;
				}
			}
			
		}
	}
	
	public void setupDeck()
	{
		this.removeAll();
		for(int i=0;i<cards.size();i++)
		{
			cards.get(i).setPosition(position);
			this.add(cards.get(i));
		}
		refreshDeck();
	}
	
	public void refreshDeck()
	{
		this.setLayout(null);
		int w = Card.WIDTH;
		int h = Card.HEIGHT;
		int x = 0, y = 0, space = 20;
		
		switch(position)
		{
		case 'U':
			this.setBounds(300, 20, getCoeff(cards.size(), 0)*cards.size()+w, h+Card.COEFF_UP);
			for(Card card : cards)
			{
				if(card.isUp())
				{
					card.setLocation(x, y+Card.COEFF_UP);
				}
				else
					card.setLocation(x, 0);
				x += getCoeff(cards.size(), 0);
			}	
			break;
		case 'L':
			this.setBounds(20, h, h+Card.COEFF_UP, getCoeff(cards.size(), 1)*cards.size()+h);
			//this.setBackground(Color.RED);
			for(Card card : cards)
			{
				if(card.isUp())
				{
					card.setLocation(Card.COEFF_UP, y);
				}
				else
					card.setLocation(0, y);
				y += getCoeff(cards.size(), 1);
			}	
			break;
		case 'R':
			this.setBounds(BoardGame.WIDTH-h-Card.COEFF_UP-space, h, h+Card.COEFF_UP, getCoeff(cards.size(), 1)*cards.size()+h);
			for(Card card : cards)
			{
				if(card.isUp())
				{
					card.setLocation(0, y);
				}
				else
					card.setLocation(Card.COEFF_UP, y);
				y += getCoeff(cards.size(), 1);
			}
			break;
		default:
			this.setBounds(300, BoardGame.HEIGHT-h-h/3-2*space, getCoeff(cards.size(), 0)*cards.size()+w, h+h/3);
			for(Card card : cards)
			{
				card.setLocation(x, Card.COEFF_UP);
				x += getCoeff(cards.size(), 0);
			}	
			break;
		}
		repaint();
	}
	
	public static int getCoeff(int n, int pos) // n --> nombre de cartes.
	{
		int w;
		if(pos == 0)
			w = 14*Card.WIDTH/3;
		else
			w = 7*Card.HEIGHT/4;
		int k = Card.WIDTH;
		int temp = n*k;
		while(temp >= w)
		{
			k -= 1;
			temp -= n;
		}
		return k;
	}
	
	public static Card[] getDefaultDeck()
	{
		Card[] cards = new Card[52]; // Pour l'instant, juste 16 cartes.
		String[] colors = new String[] {"red", "yellow", "green", "blue"};
		String[] specials = new String[] {"sens", "forbidden", "+2", "+4", "colorChanger"};
		
		for(int j=0;j<4;j++)
		{
			for(int i=0;i<10;i++)
			{
				cards[j*10+i] = new Card(new ImageIcon(BoardGame.RESOURCES_FOLDER+"cards/"+colors[j]+"_"+i+".png").getImage(), 
									colors[j].charAt(0), i);
			}
		}
		
		for(int j=0;j<4;j++)
		{
			for(int i=0;i<3;i++)
			{
				cards[40+j*3+i] = new Card(new ImageIcon(BoardGame.RESOURCES_FOLDER+"cards/"+colors[j]+"_"+specials[i]+".png").getImage(), 
									specials[i], colors[j].charAt(0));
			}
		}
		
		return cards;
	}
	
	public static Card[] scrambleDeck(Card[] deck)
	{
		List<Card> list = toList(deck);
		int n = 0;
		for(int i=0;list.size()>0;i++)
		{
			n = genAleaNb(list.size());
			deck[i] = list.get(n);
			list.remove(n);
		}
		
		return deck;
	}
	
	public static Card[] getSomeCards(int nb, Deck deck, boolean visible)
	{
		Card[] deckTab = deck.getCards();
		
		if(deckTab == null)
			return null;
		else if(deckTab.length < nb)
			return null;
		
		Card[] cardTab = new Card[nb];
		
		for(int i=0;i<nb;i++)
		{
			cardTab[i] = deckTab[i];
			cardTab[i].setCardVisible(visible);
			deck.delCard(deckTab[i]);
		}
		
		return cardTab;
	}
	
	public static List<Card> genDeck(int nb, Card[] deck, boolean visible)
	{
		List<Card> list = new ArrayList<>();
		int n = 0;
		int k = 0;
		while(k < nb)
		{
			n = genAleaNb(deck.length);
			try {
			if(deck[n] == null)
				continue;
			}catch(Exception e) {
				continue;
			}
			deck[n].setCardVisible(visible);
			list.add(deck[n]);
			deck[n] = null;
			k++;
		}
		return list;
	}
	
	public void delCard(Card card)
	{
		if(cards.contains(card))
			cards.remove(card);
	}
	
	public static int genAleaNb(int n)
	{
		
		return (int)(Math.random() * n);
	}
	
	public int getLength() 
	{
		return cards.size();
	}
	
	public Card[] getCards()
	{
		Card[] cTab = null;
		try {
			cTab = new Card[cards.size()];
			for(int i=0;i<cards.size();i++)
			{
				cTab[i] = cards.get(i);
			}
		}catch(Exception e) {}
		
		return cTab;
	}
	
	public static List<Card> toList(Card[] tab)
	{
		List<Card> list = new ArrayList<>();
		for(Card c : tab)
		{
			list.add(c);
		}
		return list;
	}
	
	public Card getAndDelLastCard()
	{
		if(cards.size() == 0)
			return null;
		Card card = cards.get(cards.size()-1);
		cards.remove(cards.size()-1);
		return card;
	}
	
	public void addCard(Card card)
	{
		this.removeAll();

		cards.add(0, card);

		if(type == 1)
			setupBin();
		else
			setupDeck();

		repaint();
	}
	
	public Card[] getAndDelUpCards()
	{
		List<Card> list = new ArrayList<>();
		Card[] tab;
		for(int i=0;i<cards.size();i++)
		{
			if(cards.get(i).isUp())
			{
				list.add(cards.get(i));
				cards.remove(i);
				i--;
			}
		}
		//putCardsDown();
		setupDeck();
		repaint();
		
		if(list.size() == 0)
			return null;
		
		tab = new Card[list.size()];
		
		for(int i=0;i<list.size();i++)
		{
			tab[i] = list.get(i);
		}
		
		return tab;
	}
	
	public void delUpCards()
	{
		for(int i=0;i<cards.size();i++)
		{
			if(cards.get(i).isUp())
			{
				cards.remove(i);
				i--;
			}
		}
		//putCardsDown();
		setupDeck();
		repaint();
	}
	
	public Card[] getUpCards()
	{
		List<Card> list = new ArrayList<>();
		Card[] tab;
		for(int i=0;i<cards.size();i++)
			if(cards.get(i).isUp())
				list.add(cards.get(i));

		repaint();
		
		if(list.size() == 0)
			return null;
		
		tab = new Card[list.size()];
		
		for(int i=0;i<list.size();i++)
		{
			tab[i] = list.get(i);
		}
		
		return tab;
	}
	
	public void putCardsDown()
	{
		for(int i=0;i<cards.size();i++)
			if(cards.get(i).isUp())
				cards.get(i).setUp(false);
	}
	
	public Card getFirstCard()
	{
		if(cards.size() == 0)
			return null;
		return cards.get(0);
	}
	
	public static Card getRandomCard()
	{
		Card[] tab = getDefaultDeck();
		return tab[genAleaNb(tab.length)];
	}

	public void setLastLength(int val)
	{
		this.lastLength = val;
	}
	
	public int getLastLength() 
	{
		return lastLength;
	}

	public void refreshLastLength() 
	{
		this.lastLength = getLength();
	}

	
}
