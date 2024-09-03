package spiderman;

import java.util.*;

public class UDGraph {

    // Main Object of Graph
    private LL[] dimensionWeb;

    // Objects for dfs and bfs
    private HashMap<Integer, Boolean> marked;
    private HashMap<Integer, Integer> edgeTo;
    private HashMap<Integer, Integer> distTo;
    private ArrayList<Integer> pathTrace;
    private Queue<Integer> q;

    public UDGraph(int numberOfDim) {
        dimensionWeb = new LL[numberOfDim];
    }

    public void initializeTables(int loadFactor) {
        marked = new HashMap<>(1, loadFactor);
        edgeTo = new HashMap<>(1, loadFactor);
        distTo = new HashMap<>(1, loadFactor);
        pathTrace = new ArrayList<>();
        q = new LinkedList<>();

        for (LL list : dimensionWeb) {
            ArrayList<Integer> dimList = list.returnSet();
            for (int dim : dimList) {
                marked.put(dim, false);
            }
        }

        for (LL list : dimensionWeb) {
            ArrayList<Integer> dimList = list.returnSet();
            for (int dim : dimList) {
                distTo.put(dim, 0);
            }
        }

    }

    public void put(Node node, int index) {
        int dimNumber = node.getDimNumber();
        int numberOfEvents = node.getData().getNumOfEvents();
        int dimWeight = node.getData().getDimWeight();
        ArrayList<Person> personList = new ArrayList<>();

        for (Person person : node.getData().getPeopleList()) {
            personList.add(person.copy());
        }

        DimensionData data = new DimensionData(numberOfEvents, dimWeight);

        Node nodeToAdd = new Node(dimNumber, data, null);

        if (dimensionWeb[index] == null) {
            LL list = new LL(nodeToAdd);
            dimensionWeb[index] = list;
        } else {
            dimensionWeb[index].addToBack(nodeToAdd);
        }
    }

    public void putAfterFront(Node node, int index) {
        int dimNumber = node.getDimNumber();
        int numberOfEvents = node.getData().getNumOfEvents();
        int dimWeight = node.getData().getDimWeight();

        DimensionData data = new DimensionData(numberOfEvents, dimWeight);

        Node nodeToAdd = new Node(dimNumber, data, null);

        dimensionWeb[index].addAfterFront(nodeToAdd);
    }

    public ArrayList<Integer> adj(int dimNumber) {
        ArrayList<Integer> neighborList = new ArrayList<>();
        int index = findIndex(dimNumber);
        Node ptr = dimensionWeb[index].getfront().getNext();

        while (ptr != null) {
            neighborList.add(ptr.getDimNumber());
            ptr = ptr.getNext();
        }

        return neighborList;
    }

    public Node findDimension(int dimNumber) {
        for (LL list : dimensionWeb) {
            Node ptr = list.returnNode(dimNumber);
            if (ptr != null)
                return ptr;
        }
        return null;
    }

    public int findIndex(int dimNumber) {
        for (int i = 0; i < dimensionWeb.length; i++) {
            Node ptr = dimensionWeb[i].getfront();
            if (ptr.getDimNumber() == dimNumber)
                return i;
        }
        return -1;
    }

    public void linkNode(int frontDim, int dimToAdd) {
        Node frontPtr = findDimension(frontDim);
        Node dimPtr = findDimension(dimToAdd);
        int frontIndex = findIndex(frontDim);
        int dimIndex = findIndex(dimToAdd);

        put(dimPtr, frontIndex);
        put(frontPtr, dimIndex);
    }

    public void dfs(int startDim, int tableLoadFactor) {
        initializeTables(tableLoadFactor);
        pathTrace.add(startDim);
        dfs(startDim);
    }

    public void dfs(int vertex) {
        marked.replace(vertex, true);

        for (int w : adj(vertex)) {
            if (!marked.get(w)) {
                edgeTo.put(w, vertex);
                pathTrace.add(w);
                dfs(w);
            }
        }
    }

    public void returnPath(int endDim) {
        for (int dim : pathTrace) {
            if (dim == endDim) {
                StdOut.print(dim);
                break;
            }
            StdOut.print(dim + " ");
        }
    }

