package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Player;

public class RankingView extends JPanel {
    
    private Window window;
    private ResultsLine[] rLines;
    private Player[] players;
    private int shadowSpace = 25;

    public RankingView(Window window, Player[] players) {
        setOpaque(false);
        this.window = window;
        this.players = players;

        int topBottom = 32, leftRight = 64;

        this.setBorder(new EmptyBorder(topBottom, leftRight,
                                       topBottom + shadowSpace, leftRight + shadowSpace));
        this.setLayout(new GridLayout(players.length+1, 1));
        
        rLines = new ResultsLine[players.length];

        for(int rank=1;rank<players.length+1;rank++)
            for(Player p : players)
                if(p.getRank() == rank)
                    rLines[rank-1] = new ResultsLine(p.getRank(), p.getName());

        for(ResultsLine line : rLines)
            this.add(line);
        
        JButton quit = new JButton("Quit");
        quit.setPreferredSize(new Dimension(150, 30));
        quit.addActionListener(e -> {
            window.quit();
        });

        JButton menu = new JButton("Menu");
        menu.setPreferredSize(new Dimension(150, 30));
        menu.addActionListener(e -> {
            Player.resetPlayerNumbers();
            window.setHomeView();
        });

        JPanel pan = new JPanel();
        pan.setOpaque(false);
        pan.setLayout(new GridLayout(1, 2));
        pan.setBorder(new EmptyBorder(20, 30, 20, 30));
        pan.add(quit);
        pan.add(menu);

        add(pan);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        int radius = 50;
        g2d.setColor(Color.BLACK);
        int space = 9;
        g2d.fillRoundRect(space, space, getWidth()-space, getHeight()-space, radius, radius);
        g2d.setColor(new Color(97, 19, 19));
        g2d.fillRoundRect(0, 0, getWidth()-shadowSpace, getHeight()-shadowSpace, radius, radius);
    }

    public static class ResultsLine extends JPanel {
    
        public static Color background = new Color(255, 255, 255);
    
        private JLabel lbl;
    
        public ResultsLine(int number, String text) {
            this.lbl = new JLabel(text);
            
            setOpaque(false);
    
            int border = 20;
            this.setBorder(new EmptyBorder(0, border, 0, border));
    
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 32f));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
    
            JPanel leftPan = new JPanel();
            leftPan.setOpaque(false);
            leftPan.setLayout(new GridBagLayout());
            leftPan.add(new Circle(number, 30));
            
            this.setLayout(new BorderLayout());
            this.add(leftPan, BorderLayout.WEST);
            this.add(lbl, BorderLayout.CENTER);
        }
    
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;
            
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            
            int radius = 50;
            g2d.setColor(background);
            g2d.fillRoundRect(0, 10, getWidth(), getHeight()-10, radius, radius);
        }
    
        public static void changeTextSize(JLabel lbl, int size) {
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, size));
        }
    
        private class Circle extends JPanel {
            private JLabel number;
    
            public Circle(int number, int size) {
                setOpaque(false);
                this.setPreferredSize(new Dimension(size, size));
                this.number = new JLabel(""+number);
                changeTextSize(this.number, 18);
                this.number.setAlignmentX(SwingConstants.CENTER);
                this.setLayout(new GridBagLayout());
                this.add(this.number);
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                /*
                Graphics2D g2d = (Graphics2D)g;
            
                g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.WHITE);
                g2d.fillOval(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.BLACK);
                g2d.drawOval(0, 0, getWidth(), getHeight());
                */
            }
    
        }
    
    }

}
