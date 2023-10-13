import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOError;
import java.io.IOException;

public class appGUI extends JFrame{
    private JSONObject weatherData;

    public appGUI(){
        super("Weather APP");

        // end once process after closing
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set size of gui
        setSize(460,650);

        // load gui at center of the screen
        setLocationRelativeTo(null);

        // manually posiiton gui at center of screen
        setLayout(null);
        setResizable(false);

        addGuiComponents();
    }
    private void addGuiComponents(){

        // <--- search field

        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15,15,365,45 );
        searchTextField.setFont(new Font("Dialog",Font.PLAIN,24));
        add(searchTextField);




        // <--- Weather Image
        JLabel weatherConditionImage  = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);

                 //  Text
                 JLabel temperatureText = new JLabel("10 C" );
                 temperatureText.setBounds(0,350,450,54);
                 temperatureText.setFont(new Font("Dialog",Font.BOLD,48));
                 temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        //  <--weather description

        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0,405,450,36);
        weatherConditionDesc.setFont(new Font("Dialog",Font.PLAIN,32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        // <--- Humid image
        JLabel humid  = new JLabel(loadImage("src/assets/humidity.png"));
        humid.setBounds(15,500,74,66);
        add(humid);

                // text
                JLabel humidText = new JLabel("<html><b>Humidity</b> 100%</html>");
                humidText.setBounds(90,500,85,55);
                humidText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(humidText);

        // <-- WindSpeed image
        JLabel windspeed  = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeed.setBounds(220,500,94,66);
        add(windspeed);

                // text
                JLabel windtext = new JLabel("<html><b>WindSpeed</b> 12km/hr</html>");
                windtext.setBounds(310,500,100,55);
                windtext.setFont(new Font("Dialog",Font.PLAIN,16));
        add(windtext);

        // <--- search button
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        // <--- cursor hover effects
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(385,15,47,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  <--- Get location from the user
                String userInput = searchTextField.getText();
                if(userInput.replaceAll("\\s","").length()<=0)
                 return ;

                // <--- retrieve weather data
                weatherData = weather.getWeatherData(userInput);

                // <--- updating GUI


                // <-- update weather image
                String condition = (String) weatherData.get("weather_condition");
                switch (condition){
                    case "clear":
                        weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Partly Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                }

                // <--- update values  text
                double temp = (double) weatherData.get("temperature");
                temperatureText.setText(temp+ "C");
                weatherConditionDesc.setText(condition);
                long humidity = (long) weatherData.get("humidity");
                humidText.setText("<html><b>Humidity</b><br>" + humidity + "%</html>");
                double wind = (double) weatherData.get("windspeed");
                windtext.setText("<html><b>WindSpeed</b><br>" + wind + "km/h</html>");




            }
        });
        add(searchButton);






    }

    // <-- create images in GUi
    private ImageIcon loadImage(String resourcePath){
     try{
         BufferedImage image = ImageIO.read(new File(resourcePath));
         return new ImageIcon(image);
     }catch (IOException e){
         e.printStackTrace();
     }
        System.out.println("Could Not find resource");
     return null;
    }

}
