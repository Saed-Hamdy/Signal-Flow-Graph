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
     * this should be change to check the sequence
     * 
     * @param c
     * @return
     */
    public Boolean CompareLoops(Path c) {
        if (c.path.size() == path.size()) {

            for (int i = 0; i < c.path.size() - 1; i++) {

                if (path.contains(c.path.get(i))) {
//                    System.out.println((path.indexOf(c.path.get(i))+1));
//                    System.out.println(c.path.get(i == path.size() - 1 ? 0 : i+1));
                    if (!(path.get(path.indexOf(c.path.get(i)) + 1) == c.path.get(i == path.size() - 1 ? 0 : i+1)))
                        return false;
                }
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
