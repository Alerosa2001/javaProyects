import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// Logica de backend que trae los datos del clima desde una API externa
// GUI mostrara estos datos traidos para responder las peticiones del usuario
public class WeatherApp {
    // Traer data para una locacion dada
    public static JSONObject getWeatherData(String locationName){
            // obtiene las coordenadas de la ubicacion usando la API de geolocalizacion
        JSONArray locationData = getLocationData(locationName);

            //Obtener latitud y longitud
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // Construir la REQUEST a la API con las coordenadas de la ubicacion
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
        "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=America%2FSao_Paulo";
        try {
            // llamar a la api y obtener respuesta
            HttpURLConnection conn = fetchApiResponse(urlString);
            //Check estado de respuesta (200 conexion exitosa)
            if (conn.getResponseCode()!= 200){
                System.out.println("Error: No se puede establecer conexion con la API");
                return null;
            }
            //Guarda los datos del JSON
            StringBuilder resultJSon = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()){
                // Lee y guarda el JSON en el String Builder
                resultJSon.append(scanner.nextLine());
            }
            // Cerrar scanner y conexion
            scanner.close();
            conn.disconnect();
            // JSON Parse
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJSon));

            // Obtener la data actualizada por hora
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
            //Obtener el index de la hora
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            //Obtener temperatura
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            //Obtener codigo clima
            JSONArray weather_code = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long)weather_code.get(index));

            //Obtener humedad
            JSONArray relative_humidity = (JSONArray)  hourly.get("relative_humidity_2m");
            long humidity = (long) relative_humidity.get(index);

            //Obtener velocidad de viento
            JSONArray wind_speed_10m = (JSONArray) hourly.get("wind_speed_10m");
            double windSpeed = (double) wind_speed_10m.get(index);


            // Construir el JSON Data Object que vamos al que vamos a acceder desde el FrontEnd
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature",temperature);
            weatherData.put("weatherCondition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windSpeed", windSpeed);
            return weatherData;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //Obtiene las coordenadas geograficas de una ubicacion dada
    public static JSONArray getLocationData(String locationName){
        // reemplaza cualquier espacio en blanco del nombre de ubicacion por un "+" para adherir al formato de request de la API
        //ejemplo: New York es formateado a New+York
        locationName = locationName.replaceAll(" ", "+");

        // url de la API con un parametro de ubicacion
        String urlString ="https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName+ "&count=10&language=en&format=json";
        try {
            // llamar a la API y obtener una respuesta
            HttpURLConnection conn = fetchApiResponse(urlString);
            // check estado de conexion, 200 es satisfactorio/400 error nuestro / 500 error ajeno a nuestro programa
            if (conn.getResponseCode() != 200){
                System.out.println("Error: no se pudo conectar con la API");
                return null;
            }else {
                // guardar los resultados de la API
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                //Lee y guarda los datos del JSON en nuestro StringBuilder
                while (scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }
                // Cerra el scanner y desconectar la conexion Http para optimizar recursos
                scanner.close();
                conn.disconnect();
                //JSON String a JSON Obj
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                //Obtener la lista de data de la ubicacion generada por la API con el locationName
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static HttpURLConnection fetchApiResponse(String urlString){
        try {
            // Intento de crear conexion
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //seteo de metodo para hacer la REQUEST del GET
            conn.setRequestMethod("GET");
            //conectar con la API
            conn.connect();
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //no se pudo realizar la conexion
        return null;
    }
    private static int findIndexOfCurrentTime(JSONArray timeList){
        String currentTime = getCurrentTime();
        //iterar la time list y ver cual valor coincide con nuestra hora local
        for (int i = 0;i<timeList.size();i++){
            String time = (String)timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)){
                //devolver el index
                return i;
            }
        }

        return 0;
    }
    public static String getCurrentTime(){
        //Obtener el date y time del momento
        LocalDateTime currentDateTime = LocalDateTime.now();
        //Formateo al modo de lectura en la API
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        //Formatea e imprime el Date time actual
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
    }
        //Convertir el weather code en algo mas leible
    private static String convertWeatherCode(long weather_code){
        String weatherCondition = "";
        if (weather_code == 0L){
            //clear
            weatherCondition = "Clear";
        } else if (weather_code <= 3L && weather_code >0L) {
            //cloudy
            weatherCondition = "Cloudy";
        } else if ((weather_code >= 51L && weather_code <= 67L)
            || (weather_code >= 80L && weather_code <= 99L)) {
            //rain
            weatherCondition = "Rain";
        } else if (weather_code >= 71L && weather_code <= 77L) {
            //snow
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }
}
