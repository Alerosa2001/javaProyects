import javax.swing.*;

/**
 * AppLauncher is the main entry point for the Weather Application.
 * It initiates the application by launching the GUI on the Swing event-dispatching thread.
 */
public class AppLauncher {

    /**
     * Main method that serves as the application's starting point.
     * It invokes the WeatherAppGui class, creating and displaying the GUI.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Launches the WeatherAppGui on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and display the WeatherApp GUI
                new WeatherAppGui().setVisible(true);
            }
        });
    }
}