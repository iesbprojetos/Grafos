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
	
	int custo [] ;
	int adjVector [];
	protected int[] ts;
	
	public int insertArc(int v, int w) {
        int result = super.insertArc(v,w);

        if (result == RESULT_OK) {
        	adjVector[v] = custo[v];
        }

        return result;
    }
	
	public void Bellman_ford(int v, int w){
		
		
	}
			
    public void Dagmin (int s){
    	int i = 0;
	for (int v  = 0; v < vertices ; v++){
		custo[v] = INF;
		                 }
		custo[s] = 0;
		
		for (int v = ts[i];i < vertices;v = ts[i++]){
		for (int w = 0;w < vertices;w++){
			if( adjVector[v] != 0){
		       if (custo[w] > custo[v]+adjVector[v]){
		           custo[w] = custo[i]+adjVector[v];
		       }
		    }
		  }
	    }
      }
    
    public void FordSentinela(int s){
    	int Sentinela = vertices;
    	List<Integer> Sentilist = new ArrayList<Integer>();
    	
    	for (int v = 0;v < vertices;v++){
    	custo[v] = INF;
    	parent[v] = -1; }
    	
    	custo[s] = 0;
    	parent[s] = 0;
    	
    	Sentilist.add(s);
    	Sentilist.add(Sentinela);
    	
    	while(Sentilist.size() > 0){
    	   int v = Sentilist.remove(0);
    	   
    	
    }
    
    
	}
}
 