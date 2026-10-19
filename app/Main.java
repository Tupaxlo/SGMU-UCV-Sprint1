package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vista.VistaPrincipalSistema;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Se utilizará el estilo predeterminado.");
            }

            VistaPrincipalSistema sistema = new VistaPrincipalSistema();
            sistema.setVisible(true);
        });
    }
}