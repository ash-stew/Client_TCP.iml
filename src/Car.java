
import javax.swing.*;
import java.io.Serializable;

public class Car implements Serializable {
    private static final long serialVersionUID = 752746024; // This needs to match that of the Server UID

    private int XPosition;
    private int YPosition;
    private int speed = 0;
    private final int TOTAL_IMAGES = 16;
    ImageIcon[] Cars;
    private int currentCar = 0;  // current image number of car
    private  boolean hasCrashed = false; // set true when crash with inner or outer areas
    private String carColor;

    public Car(int Xpos, int Ypos, String color)  // constructor
    {
        carColor = color;
        XPosition = Xpos;
        YPosition = Ypos;
        Cars = new ImageIcon[TOTAL_IMAGES]; // Initializing the Cars array

        // Loading all images of a color into Cars
        for (int i = 0; i < Cars.length; i++) {
            Cars[i] = new ImageIcon(getClass().getResource("res/" + color + i + ".png"));
        }

    }  // end of constructor

    public Car() {

    }

    public int getCurrentCar() { // will be used to paint car and to check current position when rotating up/down
        return currentCar;
    } //used to paint car and to check current position after up/down

    public int setCurrentCar(int car) {  // used to set car's new facing position after up or down
        currentCar = car;
        return currentCar;
    }

    public void setHasCrashed(boolean b) // Set true after being informed by server had crashed inner/outer
    {
        hasCrashed = b;
    }

    public int setSpeed(int s) // Used to set speed to 0 after being informed by server had crashed
    {
        speed = s;
        return s;
    }

    public int getSpeed() // Used to get current speed of car before accelerating/de-accelerating
    {
        return speed;
    }

    public void increaseSpeed() // Used to increment car speed by 1
    {
        if(speed < 100 && hasCrashed == false)
        {
            speed += 1;
        }
    }

    public void decreaseSpeed() // Used to decrement speed by 1
    {
        if(speed > 0)
        {
            speed = speed - 1;
        }
    }
 // Used to paint the current position of car and opposing player's car
    public int getXPosition()
    {
        return XPosition;
    }
    public int getYPosition()
    {
        return YPosition;
    }
   // Is called in Client constructor at start of session, prompting user to select a car color
    public void setupCar()
    {
        String[] carChoices = new String[] {"Blue", "Purple", "Red", "Green", "Yellow"};
        int option = JOptionPane.showOptionDialog(null, "Please choose car", "Car selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, carChoices, carChoices[0]);

        switch(option)
        {

            case 0:
                carColor = "RacingCarBlue";
                setCarColor();
                break;
            case 1:
                carColor = "RacingCarPurple";
                setCarColor();
                break;

            case 2:
                carColor = "RacingCarRed";
                setCarColor();
                break;

            case 3:
                carColor = "RacingCarGreen";
                setCarColor();
                break;

            case 4:
                carColor = "RacingCarYellow";
                setCarColor();
                break;
        }

    }

    public void setCarColor() // used to load all the color images of a users choice
    {
        for (int i = 0; i < Cars.length; i++)
        {
            Cars[i] = new ImageIcon(getClass().getResource("res/" + carColor + i + ".png"));
        }
    }

// Used to inform Server of car color choice and to print a personalized message to winner and loser after a race
    public String getCarColor()
    {
        return carColor;
    }


    public void moveCar()
    {

        switch(currentCar)
        {
            case 0:
                XPosition = XPosition + 2 * speed;
                break;

            case 1:
                XPosition = XPosition + 2 * speed;
                YPosition = YPosition +     speed;
                break;

            case 2:
                XPosition = XPosition + 2 * speed;
                YPosition = YPosition + 2 * speed;
                break;

            case 3:
                XPosition = XPosition +     speed;
                YPosition = YPosition + 2 * speed;
                break;

            case 4:
                YPosition = YPosition + 2 * speed;
                break;

            case 5:
                XPosition = XPosition -     speed;
                YPosition = YPosition + 2 * speed;
                break;

            case 6:
                XPosition = XPosition - 2 * speed;
                YPosition = YPosition + 2 * speed;
                break;

            case 7:
                XPosition = XPosition - 2 * speed;
                YPosition = YPosition +     speed;
                break;

            case 8:
                XPosition = XPosition - 2 * speed;
                break;

            case 9:
                XPosition = XPosition - 2 * speed;
                YPosition = YPosition -     speed;
                break;

            case 10:
                XPosition = XPosition - 2 * speed;
                YPosition = YPosition - 2 * speed;
                break;

            case 11:
                XPosition = XPosition -     speed;
                YPosition = YPosition - 2 * speed;
                break;

            case 12:
                YPosition = YPosition - 2 * speed;
                break;

            case 13:
                XPosition = XPosition +     speed;
                YPosition = YPosition - 2 * speed;
                break;

            case 14:
                XPosition = XPosition + 2 * speed;
                YPosition = YPosition - 2 * speed;
                break;

            case 15:
                XPosition = XPosition + 2 * speed;
                YPosition = YPosition -     speed;
                break;
        }

    } // end of moveCar method

} // End of Car class