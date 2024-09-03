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
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }

        StdIn.setFile(args[0]);

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

        StdIn.setFile(args[0]);

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

        StdIn.setFile(args[1]);

        int numberOfPeople = StdIn.readInt();
        
        for (int i = 0; i < numberOfPeople; i++){
            int location = StdIn.readInt(); StdIn.readChar();
            String name = StdIn.readString();
            int dimSignature = StdIn.readInt();

            Person personToAdd = new Person(location, name, dimSignature);

            dimensionalWeb.findDimension(location).getData().addPerson(personToAdd);
        }

        StdIn.setFile(args[2]);

        int hub = StdIn.readInt();

        StdOut.setFile(args[3]);
        
        dimensionalWeb.bfs(hub, loadFactor);

        
                             

    }
}
