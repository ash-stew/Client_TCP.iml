import java.awt.*;

public class Tree
{

    public Tree(Graphics g, int x, int y) {  // Tree constructor

        // tree bark
        Color c1 = Color.gray;
        g.setColor(c1);
        g.fillRect(x, y, 10, 30);   // trees, x and y location followed by width and height

        // tree leaves
        Color c2 = Color.green;
        g.setColor(c2);
        // x left, top and right.  y left, top and right co-ordinates
        g.fillPolygon(new int[]{x - 5, x + 5, x + 15}, new int[]{y, y - 20, y}, 3);

    } // End of Tree constructor

} // End of Tree class