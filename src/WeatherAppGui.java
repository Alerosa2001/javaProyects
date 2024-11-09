import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;

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

            // imagen del clima
            JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
            weatherConditionImage.setBounds(0,125,450,217);
            add(weatherConditionImage);
            // texto de temperatura
            JLabel temperatureText = new JLabel("10 C");
            temperatureText.setBounds(0,350,450,54);
            temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
            //centrado texto
            temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
            add(temperatureText);
            // Descripcion del clima
            JLabel weatherConditionDesc = new JLabel("Cloudy");
            weatherConditionDesc.setBounds(8,405,450,36);
            weatherConditionDesc.setFont(new Font("Dialog",Font.PLAIN, 32));
            weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
            add(weatherConditionDesc);
            // Imagen de humedad
            JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
            humidityImage.setBounds(15,500,74,66);
            add(humidityImage);
            //texto descriptivo de humedad utilizando etiquetas HTML dentro de los SwingComponents
            JLabel humidityText = new JLabel("<html><b>Humidity</b> 100% </hmtl>");
            humidityText.setBounds(90,500,85,55);
            humidityText.setFont(new Font("Dialog", Font.PLAIN,16));
            add(humidityText);
            //Imagen de Viento
            JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
            windspeedImage.setBounds(220,500,74,66);
            add(windspeedImage);
            // texto descriptivo del viento
            JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h </hmtl>");
            windspeedText.setBounds(310,500,85,55);
            windspeedText.setFont(new Font("Dialog", Font.PLAIN,16));
            add(windspeedText);

            // boton de busqueda

            JButton searchButton = new JButton(loadImage("src/assets/search.png"));
            //cambia el cursor al hacer hover sobre el boton
            searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            searchButton.setBounds(375,13,47,45);
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // get location from user
                    String userInput = searchTextField.getText();

                    // validate input - remove whitespace to ensure non-empty text
                    if (userInput.replaceAll("\\s","").length() <= 0){
                        return;
                    }
                    //retrieve weather data

                    weatherData = WeatherApp.getWeatherData(userInput);

                    //update weather gui

                    //update weather image
                    String weatherCondition = (String) weatherData.get("weatherCondition");

                    //depending on the condition we will update the weather image that corresponds with the condition
                    switch (weatherCondition){
                        case  "Clear":
                            weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                            break;
                        case  "Cloudy":
                            weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                            break;
                        case  "Rain":
                            weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                            break;
                        case  "Snow":
                            weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                            break;

                    }

                    //update temperature text
                    double temperature = (double) weatherData.get("temperature");
                    temperatureText.setText(temperature + " C");

                    //update weather condition text
                    weatherConditionDesc.setText(weatherCondition);

                    //update humidity text
                    long humidity = (long) weatherData.get("humidity");
                    humidityText.setText("<html><b>Humidity</b>: " + humidity + "%</html>");

                    //update windspeed
                    double windspeed = (double) weatherData.get("windSpeed");
                    windspeedText.setText("<html><b>Windspeed</b>: " + windspeed + " km/h</html>");



                }
            });
            add(searchButton);


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
