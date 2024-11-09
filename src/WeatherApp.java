import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * WeatherApp is responsible for fetching and parsing weather data
 * from an external API to provide up-to-date weather information
 * for a specified location.
 */
public class WeatherApp {

    /**
     * Retrieves weather data for a given location.
     * This method calls a geolocation API to get coordinates for the specified
     * location and then queries a weather API using these coordinates.
     *
     * @param locationName Name of the location for which to fetch weather data.
     * @return JSONObject containing temperature, weather condition, humidity, and wind speed data.
     */
    public static JSONObject getWeatherData(String locationName) {
        // Get coordinates of the location using the geolocation API
        JSONArray locationData = getLocationData(locationName);

        // Extract latitude and longitude
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // Construct the weather API request URL with coordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=America%2FSao_Paulo";

        try {
            // Call the API and get the response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // Check response status; return null if the connection fails
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Unable to connect to the weather API");
                return null;
            }

            // Read and store JSON response
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }
            // Close scanner and connection
            scanner.close();
            conn.disconnect();

            // Parse JSON data
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            // Extract hourly updated weather data
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            // Extract temperature
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            // Extract weather code and convert it to readable condition
            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weatherCode.get(index));

            // Extract humidity
            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            // Extract wind speed
            JSONArray windSpeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windSpeed = (double) windSpeedData.get(index);

            // Construct JSON object with weather data for frontend access
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weatherCondition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windSpeed", windSpeed);
            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves geographic coordinates for a given location name.
     *
     * @param locationName Name of the location to retrieve coordinates for.
     * @return JSONArray containing location data with latitude and longitude.
     */
    public static JSONArray getLocationData(String locationName) {
        // Format location name for API request by replacing spaces with "+"
        locationName = locationName.replaceAll(" ", "+");

        // Geolocation API URL with location name parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";
        try {
            // Call the API and get a response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // Check response status; return null if connection fails
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Unable to connect to geolocation API");
                return null;
            }

            // Read and store JSON response
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }
            // Close scanner and disconnect HTTP connection
            scanner.close();
            conn.disconnect();

            // Parse JSON string to JSON object
            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            // Get list of location data generated by API
            JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
            return locationData;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetches API response for a given URL.
     *
     * @param urlString URL string to make GET request.
     * @return HttpURLConnection object with API response data.
     */
    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            // Attempt to open connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Set request method to GET and connect to API
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Connection attempt failed
    }

    /**
     * Finds the index of the current hour in the list of hourly timestamps.
     *
     * @param timeList List of hourly timestamps.
     * @return Index of the timestamp that matches the current hour.
     */
    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();
        // Iterate through time list to find matching local time value
        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                return i; // Return the index if match is found
            }
        }
        return 0; // Default to first entry if no match is found
    }

    /**
     * Retrieves the current date and time in the format required by the API.
     *
     * @return String representing the current date and hour formatted as "yyyy-MM-dd'T'HH':00'".
     */
    public static String getCurrentTime() {
        // Retrieve current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Format to match API-readable format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        return currentDateTime.format(formatter);
    }

    /**
     * Converts weather code to a readable weather condition.
     *
     * @param weatherCode Weather code from the API.
     * @return String representing the weather condition.
     */
    private static String convertWeatherCode(long weatherCode) {
        String weatherCondition = "";
        if (weatherCode == 0L) {
            weatherCondition = "Clear";
        } else if (weatherCode <= 3L && weatherCode > 0L) {
            weatherCondition = "Cloudy";
        } else if ((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)) {
            weatherCondition = "Rain";
        } else if (weatherCode >= 71L && weatherCode <= 77L) {
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }
}