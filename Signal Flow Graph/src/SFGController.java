import java.awt.Point;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SFGController {
    static List<Node> nodes;
    public static List<Path> paths;
    public static List<Path> loops;
    public static List<Integer> delta;
    public static int deltaAll;
    private static HashMap<Path, List<Path>> touchedLoopsTable;

    public SFGController() {
        nodes = new ArrayList<>();
        paths = new ArrayList<>();
        loops = new ArrayList<>();
    }

    /**
     * 
     */
    public void addNode() {
        nodes.add(new Node(nodes.size(), new Point()));
    }

    /**
     * 
     * @param home
     * @param destination
     * @param cost
     */
    public void addEdge(Integer home, Integer destination, int cost) {
        Node des = nodes.get(destination);
        Edge e = new Edge(des, cost);
        nodes.get(home).childs.add(e);
    }

    /**
     * 
     * @param home
     * @param des
     * @return
     */
    public int getTransferFn(int home, int des) {
        paths = dfs(nodes.get(home), nodes.get(des), new ArrayList<Path>());
        System.out.println("loops");
        if(paths.size()==0)
            return 0;
        loops = findLoops();
        System.out.println("loops");
        for (int i = 0; i < loops.size(); i++) {
            for(Integer ii: loops.get(i).path)
                 System.out.print(" "+ii);
           System.out.println("");
        }
       
        createTouchedLoopsTable(loops);
        deltaAll = getDelta(new ArrayList<Integer>());
        delta=new ArrayList<>();
        for(Path p: paths)
           System.out.println( delta.add(getDelta(p.path))); 
        Integer result =0;
        for (int i = 0; i < paths.size(); i++) {
            result+=paths.get(i).cost*delta.get(i);
        }        
        return result/deltaAll;
    }

    private void createTouchedLoopsTable(List<Path> loops) {
        touchedLoopsTable = new HashMap<>();
        for (int i = 0; i < loops.size(); i++) {
            List<Path> touched = new ArrayList<>();
            for (int j = 0; j < loops.size(); j++)
                if (loops.get(i).touched(loops.get(j)))
                    touched.add(loops.get(j));
            touchedLoopsTable.put(loops.get(i), touched);

        }

    }

    // public void findPaths(int home, int des) {
    // dfs( nodes.get(home), nodes.get(des),new ArrayList<Path>());
    // }
    private List<Path> findLoops() {
        List<Path> l = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++)
            dfs(nodes.get(i), nodes.get(i), l);
        for (int i = 0; i < l.size(); i++)
            for (int j = i + 1; j < l.size(); j++) {
                if (l.get(i).CompareLoops(l.get(j))) // remove same Loops
                    l.remove(j--);
            }

        return l;

    }

    public Integer getDelta(List<Integer> exp) {
        int del = 1;
        List<Path> ll = new ArrayList<>(loops);
        for (int node : exp)
            for (int i = 0; i < ll.size(); i++)
                if (ll.get(i).path.contains(node))
                    ll.remove(i--);
        for (Path i : ll)
            del -= i.cost;
        int sign = 1;
        for (int i = 2; i < ll.size(); i++) { // get combination of all sizes
            List<Path> un = findUnTouchedLoops(ll, i);
            if (un.size() == 0)
                break;
            for (Path p : un)
                del += (sign * p.cost);
            sign *= -1;
        }

        return del;

    }

    /**
     * 
     * @param exeptions
     * @return
     */
    private List<Path> findUnTouchedLoops(List<Path> loops, int length) {
       List<Path>unTouched=new ArrayList<>();
       combinations2(loops, length, 0, new Integer[length], unTouched);
        return unTouched;

    }

    static void combinations2(List<Path> loops, int len, int startPosition, Integer[] result, List<Path> unTouched) {
        if (len == 0) {
            int cost=1;
            for (int i = 0; i < result.length; i++) {
                cost*=loops.get(result[i]).cost;
            }
            unTouched.add(new Path(Arrays.asList(result), cost));          
            System.out.println(Arrays.toString(result));
            return;
        }
        for (int i = startPosition; i <= loops.size() - len; i++) {
            Boolean flag = true;
            for (int j = 0; j < result.length - len; j++) {
                if (touchedLoopsTable.get(loops.get(result[j])).contains(loops.get(i)))
                    flag = false;
            }
            if (flag) {
                result[result.length - len] = i;
                combinations2(loops, len - 1, i + 1, result, unTouched);
            }
        }
    }
    /**
     * 
     * @param visited
     * @param v
     * @param d
     * @param path
     * @param cost
     * @param paths
     */
    private static void dfs_rec(boolean[] visited, Edge v, Node d, List<Integer> path, int cost, List<Path> paths) {
        path.add(v.destination.lable);
        cost *= v.cost;

        if (v.destination == d) {
            paths.add(new Path(path, cost));
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i) + " ");
            }
            System.out.println("  cost = " + cost);
        } else if (!visited[v.destination.lable]) { // added here to support
                                                    // self loops(not only 1
                                                    // length)
            visited[v.destination.lable] = true;
            for (Edge w : v.destination.childs) {
                // if (!visited[w.destination.lable]) {
                dfs_rec(visited, w, d, path, cost, paths);
                // }

            }
        }
        path.remove(path.size() - 1);
        cost /= v.cost;
       // visited[v.destination.lable] = false;
    }

    public static List<Path> dfs(Node s, Node d, List<Path> paths) {
        int n = nodes.size();
        boolean[] visited = new boolean[n];

        List<Integer> path = new ArrayList<Integer>();
        visited[s.lable] = true;
        path.add(s.lable);
        int cost = 1;

        for (Edge w : s.childs)
            dfs_rec(visited, w, d, path, cost, paths);
        // int path_index = 0; // Initialize path[] as empty
        // dfs_rec(nodes, visited, new Edge(s, 1), d, path, cost);
        return paths;
    }

    public static void main(String[] args) {
        SFGController sf = new SFGController();

        sf.addNode();
        sf.addNode();
        sf.addNode();
        sf.addNode();
        sf.addNode();
        sf.addNode();
        sf.addEdge(0, 1, 10);
        sf.addEdge(1, 2, 10);
        sf.addEdge(2, 3, 10);
        sf.addEdge(3, 4, 10);
        sf.addEdge(4, 5, 10);
        sf.addEdge(1, 3, 5);
        sf.addEdge(4, 2, -9);
        sf.addEdge(2, 5, 5);
        sf.addEdge(2, 2, -33);
        sf.addEdge(2, 1, 10);
        sf.addEdge(4, 3, 10);
        sf.getTransferFn(0, 2);
 //        sf.addEdge(2, 3, 10);
        // sf.addEdge(3, 4, 10);
        // sf.addEdge(4, 5, 10);

    }
}