import java.util.ArrayList;
import java.util.List;

public class Path {
    List<Integer> path;
    Integer cost;

    public Path(List<Integer> path, Integer cost) {
        this.path = new ArrayList<>(path);
        this.cost = cost;
    }

    /**
     * this should be change to check the sequence
     * 
     * @param c
     * @return
     */
    public Boolean CompareLoops(Path c) {
        if (c.path.size() == path.size()) {
            for (int i : c.path) {
                if (!path.contains(i))
                    return false;
            }
            return cost == c.cost;
        }
        return false;

    }

    public Boolean touched(Path p2) {
        for (int i : p2.path)
            if (path.contains(i))
                return true;
        return false;
    }
}
