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
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {
    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
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

        clusters.wrapDimensions();

        StdOut.setFile(args[1]);

        clusters.printTable();

    }
}
