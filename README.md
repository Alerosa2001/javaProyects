
# WeatherApp

A Java-based weather application that fetches real-time weather data for a user-specified location, utilizing the [Open-Meteo API](https://open-meteo.com/) for both geolocation and weather information. This repository provides both a backend to retrieve data from Open-Meteo and a graphical user interface (GUI) to display this data interactively.

## Features

- Retrieves up-to-date weather information including temperature, weather condition, humidity, and wind speed.
- Utilizes geolocation data to find latitude and longitude coordinates for specified locations.
- Displays information in a user-friendly GUI with icons representing different weather conditions.
- Data updates dynamically based on user input.

## How It Works

1. **Geolocation**: When a user enters a location (e.g., "New York"), the application uses Open-Meteo's geolocation API to retrieve the latitude and longitude of that location.
2. **Weather Data Retrieval**: Using the obtained latitude and longitude, the application requests weather data from Open-Meteo's weather API. The API returns hourly weather details for the location.
3. **Display**: The GUI then displays the relevant weather data (temperature, condition, humidity, and wind speed) and updates corresponding icons for a visually informative experience.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven or Gradle (optional, if using dependencies)
- Access to Open-Meteo API (no API key required)

### Installation

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/your-username/WeatherApp.git
    cd WeatherApp
    ```

2. **Add Dependencies**:
   Make sure to include the required libraries for JSON handling (`org.json.simple`) and image processing (`javax.imageio.ImageIO`). You may use Maven or manually add them.

3. **Compile and Run**:
   - Compile the project and execute the main class `AppLauncher` to start the GUI.
   - The GUI will open, allowing you to enter a location and view weather data.

### Running the Application

- Run the application by executing the `AppLauncher` class:
    ```bash
    javac AppLauncher.java
    java AppLauncher
    ```
- The application GUI will load, displaying fields for weather data. Enter a location in the search field and press the search icon to fetch and display the weather data.

## Technologies Used

- **Java Swing**: For the graphical user interface.
- **Open-Meteo API**: Provides geolocation and weather data in JSON format.
- **JSON Simple Library**: Used for JSON parsing.
- **Java Standard Libraries**: Includes libraries for HTTP connections (`HttpURLConnection`), image processing (`javax.imageio.ImageIO`), and multithreading (`SwingUtilities.invokeLater`).

## API Information

- **Open-Meteo API**:
   - [Geolocation Endpoint](https://open-meteo.com/en/docs#geocoding-api): Used to obtain the latitude and longitude of a location.
   - [Weather Forecast Endpoint](https://open-meteo.com/en/docs): Fetches hourly weather data, including temperature, humidity, and wind speed.
   - **Note**: Open-Meteo is a free, open-source API that does not require an API key.

### Important Files

- **`WeatherApp.java`**: Handles data retrieval from Open-Meteo, parsing JSON responses, and providing weather details.
- **`WeatherAppGui.java`**: Contains the GUI code to display weather data to the user.
- **`AppLauncher.java`**: Entry point of the application, initializes and displays the GUI.

## Future Improvements

- **Forecast Display**: Extend the application to display weather forecasts for the upcoming days.
- **Error Handling**: Enhance error handling to inform users of issues, such as invalid location names or connectivity problems.
- **Multilingual Support**: Enable users to view weather descriptions in various languages.
- **Settings**: Provide user customization options like temperature units (Celsius/Fahrenheit).

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Contributing

Feel free to contribute by creating a pull request or opening an issue. Contributions to improve functionality, add features, or resolve bugs are welcome!

## Acknowledgments

- Thanks to [Open-Meteo](https://open-meteo.com/) for their free weather and geolocation API.
- [JSON Simple](https://code.google.com/archive/p/json-simple/) for JSON handling in Java applications.
