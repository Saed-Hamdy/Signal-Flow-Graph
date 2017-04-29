
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;

public class Arc {
    int x1, x2, y1, y2;
    double x0, y0, w;
    QuadCurve2D curve;
    boolean flag;

    public Arc(Point p1, Point p2, double w, boolean flag) {
        x1 = p1.x;
        x2 = p2.x;
        y1 = p1.y;
        y2 = p2.y;
        x0 = (x1 + x2) / 2;
        this.flag = flag;
        if (flag)
            y0 = (y1 + y2) / 2 - (Math.abs(x1 - x2)) * 0.3;
        else
            y0 = (y1 + y2) / 2 + (Math.abs(x1 - x2)) * 0.3;
        curve = new QuadCurve2D.Double(x1, y1, x0, y0, x2, y2);
        this.w = w;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.draw(curve);
        String s = Double.toString(w);
        if (flag)
            y0 = (y1 + y2) / 2 - (Math.abs(x1 - x2)) * 0.15;
        else
            y0 = (y1 + y2) / 2 + (Math.abs(x1 - x2)) * 0.15;
        g.drawString(s, (int) x0, (int) y0);
        ArrayList<Point> points = pointsOnCurve();
        Point startPoint = points.get((int) (points.size() * 0.66666));
        Point endPoint = points.get((int) ((points.size() * 0.66666) + 1));
        int i = 0;
        while (startPoint.distance(endPoint) < 15) {
            try {
                endPoint = points.get((int) ((points.size() * 0.66666) + 1 + i));
                i++;
            } catch (Exception e) {
                break;
            }
        }
        int[] x = new int[3];
        int[] y = new int[3];
        double distance = startPoint.distance(endPoint);
        double halfDistance = distance / 2;
        double angle = -Math.atan2(endPoint.y - startPoint.y, endPoint.x - startPoint.x);
        x[0] = (int) endPoint.getX();
        y[0] = (int) endPoint.getY();
        x[1] = (int) ((Math.sin(angle) * halfDistance) + startPoint.getX());
        y[1] = (int) ((Math.cos(angle) * halfDistance) + startPoint.getY());
        x[2] = (int) (startPoint.getX() - (Math.sin(angle) * halfDistance));
        y[2] = (int) (startPoint.getY() - (Math.cos(angle) * halfDistance));
        g2d.fillPolygon(x, y, 3);
    }

    public ArrayList<Point> pointsOnCurve() {
        FlatteningPathIterator iter;
        ArrayList<Point> points;
        iter = new FlatteningPathIterator(curve.getPathIterator(new AffineTransform()), 0.01);
        points = new ArrayList<Point>();
        float[] coords = new float[6];
        while (!iter.isDone()) {
            iter.currentSegment(coords);
            int x = (int) coords[0];
            int y = (int) coords[1];
            points.add(new Point(x, y));
            iter.next();
        }
        return points;
    }
}