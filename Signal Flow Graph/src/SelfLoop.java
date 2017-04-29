
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class SelfLoop {
	Point position = new Point(0, 0);
	int radius=40;
	double w;
	public SelfLoop(double w, Point p){
		position=p;
		this.w=w;}
	public void draw(Graphics g) {
		int x1=position.x ;
		int y1=position.y;
		int r=radius;
        g.setColor(Color.green);
        g.drawOval(x1,y1,r,r);
        String s = Double.toString(w);
        g.drawString(s, x1+45, y1+25);
	}
}