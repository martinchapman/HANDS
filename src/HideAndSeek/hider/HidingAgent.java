package HideAndSeek.hider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import HideAndSeek.GraphTraversingAgent;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public abstract class HidingAgent extends GraphTraversingAgent implements Runnable, Hider {

	/**
	 * 
	 */
	protected int numberOfHideLocations;
	
	/**
	 * @return
	 */
	public int numberOfHideLocations() {
		
		return numberOfHideLocations;
	
	}

	/**
	 * In the case that 'hideHere' is called multiple times,
	 * after the first time we mark the returned hide location
	 * as checked.
	 */
	private ArrayList<StringVertex> precheckedHideLocations;
	
	/**
	 * @param graph
	 */
	public HidingAgent(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
	
		super(graphController);
		
		this.numberOfHideLocations = numberOfHideLocations;
		
		precheckedHideLocations = new ArrayList<StringVertex>();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#toString()
	 */
	@Override
	public String toString() {
		
		return "h" + name;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#hideLocations()
	 */
	public ArrayList<StringVertex> hideLocations()
    {
        throw new UnsupportedOperationException("Cannot access hide locations directly.");
    }
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public void addHideLocation(StringVertex location) {
		
		Utils.talk(toString(), "---------------------------------- Hiding in " + location);
		
		// Hider's local copy of where he has hidden
		super.hideLocations().add(location); 
		
		graphController.addHideLocation(responsibleAgent, location);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#run()
	 */
	@Override
	public void run() {
		
		Utils.talk(toString(), "Running " + ID + " " + responsibleAgent.getClass());
		
		startPlaying();
		
		hide();
		
	}
	
	public String getStatus() {
		
		return super.getStatus() + "\nprecheckedHideLocations: " +  precheckedHideLocations + "\n";
		
	}
	/**
	 * @param searchPath
	 * @return
	 */
	private boolean hide() {
		
		StringVertex startNode = startNode();
		
		if ( currentNode != null && currentNode != startNode ) {
			
			graphController.walkPathFromVertexToVertex(responsibleAgent, currentNode, startNode);
			
		} 
		
		atStart(startNode);
		
		StringVertex nextNode = null;
		
		while (graphController.numberOfHideLocations(responsibleAgent) != numberOfHideLocations) {
			
			atNode();
			
		    if ( !super.hideLocations().contains(currentNode) && ( hideHere(currentNode) || precheckedHideLocations.contains(currentNode) ) ) { 
	        	
        		addHideLocation(currentNode);
				
				if (graphController.numberOfHideLocations(responsibleAgent) == numberOfHideLocations) break;
			
        	}
		    
		    if ( queuedNodes.size() > 0 ) {
				
				nextNode = queuedNodes.remove(0);
				
				Utils.talk(responsibleAgent.toString(), "Going to queued node: " + nextNode);
				
			} else {
				
			    nextNode = nextNode(currentNode);
			
			}
		    
		    /* 
		     * If a connected node contains a hide location we are looking for, overwrite strategies choice and 
		     * move there (i.e. 'help it out').
		     */
		    for ( StringEdge connectedEdge : getConnectedEdges(currentNode) ) {
		    	
		    	if ( AUTOMATIC_MOVE && !super.hideLocations().contains( edgeToTarget(connectedEdge, currentNode) ) && hideHere( edgeToTarget(connectedEdge, currentNode) ) ) {
		    		
		    		// To move back to where the agent was, and then to proceed where the agent was going to go.
		    		queuedNodes.add(0, nextNode);
		    		
		    		queuedNodes.add(0, currentNode);
		    		
		    		nextNode = edgeToTarget(connectedEdge, currentNode);
		    		
		    		Utils.talk(responsibleAgent.toString(), "Moving automatically to " + nextNode + ". Queued: " + queuedNodes);
		    		
		    		precheckedHideLocations.add(nextNode);
		    	
		    	}
		    	
		    }
		    	
			addUniquelyVisitedEdge(graphController.getEdge(currentNode, nextNode));
			
			if ( !graphController.fromVertexToVertex(responsibleAgent, currentNode, nextNode) ) { 
				
				Utils.talk(responsibleAgent.toString(), "Error traversing supplied path from " + currentNode + " to " + nextNode);
				
				return false; 
				
			} else {
				
				currentNode = nextNode;
			
			}
			
		}
		
	    return true;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#printRoundStats()
	 */
	@Override
	public String printRoundStats() {
		
		return super.printRoundStats() + "Cost, " + graphController.latestRoundCosts(responsibleAgent, false) + ",Path," + exploredNodes.toString().replace(",", "");
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#printGameStats()
	 */
	@Override
	public String printGameStats() {
		
		return super.printGameStats() + "Cost, " + graphController.averageGameCosts(responsibleAgent);
		
	}
	

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		super.hideLocations().clear();
		
		exploredNodes.clear();
		
		precheckedHideLocations.clear();
		
	}
	
	/**
	 * @param traverser
	 */
	public void mergeOtherTraverser(Hider traverser) {
		
		super.mergeOtherTraverser(traverser);
		
		this.numberOfHideLocations = traverser.numberOfHideLocations();
				
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public abstract boolean hideHere(StringVertex vertex);
	
	

}