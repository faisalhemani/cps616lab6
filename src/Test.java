package Warshall;
import java.util.Scanner;

/**
 * Main test program
 * offers the ability to test and profile randomly generated graphs
 * as well as graphs read from Scanner
 * @author Sophie Quigley
 * <br>You can modify this program to perform your tests
 */
public class Test {
    /**
     * Graph being tested and profiled
     */
    private static Graph graph;
    /**
     * Input stream to read graphs
     */
    private static Scanner in = new Scanner(System.in);
    /**
     * Main test program either calls profile, testInput, or both.
     * @param args the command line arguments
     */     
    public static void main(String[] args) throws CloneNotSupportedException {
        profile(10);
        //testInput();
    }
   /**
    * Loop profiling the performance of the three versions of Warshall
    * on increasingly large randomly generated graphs of specified density
    * @param density density of the graphs generated (see Graph constructor) 
    */ 
    private static void profile(int density) throws CloneNotSupportedException {
        System.out.println("n\tWarshall1\tWarshall2\tWarshall3");
        for (int size=16; size<=2048; size+=16) {
            graph = new Graph(size,density);
            profileGraph(false);
        }        
    }
    
    /**
    * Loop reading graphs from scanner and timing the performance of the 
    * three Warshall algorithms for these graphs.
    */ 
    private static void testInput() throws CloneNotSupportedException {
        while (in.hasNext())    {
            graph = new Graph(in);
            profileGraph(true);
        }

    }
    
    /**
     * Times the three versions of Warshall's algorithm;
     * @param showresults  if true, also displays all the graphs
     */
    private static void profileGraph(boolean showresults) throws CloneNotSupportedException{
        long time1, time2, time3;
        
        if (showresults) {
            System.out.println("Graph has " + graph.getVertices() 
                + " vertices, and " + graph.getEdges() + " edges.");
            System.out.print(graph);
        }
        
        Graph graphclone = graph.clone();
        time1 = System.nanoTime();       
        graphclone.Warshall1();
        time1 = System.nanoTime() - time1;
        if (showresults) {        
            System.out.println("Warshall's Algorithm - " + graphclone.getEdges() + " edges:");
            System.out.print(graphclone);
        }
                    
        graphclone = graph.clone();
        time2 = System.nanoTime();
        graphclone.Warshall2();
        time2 = System.nanoTime() - time2;
        if (showresults) {        
            System.out.println("Warshall's Algorithm with shortcut - " + graphclone.getEdges() + " edges:");
            System.out.print(graphclone); 
        }
                    
        graphclone = graph.clone();
        time3 = System.nanoTime();
        graphclone.Warshall3();
        time3 = System.nanoTime() - time3;
        if (showresults) {        
            System.out.println("Warshall's Algorithm with BitSets - " + graphclone.getEdges() + " edges:");
            System.out.print(graphclone.BitSettoString());
        }
        
        System.out.println(graph.getVertices() + "\t" + time1 + "\t" + time2 + "\t" + time3);
    }

}