    public void bfs(int startDim, int tableLoadFactor) {
        initializeTables(tableLoadFactor);
        q.add(startDim);
        marked.put(startDim, true);
        distTo.put(startDim, 0);

        while (!q.isEmpty()) {
            int v = q.remove();
            for (int w : adj(v)) {
                if (!marked.get(w)) {
                    q.add(w);
                    marked.replace(w, true);
                    edgeTo.put(w, v);
                    distTo.replace(w, distTo.get(w) + 1);

                    ArrayList<Person> anomalyList = new ArrayList<>();
                    Person spider = null;

                    for (Person person : findDimension(w).getData().getPeopleList()) {
                        if (person.getLocation() != person.getSignature()) {
                            anomalyList.add(person);
                        } else if (person.getLocation() == person.getSignature()) {
                            spider = person;
                        }
                    }

                    for (Person anomaly : anomalyList) {
                        if (spider != null) {
                            StdOut.print(anomaly.getName() + " " + spider.getName() + " ");
                            pathBackHome(startDim, w, true);
                            StdOut.print("\n");
                            findPersonDimension(anomaly.getName()).getData().removePerson(anomaly);
                            findDimension(startDim).getData().addPerson(anomaly);
                        } else {
                            StdOut.print(anomaly.getName() + " ");
                            pathBackHome(startDim, w, false);
                            StdOut.print("\n");
                            findPersonDimension(anomaly.getName()).getData().removePerson(anomaly);
                            findDimension(startDim).getData().addPerson(anomaly);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Node> adjNodes(int dimensionNumber) {
        ArrayList<Node> neighbors = new ArrayList<>();
        int index = findIndex(dimensionNumber);
        Node ptr = dimensionWeb[index].getfront();

        while (ptr != null) {
            if (ptr.getDimNumber() != dimensionNumber)
                neighbors.add(ptr);
            ptr = ptr.getNext();
        }

        return neighbors;
    }

    public void dijkstra(Node startDim, int startDimWeight, HashMap<Person, Integer> timeMap,
            ArrayList<Person> anomalies) {
        HashSet<Integer> done = new HashSet<>();
        PriorityQueue<Integer> fringe = new PriorityQueue<>();
        HashMap<Node, Integer> distance = new HashMap<>();
        HashMap<Node, Node> pred = new HashMap<>();

        for (Node dimension : returnAllDim()) {
            if (!dimension.equals(startDim)) {
                distance.put(dimension, Integer.MAX_VALUE);
                pred.put(dimension, null);
            }
        }

        pred.put(startDim, null);
        distance.put(startDim, 0);
        fringe.add(startDim.getDimNumber());

        while (!fringe.isEmpty()) {
            int m = fringe.peek();
            fringe.remove(m);
            done.add(m);
            for (Node dimension : adjNodes(m)) {
                if (done.contains(dimension.getDimNumber()))
                    continue;
                if (distance.get(dimension) == Integer.MAX_VALUE) {
                    distance.replace(dimension, distance.get(findDimension(m))
                            + dimension.getData().getDimWeight());
                    fringe.add(dimension.getDimNumber());
                    pred.replace(dimension, findDimension(m));
                } else if (distance.get(dimension) > distance.get(findDimension(m))
                        + dimension.getData().getDimWeight()) {
                    distance.replace(dimension, distance.get(findDimension(m))
                            + dimension.getData().getDimWeight());
                    pred.replace(dimension, findDimension(m));
                }
            }
        }

        // for (Person anomaly : anomalies){
        // int time = timeMap.get(anomaly);
        // successOrFail(anomaly, distance.get(findDimension(anomaly.getSignature())),
        // time, pred, startDim);
        // }

        for (Node key : pred.keySet()) {
            if (key != null && pred.get(key) != null)
                System.out.println("" + key.getDimNumber() + ": " + pred.get(key).getDimNumber());
        }

    }

    public void successOrFail(Person anomaly, int distance, int time, HashMap<Node, Node> pred, Node startNode) {
        int canonEvents = findPersonDimension(anomaly.getName()).getData().getNumOfEvents();
        boolean truthValue = true;
        Stack<Integer> path = new Stack<>();
        Node ptr = findDimension(anomaly.getSignature());

        if (distance > time) {
            canonEvents--;
            truthValue = false;
        }

        while (pred.get(ptr) != null) {
            int dimensionNumber = ptr.getDimNumber();
            path.push(dimensionNumber);
            ptr = pred.get(ptr);
        }

        path.push(ptr.getDimNumber());

        if (truthValue) {
            StdOut.print(canonEvents + " " + anomaly.getName() + " " + "SUCCESS" + " ");
            while (!path.isEmpty()) {
                StdOut.print(path.pop() + " ");
            }
            StdOut.print("\n");
        } else {
            StdOut.print(canonEvents + " " + anomaly.getName() + " " + "FAILED" + " ");
            while (!path.isEmpty()) {
                StdOut.print(path.pop() + " ");
            }
            StdOut.print("\n");
        }

    }

    public Stack<Integer> returnPath(int startLocation, int finalLocation) {
        Stack<Integer> path = new Stack<>();
        int location = finalLocation;

        while (edgeTo.get(location) != startLocation) {
            path.push(edgeTo.get(location));
            location = edgeTo.get(location);
        }

        path.push(edgeTo.get(location));

        return path;
    }

    public void pathBackHome(int hubDimension, int anomDimension, boolean isSpider) {
        Stack<Integer> pathFromHub = returnPath(hubDimension, anomDimension);
        Stack<Integer> pathToHub = new Stack<>();

        while (!pathFromHub.isEmpty()) {
            int dim = pathFromHub.pop();
            pathToHub.push(dim);
            if (!isSpider)
                StdOut.print(dim + " ");
        }

        StdOut.print(anomDimension + " ");

        while (!pathToHub.isEmpty()) {
            StdOut.print(pathToHub.pop() + " ");
        }
    }

    public Node findPersonDimension(String name) {

        for (int i = 0; i < dimensionWeb.length; i++) {
            Node ptr = dimensionWeb[i].getfront();

            while (ptr != null) {
                for (Person personInList : ptr.getData().getPeopleList()) {
                    if (name.equals(personInList.getName()))
                        return ptr;
                }
                ptr = ptr.getNext();
            }
        }
        return null;
    }

    public Person findPerson(String name) {
        Person person = null;

        for (LL list : dimensionWeb) {
            Node ptr = list.getfront();
            while (ptr != null) {
                for (Person individual : ptr.getData().getPeopleList()) {
                    if (individual.getName().equals(name))
                        return individual;
                }
                ptr = ptr.getNext();
            }
        }

        return person;
    }

    public void printGraph() {
        for (LL list : dimensionWeb) {
            if (list != null) {
                list.dimSet();
                StdOut.print("\n");
            }
        }
    }

    public ArrayList<Node> returnAllDim() {
        ArrayList<Node> dimensions = new ArrayList<>();

        for (LL list : dimensionWeb) {
            Node ptr = list.getfront();

            while (ptr != null) {
                dimensions.add(ptr);
                ptr = ptr.getNext();
            }
        }

        return dimensions;
    }

    public int size() {
        return dimensionWeb.length;
    }

}
