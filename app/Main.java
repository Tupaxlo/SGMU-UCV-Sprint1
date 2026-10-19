package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vista.VistaPrincipal;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Se utilizará el estilo predeterminado.");
            }

            // Arrancamos directamente el Panel Central
            VistaPrincipal menuCentral = new VistaPrincipal();
            menuCentral.setVisible(true);
        });
    }
}