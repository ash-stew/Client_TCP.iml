import java.awt.*;

public class Arrow {

    public Arrow(Graphics g, int x, int y) // Arrow constructor
    {
        Color c1 = Color.CYAN;
        g.setColor(c1);
        g.fillRect(x, y, 50, 10);   // arrows
        g.fillRect(x, y, 50, 10);

        g.fillPolygon(new int[]{x+50,x+50,x+60}, new int[]{y+15,y-5,y+5}, 3 );
        g.fillPolygon(new int[]{x+50,x+50,x+60}, new int[]{y+15,y-5,y+5}, 3 );

    } // end of Arrow constructor

} // // end of Arrow class
