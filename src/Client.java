import javax.swing.*;
import java.io.*;
import java.net.Socket;
import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;

public class Client  extends JPanel implements KeyListener
{
    private static Socket clientSocket = null; // The socket for the client
    private static String serverHost = "localhost";  // the host name that will be connected to via socket
    private int portNumber = 5000; // The port number of the host, set to 5000 by default
    private static String responseLine; // the message from the server
    private static ObjectOutputStream output = null; // used to send objects (a car) and messages (a string)
    private static ObjectInput input = null; // used to read objects (a car) & messages (a string)
    private Timer animationTimer; // Will serve as the action timer
    private final int ANIMATION_DELAY = 100; // in milsecs,firing new action event per interval
    AudioInputStream audio; // 'audio' will make it possible to work with wav files
    File carCrashSound = new File("src/crash_metal2.wav"); // played when cars collide into each-other
    File winnerSound = new File("src/Congratulations.wav");
    File loserSound = new File("src/LoserSound.wav");
    File collideEdgeSound = new File("src/carCrash.wav");
    private int playEdgeSoundCounter = 0;

    JFrame gameOver = new JFrame("Game Over"); // Will be used after game for message and confirm dialogues
    JFrame connection = new JFrame("Connection"); // Will act as input dialogue for server name & port number

    private static Car car = new Car(); // The player's car
    private static Car foreignCar = new Car();  // The opposing player's car

    // replayGame set to true after a game, used in sendCar() to avoid server having to 'identify' again, (in switch)
    private static boolean replayGame = false;
    private static boolean gameStarted = false; // player cannot move while false. Set true once both players set-up

    public Client() throws Exception   // Client constructor
    {
        // prompting user for port no & server name, will then initialize the client socket and input/output stream
        serverHost = JOptionPane.showInputDialog(connection, "Please enter server name");
        portNumber = Integer.parseInt(JOptionPane.showInputDialog(connection, "Please enter port number"));
        clientSocket = new Socket(serverHost, portNumber);
        output = new ObjectOutputStream(clientSocket.getOutputStream());
        input = new ObjectInputStream(clientSocket.getInputStream());

        addKeyListener(this); // Adding keyListener, to make it possible to control the cars
        setFocusable(true); // will enable the JPanel to be focussed
        setFocusTraversalKeysEnabled(false); // keys like Tab not needed here to direct focus
        GameWindowFrame gameWindow = new GameWindowFrame(); // Creating object of the game JFrame
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.add(this); // adding this JPanel to the JFrame

        car = new Car(370,505,"RacingCarPurple" ); //Creating player's car, purple by default
        car.setupCar(); // Prompting the player to choose a car color
        sendCar(); // once a car color selected, it will be sent to the server

    }  // End of Client constructor

    public void keyPressed(KeyEvent e) // This will deal with key presses from player
    {
        switch(e.getKeyCode())
        {
            // PLAYER CONTROLS
            case KeyEvent.VK_UP:  // Player pressed Up key. Rotate car anti-clockwise

                if(car.getCurrentCar() == 0)
                {
                    car.setCurrentCar(15);
                }
                else
                {
                    car.setCurrentCar((car.getCurrentCar() - 1) % 16);
                }
            break;

            case KeyEvent.VK_DOWN:  // Player pressed Down key. Rotate car clockwise

                car.setCurrentCar((car.getCurrentCar() + 1) % 16);
                break;

            case KeyEvent.VK_LEFT: // Player pressed Accelerate, increase speed by 1

                if(car.getSpeed() < 100 &&  gameStarted == true)
                {
                    car.increaseSpeed(); // increase speed by 1 ( providing player has not crashed)
                }
                break;

            case KeyEvent.VK_RIGHT: // Player pressed De-accelerate, decrease speed by 1

                if(car.getSpeed() > 0)
                {
                    car.decreaseSpeed();
                }
                break;

            case KeyEvent.VK_Q: // Player pressed Q, quitting the game

                System.exit(0);
                break;

        } // end of switch
    }  // End of keyPressed

    // KeyTyped and KeyReleased also need to be included, even if they are not used for anything
    public void keyTyped(KeyEvent e) { }
    public void keyReleased(KeyEvent e) { }

// This will paint racetrack, trees, arrows and cars (once set-up)
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        new Racetrack(g); // painting the racetrack

