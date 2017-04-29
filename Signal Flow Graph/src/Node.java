import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Node {
    List<Edge> childs;
    Integer lable;
    Point position ;
    int radius;
    

    public Node() {
        childs=new ArrayList<>();
        position =new Point(0,0);
    }

    public Node(Integer name ,Point location) {
        lable =name;
        position =location  ;      
        childs=new ArrayList<>();
    }
    public void draw(Graphics g) {
        int x=position.x - radius/2;
        int y=position.y  - radius/2;
        int r=radius;
        g.setColor(Color.orange);
        g.fillOval(x,y,r,r);
        String s = "node " + lable;
        g.setColor(Color.DARK_GRAY);
        g.drawString(s, x, y);
    }
    public boolean isAt(Point point){
        int x=position.x - radius/2;
        int y=position.y  - radius/2;
        return Math.pow((point.getX() - x), 2) +
                Math.pow((point.getY() - y), 2) < radius*radius;
    }
}
