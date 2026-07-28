package app;

import javax.swing.SwingUtilities;
import controllers.MapController;
import view.MainFrame;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainFrame frame = new MainFrame();

            new MapController(frame);

        });
    }
}
