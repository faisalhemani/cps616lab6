package Warshall;
import java.util.Scanner;
import java.util.Random;
import java.util.BitSet;

/**
  * Graph objects can be used to work with directed graphs.
  * They are internally represented using adjacency matrices.
  * @author Sophie Quigley
  * @author PUT YOUR NAMES HERE
  * <BR>THE ONLY METHODS THAT SHOULD BE MODIFIED ARE:
  * <BR>Warshall2 and Warshall3 
  * 
  */
public class Graph implements Cloneable {
    /**
     * Total number of vertices.
     */
    int vertices = 0;
    /**
     * Adjacency matrix of graph.
     * <br>edges[x][y] is the number of edges from vertex x to vertex y.
     */
    int[][] edges;
    /**
     * Total number of edges in graph
     */
    /**
     * BitSet representation of each row in adjacency matrix
     */
    BitSet[] bitsetrow;
    
    int totaledges = 0;
    /**
     * Used by graph visitors to keep track of visited vertices.
     */

    boolean[] visitedV;
    /**
     * Used by graph visitors to keep track of visited edges.
     */
    int[][] visitedE;
    /**
     * Used by graph visitors to keep track of unvisited edges
     * as an alternative to using visitedE.
     */
    int[][] unvisitedE;
    /**
     * Used to generate edges randomly
     */
    Random rand = new Random();   
    
    /**
     * 
     * Creates a new undirected Graph whose content will be read from the Scanner.
     * <br>Input format consists of non-negative integers separated by white space as follows:
     * <ul>
     * <li>First positive integer specifies the number of vertices n
     * <li>Next n*n integers specify the edges, listed in adjacency matrix order
     * </ul>
     * The graph information will be rejected when incorrect data is read.
     * @param in Scanner used to read graph description
     */
    Graph(Scanner in) {
        int i, j;
        
        // Read number of vertices
        vertices = in.nextInt();
        if (vertices <= 0) {
            System.out.println("Error: number of vertices must be positive");
            vertices = 0;
            return;
        }
        
        // Read adjacency matrix        
        edges = new int[vertices][vertices];
        for (i=0; i<vertices; i++) {
            for (j=0; j<vertices; j++) {
                edges[i][j]=in.nextInt();
                if (j>=i)   totaledges += edges[i][j];
                if (edges[i][j] <0){
                    System.out.println("Error: number of edges cannot be negative");
                    this.vertices = 0;
                    return;
                }
            }
        }
        // Prepare visitation status arrays
        visitedV = new boolean[vertices];
        visitedE = new int[vertices][vertices];
        unvisitedE = new int[vertices][vertices];   
        clearVisited();
        
        // Prepare BitSet representation
        BitSetConversion();
        
    }
    /**
     * Creates a randomly generated graph according to specifications,
     * or an empty graph if the specifications are faulty.
     * @param vertices Number of vertices in graph - must be positive
     * @param density density of graph: the number of edges will be 
     * about Vertices^2 / density
     */
     Graph( int vertices, int density) {
        if (vertices <= 0) {
            System.out.println("Error: number of vertices must be positive");
            return;
        }
        if (density <0) {
            System.out.println("Error: maxVertexDegree cannot be negative");
            return;           
        }
        this.vertices = vertices;
        edges = new int[vertices][vertices]; 
         
        // Populate edges randomly        
        int newedge;
        for (int i=0; i<vertices; i++)
            for (int j=0; j<vertices; j++) {
                newedge = rand.nextInt(density);
                if (newedge==0) edges[i][j]=1;
                totaledges += edges[i][j];
            }
        
        // Prepare visitation status arrays
        visitedV = new boolean[vertices];
        visitedE = new int[vertices][vertices];
        unvisitedE = new int[vertices][vertices];   
        clearVisited();
        
        // Prepare BitSet representation        
        BitSetConversion(); 
    }
    
    /**
     * Resets visitedV, visitedE, and unvisitedE matrices for a new visitation
     */
     private void clearVisited() {
        for (int i=0; i<vertices; i++) {
            visitedV[i] = false;
            for (int j=0; j<vertices; j++) {
                visitedE[i][j] = 0;
                unvisitedE[i][j] = edges[i][j];
            }
        }
    }
    
     /**
      * prepares an equivalent adjacency matrix representation
      * where each row is a BitSet
      */
    private void BitSetConversion() {
        bitsetrow = new BitSet[vertices];
        for (int i=0; i<vertices; i++) {
            bitsetrow[i] = new BitSet(vertices);
            for (int j=0; j<vertices; j++)
                if (edges[i][j]==1)
                    bitsetrow[i].set(j);
        }    
    }
    
