import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // to display app GUI

                new appGUI().setVisible(true);

//                System.out.println(weather.getLocationData("India"));
//                System.out.println(weather.getCurrentTime());

            }

        });
    }

}