        // The trees to the right of the racetrack
        new Tree(g, 820, 550);
        new Tree(g, 820, 450);
        new Tree(g, 820, 350);
        new Tree(g, 820, 250);
        new Tree(g, 820,150);
        new Tree(g, 820, 50);
        // The trees to the left of the racetrack
        new Tree(g, 20, 550);
        new Tree(g, 20, 450);
        new Tree(g, 20, 350);
        new Tree(g, 20, 250);
        new Tree(g, 20,150);
        new Tree(g, 20, 50);

        // The trees at the top
        new Tree(g, 790, 50);
        new Tree(g, 760,50);
        new Tree(g, 730,50);
        new Tree(g, 700, 50);
        new Tree(g, 670, 50);
        new Tree(g, 640,50);
        new Tree(g, 610,50);
        new Tree(g, 580, 50);
        new Tree(g, 550,50);
        new Tree(g, 520,50);
        new Tree(g, 490, 50);
        new Tree(g, 460, 50);
        new Tree(g, 430,50);
        new Tree(g, 400,50);
        new Tree(g, 370, 50);
        new Tree(g, 340,50);
        new Tree(g, 310,50);
        new Tree(g, 280, 50);
        new Tree(g, 250, 50);
        new Tree(g, 220,50);
        new Tree(g, 190,50);
        new Tree(g, 160,50);
        new Tree(g, 130,50);
        new Tree(g, 100,50);
        new Tree(g, 70,50);
        new Arrow(g,500,525);
        new Arrow(g, 500, 575);
        new Arrow(g, 600, 525);
        new Arrow(g, 600, 575);

        // A try-catch is used to handle exceptions when trying to paint cars before they have been set-up
        try {
            car.Cars[car.getCurrentCar()].paintIcon(this, g, car.getXPosition(), car.getYPosition());
            foreignCar.Cars[foreignCar.getCurrentCar()].paintIcon(this, g, foreignCar.getXPosition(), foreignCar.getYPosition());
        }
        catch(Exception e){  // Then the cars have not been set-up yet
           System.out.println(e.getMessage());
            }

    } // End of paintComponent


    public void playSound(File f)
    {
        {
            try
            {
                audio = AudioSystem.getAudioInputStream(f); // entry point for wav file
                Clip clip = AudioSystem.getClip(); // will be used to play audio
                clip.open(audio);
                clip.start(); // playing the audio
            }
            catch (UnsupportedAudioFileException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (LineUnavailableException e)
            {
                e.printStackTrace();
            }
        }
    }

    // Is called once a game has ended, offering the user the chance to play again
    public void playAgainOption()
    {
       int playAgainChoice = JOptionPane.showConfirmDialog(gameOver, "Would you like to play again " + car.getCarColor() + "?", "Game Over", JOptionPane.YES_NO_OPTION);

        if(playAgainChoice == 0) // User selected yes
        {
            replayGame = true; // set true so that in sendCar the client won't again prompt the server to 'identify'
            sendCar();
        }
        else // User selected no
        {
            sendMessage("no_play_again");
            JOptionPane.showMessageDialog(gameOver, "Good Bye!");
            System.exit(0);
        }

    }

  // startAnimation() will be called in Main, this will start the action timer
    public void startAnimation()
    {
        if(animationTimer == null)
        {
            animationTimer = new Timer(ANIMATION_DELAY, new TimerHandler());  // create timer that fires action events
            animationTimer.start();
        } // end of if statement

        else  // then there already is an animation timer. Restart animation
        {
            if( ! animationTimer.isRunning())
            {
                animationTimer.restart();
            }
        } // end of else

    } // end of startAnimation method

// Private inner class, making it possible to process ActionEvents. Will keep checking every interval (1 second)
    private class TimerHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent actionEvent)
        {

            if (gameStarted== true  ) // If both players are set-up
             {
                 car.moveCar();
                 sendCarUpdate();
             }
            else  // Both players are not set-up, Client will keep messaging server to check when can start
            {
                sendMessage("check_game_start");
            }
            // The client will receive message from server and process accordingly
            responseLine = receiveMessage();
            handleServerResponse(responseLine);
            repaint();
        } // End of actionPerformed
    }

