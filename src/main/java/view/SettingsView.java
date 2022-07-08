package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import model.AI;
import model.Player;

public class SettingsView extends JPanel {
	private static final long serialVersionUID = 1L;

    private JTextField[] editNames;

    private NamePanel names = new NamePanel();
    
	public SettingsView(Window frame) {
        setOpaque(false);
        FlowLayout layout = new FlowLayout();
        layout.setHgap(999999);
        layout.setVgap(60);
        setLayout(layout);

        // Boutons
        JButton start = new JButton("Start Game");
        JButton back = new JButton("Back");
        back.addActionListener(event -> {
            frame.setHomeView();
        });
        JPanel buttons = new JPanel();
        buttons.add(back);
        buttons.add(start);
        buttons.setOpaque(false);

        editNames = new JTextField[4];
        for(int i=0;i<editNames.length;i++) {
            editNames[i] = new JTextField();
            editNames[i].setColumns(15);
        }

        names.refresh(3, 0);

        // Sélection du nombre de joueurs
        JLabel labelJ = new JLabel("Players");
        JSlider nbJ = new JSlider(2, 4, 4);
        nbJ.setOpaque(false);
        nbJ.setMajorTickSpacing(1);
        nbJ.setPaintLabels(true);
        JPanel joueurs = new JPanel();
        joueurs.setLayout(new BorderLayout());
        joueurs.add(labelJ, BorderLayout.NORTH);
        joueurs.add(nbJ, BorderLayout.SOUTH);
        joueurs.setOpaque(false);
        
        // Sélection du nombre d'IA
        JLabel labelIA = new JLabel("AI");
        JSlider nbIA = new JSlider(0, 4, 3);

        names.refresh(nbJ.getValue(), nbIA.getValue());
        names.revalidate();
        names.repaint();

        nbIA.setOpaque(false);
        nbIA.setMajorTickSpacing(1);
        nbIA.setPaintLabels(true);
        nbIA.addChangeListener(event -> {
            names.refresh(nbJ.getValue(), nbIA.getValue());
            names.revalidate();
            names.repaint();
        });
        nbJ.addChangeListener(event -> {
            names.refresh(nbJ.getValue(), nbIA.getValue());
            names.revalidate();
            names.repaint();
        });
        JPanel ia = new JPanel();
        ia.setLayout(new BorderLayout());
        ia.add(labelIA, BorderLayout.NORTH);
        ia.add(nbIA, BorderLayout.SOUTH);
        ia.setOpaque(false);

        JPanel speedPan = new JPanel();
        speedPan.setOpaque(false);
        JSlider AISpeed = new JSlider(1, 1000, 1000);
        AISpeed.setOpaque(false);
        AISpeed.setMajorTickSpacing(999);
        AISpeed.setPaintLabels(true);
        speedPan.setLayout(new BorderLayout());
        speedPan.add(new JLabel("AI Speed"), BorderLayout.NORTH);
        speedPan.add(AISpeed, BorderLayout.SOUTH);

        add(joueurs);
        add(ia);
        add(speedPan);
        add(names);
        add(buttons);

        start.addActionListener(event -> {
            AI.sleepTime = AISpeed.getValue();
            BoardGame board = new BoardGame(frame);
            int nJ = nbJ.getValue() - nbIA.getValue();
            Player[] players = new Player[nbIA.getValue()+nJ];

            char[] pos = new char[] {'B', 'L', 'U', 'R'};
            for(int i=0;i<players.length;i++) {
                if(i < nJ) {
                    players[i] = new Player(board, pos[i], i == 0);
                    if(!editNames[i].getText().isBlank())
                        players[i].setName(editNames[i].getText());
                }
                else {
                    players[i] = new AI(board, pos[i]);
                }
            }
            board.setupPlayers(players);
            frame.setBoardGame(board);
        });
    }

    private class NamePanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public NamePanel() {
            this.setOpaque(false);
        }

        public void refresh(int p, int ai) {
            this.removeAll();
            if (ai > p)
                ai = p;
            this.setLayout(new GridLayout(2, p));
            for (int i = 1; i <= p - ai; i++)
                this.add(new JLabel("Player " + i));
            for (int i = p - ai; i < p; i++)
                this.add(new JLabel("AI " + (i+1)));
            for(int i=0;i<p;i++) {
                editNames[i].setEditable(true);
                this.add(editNames[i]);
            }
            for(int i=p-ai;i<p;i++) {
                editNames[i].setEditable(false);
                editNames[i].setText("");
            }
        }

    }
    
}
