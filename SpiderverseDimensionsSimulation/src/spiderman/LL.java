package spiderman;

import java.util.ArrayList;

public class LL {
    private Node front;

    public LL(Node front){
        this.front = front;
    }

    public void setfront(Node node){
        front = node;
    }

    public Node getfront(){
        return front;
    }

    public void addToFront(Node node){
        if (front == null){setfront(node);}
        else{
            node.setNext(front);
            setfront(node);
        }
    }

    public void addToBack(Node node){
        if (front == null) setfront(node);
        else {
            Node ptr = front;
            while (ptr.getNext() != null){ptr = ptr.getNext();}
            ptr.setNext(node);
        }
    }

    public void dimSet(){
        Node ptr = front;
        while (ptr != null){
            StdOut.print(ptr.getDimNumber() + " ");
            ptr = ptr.getNext();
        }
    }

    public ArrayList<Integer> returnSet(){
        Node ptr = front;
        ArrayList<Integer> dimList = new ArrayList<>();

        while (ptr != null){
            dimList.add(ptr.getDimNumber());
            ptr = ptr.getNext();
        }

        return dimList;
    }



    public Node returnNode(int dimensionNumber){
        Node ptr = front;
        while (ptr != null){
            if (ptr.getDimNumber() == dimensionNumber){return ptr;}
            ptr = ptr.getNext();
        }
        return null;
    }

    public void addAfterFront(Node node){
        Node ptr = front;

        if (ptr.getNext() == null) ptr.setNext(node);
        else {
            Node ptrAhead = ptr.getNext();
            ptr.setNext(node);
            node.setNext(ptrAhead);
        }
    }

    
}
