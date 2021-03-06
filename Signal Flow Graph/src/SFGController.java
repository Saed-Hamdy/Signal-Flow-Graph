import java.awt.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SFGController {
    public static List<Node> nodes;
    public static List<Path> paths;
    public static List<Path> AllLoops;
    public static List<List<Path>> Loops;    
    public static List<Float> delta;
    public static Float deltaAll;
    public static List<List<Path>> untouchedLoops;
    public String errorMessage,resuts;
    private static HashMap<Path, List<Path>> touchedLoopsTable;
    
    public SFGController() {
        
        nodes = new ArrayList<>();
        paths = new ArrayList<>();
        AllLoops = new ArrayList<>();
        untouchedLoops =new ArrayList<>();
        errorMessage="";
    }

    /**
     * 
     */
    public void addNode() {
        nodes.add(new Node(nodes.size(), new Point()));
    }

    public void deleteNode(int x) {
        errorMessage = "";
        if (x < nodes.size())
            nodes.remove(x);
        else
            errorMessage = new String("the Node u want to delete is out of index ");
    }

    public void deleteEdge(int x, int y) {
        errorMessage = new String("No Edge Like that");
        for (Edge e : nodes.get(x).childs)
            if (e.destination.lable == y) {
                nodes.get(x).childs.remove(e);
                errorMessage = "";
                break;
            }

    }
    public void addCost(Integer x, Integer y, Float cost) {
        for (Edge e : nodes.get(x).childs)
            if (e.destination.lable == y) {
                e.cost+=cost;
                errorMessage = "";
                break;
            }        
    }

    /**
     * 
     * @param home
     * @param destination
     * @param cost
     */
    public void addEdge(Integer home, Integer destination, Float cost) {
        errorMessage = "";
        try {
            Node des = nodes.get(destination);
            Edge e = new Edge(des, cost);
            nodes.get(home).childs.add(e);
        } catch (Exception e) {
            errorMessage = new String("can't make this edge ");
        }
    }

    /**
     * 
     * @param home
     * @param des
     * @return
     */
    public String getTransferFn(int home, int des) {
        System.out.println("in  "+nodes.size());
        if (nodes.size() == 0)
            return "";
        
        paths = dfs(nodes.get(home), nodes.get(des), new ArrayList<Path>());
        if (paths.size() == 0)
            return "";
        
        Loops= new ArrayList<List<Path>>(paths.size());
        untouchedLoops= new ArrayList<List<Path>>(paths.size()+1);
        System.out.println("AllLoops");        
        AllLoops = findLoops();
        System.out.println("loops");
        for (int i = 0; i < AllLoops.size(); i++) {
            for (Integer ii : AllLoops.get(i).path)
                System.out.print(" " + ii);
            System.out.println(" cost :" + AllLoops.get(i).cost);
        }

        createTouchedLoopsTable(AllLoops);
        deltaAll = getDelta(new ArrayList<Integer>(),0);
        delta = new ArrayList<>();
        for (int i=0;i<paths.size();i++)
            System.out.println(delta.add(getDelta(paths.get(i).path,i+1)));
        Float result = new Float(0);
        for (int i = 0; i < paths.size(); i++) {
            result += paths.get(i).cost * delta.get(i);
        }
        for (List<Path> p : untouchedLoops) {
            for (int i = 0; i < p.size(); i++) {
                for (int j : p.get(i).path)
                    System.out.print(j);
                System.out.print(" ");
            }
            System.out.println("");
        }
        setResults(result / deltaAll);
        return String.valueOf(result / deltaAll);

    }

    private void setResults(float f) {
        String s=" Transfer function Value   = "+f+"\n\n";
        s+=" Paths :-\n";
        for(int i=1;i<=paths.size();i++){
            s+="    Path "+i+" )";
            for(Integer intg :paths.get(i-1).path)
                s+="  "+intg;
            s+="\n";
        }
        s+="\n AllLoops :- \n";
        for(int i=1;i<=AllLoops.size();i++){
            s+="    Loop "+i+" )";
            for(Integer intg :AllLoops.get(i-1).path)
                s+="  "+intg;
            s+="\n";
        }
        s+=" \n UnTouched loops :-\n";
        
            int size=1;
            for (Path p:untouchedLoops.get(0)){
                if(p.path.size()>size){
                    size++;
                    s=s.substring(0,s.length()-2);
                    s+="\n    Size "+size+" )";
                }
                for(int in :p.path)
                    s+=" "+in;
                s+=" ,";
            }  
           s=s.substring(0,s.length()-2)+"\n";  
         s+="\n Paths Loop :- \n";
         for(int i=1;i<Loops.size();i++){
             s+="     After Delete Path ("+i+") :-\n           ";
             s+="Loops           :-  ";
             for(Path p:Loops.get(i)){
                 s+=p.name+" , ";
             }
             s=s.substring(0,s.length()-2)+"\n           ";
             s+="UnTouched loops :-  ";
             size=1;
             for (Path p:untouchedLoops.get(i)){
                 if(p.path.size()>size){
                     size++;
                     s=s.substring(0,s.length()-2);
                     s+="\n    Size "+size+" )";
                 }
                 for(int in :p.path)
                     s+=" "+in;
                 s+=" ,";
             } 
             s=s.substring(0,s.length()-2)+"\n";
         }
         s+="\n\n DeltaAll  :- "+deltaAll+"\n";
         for(int i=0;i<paths.size();i++){
             s+=" Delta ("+(i+1)+") :- "+delta.get(i)+"\n";
         }
         
        resuts=s;
        System.out.println(s);
        
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
        for (int i = 0; i < l.size(); i++) {
            l.get(i).name=i+1;
            for (int j = i + 1; j < l.size(); j++) {
                if (l.get(i).CompareLoops(l.get(j))) // remove same Loops
                    l.remove(j--);
            }
        }
        return l;

    }

    public Float getDelta(List<Integer> exp,int index) {
        untouchedLoops.add(new ArrayList<Path>());
        Loops.add(new ArrayList<Path>());
        Float del = new Float(1);
        List<Path> ll = new ArrayList<>(AllLoops);
        for (int node : exp)
            for (int i = 0; i < ll.size(); i++)
                if (ll.get(i).path.contains(node))
                    ll.remove(i--);
        
        Loops.get(index).addAll(new ArrayList<>(ll));
        for (Path i : ll)
            del -= i.cost;
        int sign = 1;
        for (int i = 2; i < ll.size(); i++) { // get combination of all sizes
            List<Path> un = findUnTouchedLoops(ll, i);
            untouchedLoops.get(untouchedLoops.size() - 1).addAll(new ArrayList<>(un));

            if (un.size() == 0)
                break;
            for (Path p : un)
                del += (sign * p.cost);
            sign *= -1;
        }
        System.out.println(del);
        return del;

    }

    /**
     * 
     * @param exeptions
     * @return
     */
    private List<Path> findUnTouchedLoops(List<Path> loops, int length) {
        List<Path> unTouched = new ArrayList<>();
        combinations2(loops, length, 0, new Integer[length], unTouched);
        return unTouched;

    }

    private static void combinations2(List<Path> loops, int len, int startPosition, Integer[] result, List<Path> unTouched) {
        if (len == 0) {
            Float cost = new Float(1);
            for (int i = 0; i < result.length; i++) {
                cost *= loops.get(result[i]).cost;
            }
            
            for (int i = 0; i < result.length; i++) {
                result[i]=loops.get(result[i]).name;
            }
            unTouched.add(new Path(Arrays.asList(result), cost));
            System.out.println(Arrays.toString(result));
            return;
        }
        for (int i = startPosition; i <= loops.size() - len; i++) {
            Boolean flag = true;
            for (int j = 0; j < result.length - len&&flag; j++) {
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
    private static void dfs_rec(boolean[] visited, Edge v, Node d, List<Integer> path, Float cost, List<Path> paths) {
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
            for (Edge w : v.destination.childs)
                dfs_rec(visited, w, d, path, cost, paths);

            visited[v.destination.lable] = false;
        }
        path.remove(path.size() - 1);
        cost /= v.cost;
    }

    public static List<Path> dfs(Node s, Node d, List<Path> paths) {
        int n = nodes.size();
        boolean[] visited = new boolean[n];

        List<Integer> path = new ArrayList<Integer>();
        visited[s.lable] = true;
        path.add(s.lable);
        Float cost = new Float(1);

        for (Edge w : s.childs)
            dfs_rec(visited, w, d, path, cost, paths);
        return paths;
    }

    public static void main(String[] args) {
        SFGController sf = new SFGController();
        // System.out.println( "hmndbn");
        // Scanner in =new Scanner(System.in);
        // in.nextLine();
        sf.addNode();
        sf.addNode();
        sf.addNode();
        sf.addNode();
        sf.addNode();
        sf.addNode();
        // sf.addNode();
        // sf.addNode();
        // sf.addNode();
        // sf.addNode();
        // sf.addNode();
        sf.addEdge(0, 1, new Float(1));
        sf.addEdge(1, 2, new Float(1));
        sf.addEdge(2, 3, new Float(1));
        sf.addEdge(3, 4, new Float(1));
        sf.addEdge(4, 5, new Float(1));
        sf.addEdge(4, 3, new Float(-1));
        sf.addEdge(2, 1, new Float(-1));
        sf.addEdge(4, 1, new Float(-1));
        sf.addEdge(1, 3, new Float(1));
        sf.addEdge(1, 5, new Float(1));
        sf.addEdge(5, 1, new Float(-1));
        
        // sf.addEdge(1, 3, 5);
        // sf.addEdge(4, 2, -9);
        // sf.addEdge(2, 5, 5);
        // sf.addEdge(2, 2, -33);
        // sf.addEdge(2, 1, 10);
        // sf.addEdge(4, 3, 10);
        System.out.println("transfer fn : " + sf.getTransferFn(0, 5));
        // sf.addEdge(2, 3, 10);
        // sf.addEdge(3, 4, 10);
        // sf.addEdge(4, 5, 10);

    }

    
}