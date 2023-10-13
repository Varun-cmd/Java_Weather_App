import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class weather {


    // <--- fetch data from given location


    public static JSONObject getWeatherData(String locationName){

        //  get coordinates through geolocation API
        JSONArray locationData = getLocationData(locationName);

        //  extract latitude and longitude
        JSONObject location = (JSONObject)locationData.get(0);
        double lat = (double) location.get("latitude");
        double longi = (double) location.get("longitude");

        // build API
        String url ="https://api.open-meteo.com/v1/forecast?" +
        "latitude=" + lat + "&longitude=" + longi +
        "&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m&timezone=Asia%2FTokyo";

        try{
            // calling API and recieve response
            HttpURLConnection conn = fetchApiResponse(url);

            if (conn.getResponseCode()!=200){
                System.out.println("Error could not connect to API");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner in = new Scanner(conn.getInputStream());               // <-- read and store data in string builder
            while(in.hasNext()){
                resultJson.append(in.nextLine());
            }
            in.close();
            conn.disconnect();
            JSONParser parser  = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            // get List of location data
            JSONObject hourly = (JSONObject) resultsJsonObj.get("hourly");

            // Need current hours data so index current hour

            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexofCurrent(time);

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temp  = (double) temperatureData.get(index);
            JSONArray weathercode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long)weathercode.get(index));

            // humidity data

            JSONArray relHumidity = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long)  relHumidity.get(index);

            // windSpeed data

            JSONArray windSpeedData = (JSONArray) hourly.get("windspeed_10m");
            double windspeed = (double)  windSpeedData.get(index);


            // build objects to return to front end
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature",temp);
            weatherData.put("weather_condition",weatherCondition);
            weatherData.put("humidity",humidity);
            weatherData.put("windspeed",windspeed);

            return weatherData;



        }catch (Exception e){
            e.printStackTrace();
        }



        return  null;
    }

    public static JSONArray getLocationData(String location){
        // input formatting for API
         location = location.replaceAll(" ","+");
         String url ="https://geocoding-api.open-meteo.com/v1/search?name=" + location + "&count=10&language=en&format=json";
         try{
             HttpURLConnection conn = fetchApiResponse(url);

             //checking response
             if(conn.getResponseCode()!=200){
                 System.out.println("Error could not connect to API");
             }
             else{
                 StringBuilder resultJson = new StringBuilder();
                 Scanner in = new Scanner(conn.getInputStream());               // <-- read and store data in string builder
                 while(in.hasNext()){
                     resultJson.append(in.nextLine());
                 }
                 in.close();
                 conn.disconnect();
                 JSONParser parser  = new JSONParser();
                 JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                 // get List of location data
                 JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                 return locationData;


             }


         }catch (Exception e){
             e.printStackTrace();
         }
         return null;
    }

    private static  HttpURLConnection fetchApiResponse(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            // connect api
            conn.connect();
            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static int  findIndexofCurrent(JSONArray timeList){
        String currentTime = getCurrentTime();

        // iterating though time list to match current time;
        for (int i = 0; i <timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime)){
                return i;
            }

        }
        return 0;
    }
    public static String getCurrentTime(){
        // get current data and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 2023-01-01T00:00 (API format) ..
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedDatetime = currentDateTime.format(formatter);

        return formattedDatetime;

    }
    private  static  String convertWeatherCode(long weathercode){

        // convertng weather codes to readable format

        String weatherCondition  = "";
        if(weathercode==0L)
            weatherCondition = "Clear";
        else if (weathercode >  0L && weathercode<=3L)
            weatherCondition = "Partly Cloudy";
        else if (weathercode >  44L && weathercode<=49L)
            weatherCondition = "Cloudy";
        else if (weathercode >= 51L && weathercode <=67L||weathercode >= 80L && weathercode <=99L)
            weatherCondition = "Rain";
        else if (weathercode >  71L && weathercode<=77L)
            weatherCondition = "Snow";

        return weatherCondition;

    }
    

}
