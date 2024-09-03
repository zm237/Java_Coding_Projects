package spiderman;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    public static void main(String[] args) {

        // if ( args.length < 3 ) {
        //     StdOut.println(
        //         "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
        //         return;
        // }

        StdIn.setFile("dimension.in");

        int numberOfDimensions = StdIn.readInt();
        StdIn.readChar();
        int listSize = StdIn.readInt();
        StdIn.readChar();
        int loadFactor = StdIn.readInt();

        HashTable clusters = new HashTable(listSize, loadFactor);

        for (int i = 0; i < numberOfDimensions; i++){
            int dimensionNumber = StdIn.readInt();
            StdIn.readChar();

            int numberOfEvents = StdIn.readInt();
            StdIn.readChar();

            int dimensionWeight = StdIn.readInt();

            DimensionData dataToAdd = new DimensionData(numberOfEvents, dimensionWeight);

            clusters.put(dimensionNumber, dataToAdd, false);
        }

        StdIn.readChar();

        clusters.wrapDimensions();

        StdOut.setFile("clusters.out");

        clusters.printTable();
        
        StdIn.setFile("dimension.in");

        StdIn.readInt(); StdIn.readChar();
        StdIn.readInt(); StdIn.readChar();
        StdIn.readInt();
        
        UDGraph dimensionalWeb = new UDGraph(numberOfDimensions);

        for (int i = 0; i < numberOfDimensions; i++){
            int dimensionNumber = StdIn.readInt();
            StdIn.readChar();

            int numberOfEvents = StdIn.readInt();
            StdIn.readChar();

            int dimensionWeight = StdIn.readInt();

            DimensionData dataToAdd = new DimensionData(numberOfEvents, dimensionWeight);

            Node nodeToAdd = new Node(dimensionNumber, dataToAdd, null);

            dimensionalWeb.put(nodeToAdd, i);
        }

        for (int i = 0; i < clusters.size(); i++){
            int frontIndex = clusters.returnFront(i).getDimNumber();
            int[] neighbors = clusters.returnDimList(frontIndex);

            for (int neighbor : neighbors){
                dimensionalWeb.linkNode(frontIndex, neighbor);
            }
        }

        StdIn.setFile("spiderverse.in");

        int numberOfPeople = StdIn.readInt();
        

        for (int i = 0; i < numberOfPeople; i++){
            int location = StdIn.readInt(); StdIn.readChar();
            String name = StdIn.readString();
            int dimSignature = StdIn.readInt();

            Person personToAdd = new Person(location, name, dimSignature);

            dimensionalWeb.findDimension(location).getData().addPerson(personToAdd);
        }


        StdOut.setFile("collider.out");

        dimensionalWeb.printGraph();

        
    }
}