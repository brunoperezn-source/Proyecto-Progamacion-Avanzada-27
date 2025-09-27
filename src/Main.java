import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Clase principal del Sistema de Gestión de Voluntarios con interfaz gráfica
 */
public class Main {
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema para mejor apariencia
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("No se pudo cargar el Look and Feel del sistema, usando el predeterminado");
        }

        // Ejecutar la aplicación en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Crear el manager y la interfaz gráfica
                VolunteerManager manager = new VolunteerManager();
                GUIMenu guiMenu = new GUIMenu(manager);
                // La ventana se hace visible dentro del constructor de GUIMenu
            } catch (Exception e) {
                System.err.println("Error al inicializar la aplicación: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}


