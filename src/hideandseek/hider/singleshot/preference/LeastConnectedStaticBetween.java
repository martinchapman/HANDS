package hideandseek.hider.singleshot.preference;

import java.util.ArrayList;
import java.util.HashSet;

import org.jgrapht.alg.DijkstraShortestPath;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.HidingAgent;

/**
 * Attempts to hide nodes with the least possible connectivity.
 * 
 * Best is thus leaf nodes.
 * 
 * @author Martin
 *
 */
public class LeastConnectedStaticBetween extends LeastConnected {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public LeastConnectedStaticBetween(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
	
		return currentNode();
		
	}


}
