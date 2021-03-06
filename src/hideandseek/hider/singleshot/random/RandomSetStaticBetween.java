package hideandseek.hider.singleshot.random;

import java.util.ArrayList;

import org.jgrapht.alg.DijkstraShortestPath;

import Utility.Utils;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.HidingAgent;

/**
 * Produces a K size set of random nodes and then
 * heads for those nodes on the graph to hide in them.
 * 
 * NB: Doesn't allow bias to emerge as a product of low
 * weighted edges.
 * 
 * @author Martin
 *
 */
public class RandomSetStaticBetween extends RandomSet {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public RandomSetStaticBetween(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
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
