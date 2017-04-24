import java.util.ArrayList;
import java.util.List;

public class Path {
    List<Integer> path;
    Float cost;

    public Path(List<Integer> path, Float cost) {
        this.path = new ArrayList<>(path);
        this.cost = cost;
    }

    /**
     * now it is correct
     * 
     * @param c
     * @return
     */
    public Boolean CompareLoops(Path c) {
        if (c.path.size() == path.size() && c.cost.equals(cost)) {
            for (int i = 0; i < c.path.size() - 1; i++) {
                if (path.contains(c.path.get(i))) {
                    if (!(path.get(path.indexOf(c.path.get(i)) + 1) == c.path.get(i == path.size() - 1 ? 0 : i + 1)))
                        return false;
                } else
                    return false;
            }
            return true;
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
