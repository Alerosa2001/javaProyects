import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                        //muestra nuestra app gui de clima
                        new WeatherAppGui().setVisible(true);
            }
        });
    }
}