// Will merge a series of bytes into a string and then send the output to the Server
    private static void sendMessage(String message) {
        try {
            output.writeBytes(message + "\n");
            output.flush();
            output.reset(); // To avoid using any previous output in any future sendMessages
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

// This will return a string, the message from the Server, and store in 'responseLine'
    private static String receiveMessage()
    {
        try
        {
            return input.readLine();
        }
        catch (Exception e) {
            return null;
        }
    }

    // Used at start of game to receive own Car from Server, As Server needs to set it's starting position
    private static void receiveCar()
    {
        try
        {
            car = (Car) input.readObject();
        } catch (Exception e)
        {
         System.out.println(e.getMessage());
         e.printStackTrace();
        }

    }
   // Will receive opposing player's car position from the Server, in reply to Client's "car_update" message
    private static void receiveCarForeign()
    {
        try {
            foreignCar = (Car) input.readObject();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

// will be called just after car_update message in sendCarUpdate, it will send the Server the current car position
// At start of game sendCar operates differently, in that it informs Server of Car color choice ("identify ")
    private static void sendCar() {
        try {

            if(replayGame == true)  //If users decide on a rematch, this avoids going through "identify" again
            {
              sendMessage("play_again");
            }

            if(gameStarted == false && replayGame == false ) // If this the first game that is being played
            {
                sendMessage("identify " + car.getCarColor()); // informing the server of chosen car color
            }
           else  // This happens whilst a race in progress, sending own car position to server
               {
                output.writeObject(car);
                output.flush();
                output.reset();
               }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Called at every interval, prompting the server for an update and sending own car position
    private static void sendCarUpdate() {
        sendMessage("car_update");
        sendCar();
    }

    private void handleServerResponse(String response)
    {

    // Client receives nothing from Server, most likely due to other client disconnecting & prompting a shutdown
        if(response == null)
        {
            JOptionPane.showMessageDialog(gameOver, "Player has disconnected, aborting session");
            System.exit(0);
        }

        switch (response)
        {
            case "foreign_car_update":  // Receiving opposing player's car to paint on screen

                receiveCarForeign();
                break;

            case "setup_game":  // Used at start of each game to receive own car, set in it's starting position

                receiveCar();
                replayGame = false; // This will stop Client from sending any "play_again" messages to Server
                break;

            case "start_game":   // Server informs players that both players are set-up and the race can start
                gameStarted = true; // this will enable user to move their Car
                car.setHasCrashed(false); // This may have potentially been set to true in previous game
                playEdgeSoundCounter = 0;
                break;

            case "edge_crash":   // Server inform player that he/she collided with inner/outer edge
                if(playEdgeSoundCounter == 0)
                {
                    playSound(collideEdgeSound);
                    playEdgeSoundCounter ++;
                }
                car.setHasCrashed(true);
                car.setSpeed(0);
                break;

            case "cars_collide":   // Server informing players that the cars have collided into each other
                playSound(carCrashSound);
                JOptionPane.showMessageDialog(gameOver, "The cars have collided! The game is over");
                gameStarted = false;
                playAgainOption();
                break;

            case "both_crashed": // Server informing players that they have collided with the edge of racetrack

                JOptionPane.showMessageDialog(gameOver, "Both cars have crashed and cannot move! The game is over");
                gameStarted = false;
                playAgainOption();
                break;

            case "winning_message": // Server informing player that he/she has won

                gameStarted = false;
                replayGame = true;
                playSound(winnerSound);
                JOptionPane.showMessageDialog(gameOver, "You won! Congratulations! " + car.getCarColor());
                playAgainOption();
                break;

            case "losing_message":  // Server informing player that he/she has lost

                gameStarted = false;
                replayGame = true;
                playSound(loserSound);
                JOptionPane.showMessageDialog(gameOver, "You lost, hard luck " + car.getCarColor());
                playAgainOption();
                break;

            case "choose_again": // Server informing 2nd player that he/she has chosen same car color as player 1
                JOptionPane.showMessageDialog(gameOver, "You chosen same car as player 1, please choose again");
                car.setupCar();  // This will reprompt the player to choose a color
                sendCar();
                break;

            case "player_disconnected": // Server informs player that opponent has quit, prompting a shutdown
                JOptionPane.showMessageDialog(gameOver, "Player has disconnected, aborting session");
                System.exit(0);
                break;
        } // end of switch
    } // end of handleServerResponse method


} // End of Client class
