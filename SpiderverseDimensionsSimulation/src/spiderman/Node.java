package spiderman;

public class Node {
    
    private int dimNumber;
    private DimensionData data;
    private Node next;
    
    public Node(int dimNumber, DimensionData data, Node next){
        this.dimNumber = dimNumber;
        this.data = data;
        this.next = next;
    }
    
    public void setdimNumber(int dimNumberToAdd){
        dimNumber = dimNumberToAdd;
    }

    public void setNext(Node node){
        next = node;
    }

    public void setData(DimensionData data){
        this.data = data;
    }

    public Node getNext(){
        return next;
    }

    public int getDimNumber(){
        return dimNumber;
    }

    public DimensionData getData(){
        return data;
    }

    public Node copy(){
        int dimensionNumber = dimNumber;
        DimensionData data = new DimensionData(dimNumber, dimNumber);

        Node copyNode = new Node(dimensionNumber, data, null);
        
        return copyNode;
    }
}
