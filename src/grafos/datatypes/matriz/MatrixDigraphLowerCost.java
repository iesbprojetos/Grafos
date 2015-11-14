package grafos.datatypes.matriz;

  import static grafos.Constants.*;

	
	public class MatrixDigraphLowerCost extends MatrixDigraph {
	    
	    protected int[][] custo;


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
	    		custo[s][t] = adjMatrix[s][t];}
	    	
	    	for (int k = 1;k < vertices;k++){
	    	  for (s = 0;s < vertices;s++) {
	    		for (t = 0;t < vertices;t++) {
	    		  d = custo[s][k-1]+custo[k-1][t];
	    		  if (custo[s][t] > d) {
	    		      custo[s][t] = d;    
	    		      }
	             }
	   		}
		}   
	 }
  }
