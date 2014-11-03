package HideAndSeek.hider.singleshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * A hider who's tendency to choose cheap edges over random edges
 * is manually set.
 * 
 * Relies on edges being of variable weight.
 * 
 * @author Martin
 */
public class FixedStartVariableLowEdgeCost extends VariableLowEdgeCost {

	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public FixedStartVariableLowEdgeCost(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double bias) {
		super(graphController, numberOfHideLocations, bias);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {

		return firstNode();
		
	}

}