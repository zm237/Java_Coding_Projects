package spiderman;

import java.util.ArrayList;
import java.util.HashMap;

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
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {
    
    public static void main(String[] args) {

        // if ( args.length < 5 ) {
        //     StdOut.println(
        //         "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
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

        StdIn.readChar();

        clusters.wrapDimensions();

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

        StdIn.setFile("hub.in");

        int hub = StdIn.readInt();

        StdIn.setFile("anomalies.in");

        int numbOfAnomalies = StdIn.readInt(); StdIn.readChar();
        HashMap<Person, Integer> anomalies = new HashMap<>();
        ArrayList<Person> anomaliesList = new ArrayList<>();

        for (int i = 0; i < numbOfAnomalies ; i++){
            String anomName = StdIn.readString(); StdIn.readChar();
            int 時間 = StdIn.readInt();

            Person person = dimensionalWeb.findPerson(anomName);

            assert(person != null);

            anomalies.put(person, 時間);
            anomaliesList.add(person);
        }

        Node hubDim = dimensionalWeb.findDimension(hub);

        StdOut.setFile("GoHome.out");

        dimensionalWeb.dijkstra(hubDim, hubDim.getData().getDimWeight(), anomalies, anomaliesList);


        
    }
}
