import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * WeatherAppGui class provides a graphical user interface (GUI) for displaying weather data.
 * The GUI allows users to search for a location, retrieve real-time weather data, and display it visually.
 */
public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;

    /**
     * Constructor for WeatherAppGui. Initializes the GUI window, sets layout and size properties,
     * and adds GUI components for user interaction.
     */
    public WeatherAppGui() {
        // Sets up GUI title and basic configuration
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null); // Center the window
        setLayout(null); // Manual layout positioning
        setResizable(false); // Disables window resizing
        addGuiComponents(); // Adds the GUI components
    }

    /**
     * Adds all GUI components, including text fields, labels, and buttons.
     * Configures the visual elements to display weather data such as temperature,
     * humidity, wind speed, and weather condition.
     */
    private void addGuiComponents() {
        // Search text field for user input (location)
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchTextField);

        // Weather condition image label
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        // Temperature display label
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        // Weather condition description label
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(8, 405, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        // Humidity icon and text label
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        // Wind speed icon and text label
        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220, 500, 74, 66);
        add(windspeedImage);

        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(310, 500, 85, 55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windspeedText);

        // Search button with click event handler to retrieve and update weather data
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get location from user input and validate
                String userInput = searchTextField.getText();
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return; // Exit if input is empty
                }

                // Retrieve weather data for the specified location
                weatherData = WeatherApp.getWeatherData(userInput);

                // Update weather GUI components based on retrieved data
                String weatherCondition = (String) weatherData.get("weatherCondition");

                // Update the weather condition image based on the condition
                switch (weatherCondition) {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                // Update temperature display
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + " C");

                // Update weather condition description
                weatherConditionDesc.setText(weatherCondition);

                // Update humidity display
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b>: " + humidity + "%</html>");

                // Update wind speed display
                double windspeed = (double) weatherData.get("windSpeed");
                windspeedText.setText("<html><b>Windspeed</b>: " + windspeed + " km/h</html>");
            }
        });
        add(searchButton);
    }

    /**
     * Loads an image from a specified file path to use in the GUI.
     *
     * @param resourcePath The path of the image file to load.
     * @return An ImageIcon object representing the loaded image, or null if an error occurs.
     */
    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not find resource");
        return null;
    }
}