    /**
     * Deep cloning of the Graph
     * @return a deep clone of the graph
     * @throws CloneNotSupportedException 
     */
    @Override
    public Graph clone() throws CloneNotSupportedException {
        Graph newgraph = (Graph) super.clone();
        newgraph.edges = this.edges.clone();
        newgraph.bitsetrow = this.bitsetrow.clone();
        newgraph.unvisitedE = this.unvisitedE.clone();
        newgraph.visitedE = this.visitedE.clone();
        newgraph.visitedV = this.visitedV.clone();
        for (int i=0; i<vertices; i++) {
            newgraph.edges[i] = this.edges[i].clone();
            newgraph.unvisitedE[i] = this.unvisitedE[i].clone();
            newgraph.visitedE[i] = this.visitedE[i].clone();            
        }
        return newgraph;
    }
    
   /**
    * Returns a String representation of the graph
    * which is a 2-D representation of the adjacency matrix of that graph.
    * @return The 2-D representation of the adjacency matrix of that graph.
    * 
    */    
    @Override
    public String toString() {
        return matrixtoString(edges);
    }

    /**
     * Returns a String representation of 2 dimensional matrix
     * of size vertices X vertices.  
     * This can be used to visualize edges, visitedE, and unvisitedE
     * @param matrix matrix to be represented
     * @return 2D string representation of matrix
     */
    private String matrixtoString(int[][] matrix) {
        String result = "";
        for (int i=0; i<vertices; i++) {
            for (int j=0; j<vertices; j++) {
                result += matrix[i][j];
                result += " ";
            }
            result += "\n";
        }
        return result;         
    }

    /** 
     * Returns a String representation of the BitSet representation of 
     * the adjacency matrix for the graph.
     * @return 2D string representation of the BitSet representation of the matrix
     */
    public String BitSettoString() {
        String result = "";
        for (int i=0; i<vertices; i++)
            result += bitsetrow[i] + "\n";
        return result;         
    }
    
    /**
    * Returns the number of vertices in the graph.
    * @return The number of vertices in the graph.
    *
    */  
    public int getVertices() {
        return vertices;
    }
    
    /**
    * Returns the number of edges in the graph.
    * @return The number of edges in the graph.
    *
    */  
    public int getEdges() {
        return totaledges;
    }   
    
    /**
     * Returns the adjacency matrix of the graph
     * @return The adjacency matrix of the graph 
     */
    public int[][] getMatrix() {
        return edges;
    }
    
    /**
     * Returns the number of edges from sourceV to destV 
     * @param sourceV The source vertex
     * @param destV The destination vertex
     * @return the number associated with edges from sourceV to destV
     */
    public int getEdges(int sourceV, int destV) {
        if (sourceV>=0 && sourceV<vertices && destV>=0 && destV<vertices)
            return edges[sourceV][destV];
        else
            return 0;
    }  
      
    /**
     * Verifies whether graph is connected
     * @return True iff graph is connected
     */
    public boolean isConnected() {
        clearVisited();
        DFSvisit(0);
        for (int i=0; i<vertices; i++)  
            if (!visitedV[i]) {
                clearVisited();
                return false;
            }
        clearVisited();
        return true;        
    }
    
    /**
     * Conducts a Depth First Search visit of the unvisited vertices 
     * of the graph starting at vertex.  
     * <br>Ties between vertices are broken in numeric order.
     * <br>Used by isConnected()
     * @param vertex First vertex to be visited.
     */
    private void DFSvisit(int vertex) {
        visitedV[vertex] = true;
        for (int i=0; i<vertices; i++)
            if (edges[vertex][i]!=0 && !visitedV[i])
                DFSvisit(i);
    } 
    
    /**
     * Warshall's algorithm from the textbook
     */
    public void Warshall1() {
        int i, j, k;
        for (k=0; k<vertices; k++)
            for (i=0; i<vertices; i++)
                for (j=0; j<vertices; j++)
                    if (edges[i][j]==0 && edges[i][k]==1 && edges[k][j]==1) {
                        edges[i][j] = 1;
                        totaledges++;
                    }
    }
    
    /**
     * Same as Warshall1 but with shortcircuiting
     */
    public void Warshall2() {
    	int i, j, k;
        for (k=0; k<vertices; k++){
            for (i=0; i<vertices; i++){
            	if(edges[i][k] == 1){
	                for (j=0; j<vertices; j++){
	                    if (edges[i][j]==0 || edges[k][j]==1) {
	                        edges[i][j] = 1;
	                        totaledges++;
	                    }
            		}
            	}
			}
		}
    }
    
    /**
     * Same as Warshall2 but using BitSets for the matrix rows
     * 
     * I DON THINK WHAT I HAVE HERE IS RIGHT
     */
    public void Warshall3() {
    	int i, j, k;
    	for (k=0; k<vertices; k++){
            for (i=0; i<vertices; i++){
            	if(bitsetrow[i].get(k)){
	            	for (j=0; j<vertices; j++){
	            		if (!bitsetrow[i].get(j) || bitsetrow[k].get(j))
	            			bitsetrow[i].set(j);
	            	}            		
            	}
            }
    	}
    }
    
}