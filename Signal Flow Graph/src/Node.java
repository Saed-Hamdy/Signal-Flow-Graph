import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Node {
    List<Edge> childs;
    Integer lable;
    Point location;

    public Node() {
        childs=new ArrayList<>();
        location =new Point();
    }

    public Node(Integer name ,Point location) {
        lable =name;
        this.location =location  ;      
        childs=new ArrayList<>();
    }
}
