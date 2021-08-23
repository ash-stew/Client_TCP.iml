import javax.swing.*;
import java.awt.*;

public class Racetrack extends JFrame {

    public Racetrack(Graphics g) {  // Racetrack Constructor

        Color c1 = Color.green;
        g.setColor(c1);
        g.fillRect(150, 200, 550, 300);  // grass

        Color c2 = Color.black;
        g.setColor(c2);
        g.drawRect(50, 100, 750, 500); // outer edge
        g.drawRect(150, 200, 550, 300); // inner edge

        Color c3 = Color.yellow;
        g.setColor(c3);
        g.drawRect(100, 150, 650, 400); // mid-lane marker

        Color c4 = Color.white;
        g.setColor(c4);
        g.drawLine(425, 500, 425, 600); // start line

    } // End of Racetrack constructor

} // End of Racetrack class