import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    public WeatherAppGui(){
            //setup gui y agregado de titulo
            super("Weather App");

            //configura gui para que finalice el programa una vez que este se cierra.
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            // Seteo de pixeles del gui
            setSize(450,650);
        //Posicionamiento del gui en el centro de la pantalla
            setLocationRelativeTo(null);
            //configura el layout manager a null para posicionar los componentes del gui manualmente
            setLayout(null);
            //Elimina la opcion de resize del gui
            setResizable(false);
            addGuiComponents();
    }
        private void addGuiComponents(){
        //campo de busqueda
            JTextField searchTextField = new JTextField();
        // seteo de ubicacion y tamaño de nuestro componente
            searchTextField.setBounds(15,15,351,45);
            // cambio de fuente y estilo
            searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
            add(searchTextField);

            // boton de busqueda

            JButton searchButton = new JButton(loadImage("src/assets/search.png"));
            add(searchButton);
            //cambia el cursor al hacer hover sobre el boton
            searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            searchButton.setBounds(375,13,47,45);
    }



        //crea imagenes para nuestros componentes del gui
        private ImageIcon loadImage(String resourcePath){
        // lee el archivo de imagen desde el path dado
            try{
                BufferedImage image = ImageIO.read(new File(resourcePath));
                // devuelve la imagen para que el componente la renderice
                return new ImageIcon(image);

            } catch (IOException e) {
               e.printStackTrace();
            }
            System.out.println("Could not find resoruce");
                return null;
        }
}
