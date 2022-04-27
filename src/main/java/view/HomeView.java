package view;

import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class HomeView extends JPanel {
    
    private Window window;
    private JButton play;

    public HomeView(Window window) {
        this.window = window;
        this.play = new JButton("Play");
        this.play.addActionListener(e -> {
            window.setBoardGame();
        });
        this.setLayout(new GridBagLayout());
        this.add(play);
    }

}
