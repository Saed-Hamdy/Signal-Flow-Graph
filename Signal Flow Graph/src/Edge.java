
public class Edge implements Comparable<Edge> {

    Node destination ;
    Float cost;
    public Edge(Node des,Float cost) {
        this.cost=cost;
        destination=des;
    }
    @Override
    public int compareTo(Edge e) {
        
        return destination.lable-e.destination.lable;
    }
}
