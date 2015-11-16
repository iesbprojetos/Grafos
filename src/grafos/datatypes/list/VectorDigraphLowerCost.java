package grafos.datatypes.list;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import static grafos.Constants.*;

public class VectorDigraphLowerCost extends VectorDigraph {
	private static int INF = Integer.MAX_VALUE;

	public VectorDigraphLowerCost(int vertices) {
		super(vertices);
	}
	
	int cost [] ;
	int adjVector [];
	protected int ts [] ;
	
	
	public int insertArc(int v, int w) {
        int result = super.insertArc(v,w);

        if (result == RESULT_OK) {
        	adjVector[v] = cost[v];
        }

        return result;
    }
			
    public void Dagmin (int s){
    	int i = 0;
	for (int v  = 0; v < vertices ; v++){
		cost[v] = INF;
		                 }
		cost[s] = 0;
		
		for (int v = ts[i];i < vertices;v = ts[i++]){
		for (int w = 0;w < vertices;w++){
			if( adjVector[v] != 0){
		       if (cost[w] > cost[v]+adjVector[v]){
		           cost[w] = cost[i]+adjVector[v];
		       }
		    }
		  }
	    }
      }
    
    public void FordSentinela(int s){
    	int Sentinela = vertices;
    	int k = 0;
    	int retorno_sentinela = -1;
    	
    	List<Integer> Sentilist = new ArrayList<Integer>();
    	
    	for (int v = 0;v < vertices;v++){
    	cost[v] = INF;
    	parent[v] = -1; }
    	
    	cost[s] = 0;
    	parent[s] = 0;
    	
    	Sentilist.add(s);
    	Sentilist.add(Sentinela);
    	
    	while(Sentilist.size() > 0){
    	   int v = Sentilist.remove(0);
    	   if (v == Sentinela){
    	      if (k++ == vertices){
    	         if (!(Sentilist.isEmpty())){
    	        	   retorno_sentinela = 0;}
    	         else{
    	        	  retorno_sentinela = 1; }
    	   
    	         Sentilist.add(Sentinela);  }
    	      else{ 
    	    	  for (int j = 0; j < adjVector[v];j++){
    	             for (int p = adjVector[v]; p != 0; p = j){
    	            	
    	                if (cost[vertices] > cost[v]+ p) {
    	                    cost[vertices] = cost[v]+p;
    	                    parent[vertices] = v;
    	                    Sentilist.add(vertices);;  }
    	       }
    	     }       	
    	   }	
        }
     }
   } 	
}

 