package grafos.datatypes.matriz;

  import static grafos.Constants.*;

	
	public class MatrixDigraphLowerCost extends MatrixDigraph {
	  
		 private static int INF = Integer.MAX_VALUE;
		 private int cost [][];
		 

	    public MatrixDigraphLowerCost(int vertices) {
	        super(vertices);
	    }
	    

	    public int insertArc(int v, int w, int custo) {
	        int result = super.insertArc(v, w);

	        if (result == RESULT_OK) {
	            adjMatrix[v][w] = custo;
	        }

	        return result;
	    }

	    public void Floyd(int s,int t, int d){
	    	
	    	for (int v = 0; v < vertices ; v++){
	    		cost[s][t] = adjMatrix[s][t];}
	    	
	    	for (int k = 1;k < vertices;k++){
	    	  for (s = 0;s < vertices;s++) {
	    		for (t = 0;t < vertices;t++) {
	    		  d = cost[s][k-1]+cost[k-1][t];
	    		  if (cost[s][t] > d) {
	    		      cost[s][t] = d;    
	    		      }
	             }
	   		}
		}   
	 }
	    
	    public void Ford (int s, int d){
	    	
	    	for (int v = 0; v < vertices;v++){
	    		cost[0][v] = INF;
	    		cost[0][s] = 0; }
	    		for (int k = 1;k < vertices;k++){
	    		    for (int w = 0;w < vertices;w++){
	    	  	        cost[k][w] = cost[k-1][w];
	    		        for (int v = 0;v < vertices;v++){
	    		               d = cost[k-1][v] + adjMatrix[v][w];
	    		               if (cost[k][w] > d){
	    		                   cost[k][w] = d; }
	    		        }
	    		    }
	    		}
	       }	    	
	}
