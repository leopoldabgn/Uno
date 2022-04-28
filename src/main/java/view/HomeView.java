package view;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class HomeView extends JPanel {
    
    private Window window;
    private JButton quit, play;

    public HomeView(Window window) {
        setOpaque(false);
        this.window = window;
        this.quit = new JButton("Quit");
        this.quit.setPreferredSize(new Dimension(220, 50));
        quit.addActionListener(e -> {
            window.quit();
        });
        this.play = new JButton("Play");
        this.play.setPreferredSize(new Dimension(220, 50));
        this.play.addActionListener(e -> {
            window.setSettingsView();
        });

        JPanel pan = new JPanel();
        pan.setOpaque(false);
        pan.setLayout(new GridLayout(2, 1));
        pan.add(play);
        pan.add(quit);
        this.setLayout(new GridBagLayout());
        this.add(pan);
    }

}
