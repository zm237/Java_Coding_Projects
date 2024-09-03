package spiderman;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

public class HashTable {
    private LL[] clusters;
    private int loadFactor;
    private int numAdded;

    public HashTable(int initialSize, int loadFactor){
        clusters = new LL[initialSize];
        this.loadFactor = loadFactor;
        numAdded = 0;
    }

    public void rehash(){
        Queue<Node> queue = new LinkedList<>();

        for (LL list : clusters){
            Node ptr = list.getfront();
            while (ptr != null){
                queue.add(ptr);
                ptr = ptr.getNext();
            }
        }

        int numAddedNow = numAdded;
        int newSize = clusters.length*2;
        clusters = new LL[newSize];

        for (Node node : queue){
            put(node.getDimNumber(), node.getData(), true);
        }

        numAdded = numAddedNow;
    }

    public void put(int key, DimensionData dimensionData, boolean rehashing){
        Node node = new Node(key, dimensionData, null);
        double check = (double)numAdded / clusters.length;

        if (!rehashing && check >= loadFactor){rehash();}

        int index = key % clusters.length;

        if (clusters[index] == null){
            LL addLL = new LL(node);
            clusters[index] = addLL;
        }
        else {
            clusters[index].addToFront(node);
        }

        numAdded++;
    }

    public void printTable(){
        for (LL list : clusters){
            if (list == null) continue;
            list.dimSet(); StdOut.print("\n");
        }
    }

    public void wrapDimensions(){
        for (int i = 0; i < clusters.length; i++){
            Node ptr = clusters[i].getfront();
            while (ptr.getNext() != null){ptr = ptr.getNext();}
            
            if (i == 0){
                Node behindOne = clusters[clusters.length - 1].getfront().copy();
                Node behindTwo = clusters[clusters.length - 2].getfront().copy();
                behindOne.setNext(behindTwo);
                ptr.setNext(behindOne);               
            }
            else if (i == 1){
                Node behindOne = clusters[0].getfront().copy();
                Node behindTwo = clusters[clusters.length - 1].getfront().copy();
                behindOne.setNext(behindTwo);
                ptr.setNext(behindOne);  
            }
            else {
                Node behindOne = clusters[i - 1].getfront().copy();
                Node behindTwo = clusters[i - 2].getfront().copy();
                behindOne.setNext(behindTwo);
                ptr.setNext(behindOne);  
            }
        }
    }

    public int[] returnDimList(int dimNumber){
        ArrayList<Integer> dimList = new ArrayList<>();
        int index = dimNumber % clusters.length;
        Node ptr = clusters[index].getfront();

        while (ptr != null){
            if (ptr.getDimNumber() != dimNumber){
            int check = ptr.getDimNumber();
                dimList.add(check);
            }
            ptr = ptr.getNext();
        }


        int[] dimArray = new int[dimList.size()];

        for (int i = 0; i < dimArray.length; i++){
            dimArray[i] = dimList.get(i);
        }

        return dimArray;
    }

    public int size(){return clusters.length;}

    public int totalDim(){return numAdded;}

    public Node returnFront(int index){
        return clusters[index].getfront();
    }

    public LL returnList(int index){return clusters[index];}

    public Node getDimension(int dimensionNumber){
        for (LL list : clusters){
            Node nodeToFind = list.returnNode(dimensionNumber);
            if (nodeToFind == null) continue;
            if (nodeToFind.getDimNumber() == dimensionNumber) return nodeToFind;
        }
        
        return null;
    }
}
