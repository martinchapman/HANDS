package HideAndSeek.hider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public abstract class Hider extends GraphTraverser implements Runnable {

	/**
	 * 
	 */
	protected int numberOfHideLocations;
	
	/**
	 * 
	 */
	protected ArrayList<StringVertex> hideLocations;
	
	/**
	 * @return
	 */
	public ArrayList<StringVertex> getHideLocations() {
		
		return hideLocations;
	
	}

	/**
	 * 
	 */
	protected ArrayList<StringVertex> exploredNodes;
	
	/**
	 * 
	 */
	private String ID;
	
	/**
	 * @param graph
	 */
	public Hider(HiddenObjectGraph<StringVertex, StringEdge> graph, int numberOfHideLocations) {
	
		super(graph);
		
		this.numberOfHideLocations = numberOfHideLocations;
		
		ID = "" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + Math.random() * 100;

		hideLocations = new ArrayList<StringVertex>();
		
		exploredNodes = new ArrayList<StringVertex>();
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return this.getClass().toString().substring(this.getClass().toString().lastIndexOf('.') + 1, this.getClass().toString().length());
		
	}
	
	/**
	 * @param location
	 */
	protected void addHideLocation(StringVertex location) {
		
		Utils.talk(toString(), "---------------------------------- Hiding in " + location);
		
		// Hider's local copy of where he has hidden
		hideLocations.add(location); 
		
		graph.addHideLocation(location);
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		System.out.println("Running " + ID + " " + this.getClass());
		
		hide();
		
	}
	
	/**
	 * @param searchPath
	 * @return
	 */
	private boolean hide() {
		
		StringVertex currentNode = startNode();
		
		StringVertex nextNode = null;
		
		while (graph.numberOfHideLocations() != numberOfHideLocations) {
			
			exploredNodes.add(currentNode);
			
			uniquelyVisitedNodes.add(currentNode);
			
		    if ( !hideLocations.contains(currentNode) && hideHere(currentNode) ) { 
	        		
        		addHideLocation(currentNode);
				
				if (graph.numberOfHideLocations() == numberOfHideLocations) { break; }
			
        	}
			 
			nextNode = nextNode(currentNode);
			
			if ( !graph.fromVertexToVertex(this, currentNode, nextNode) ) { 
				
				Utils.talk(this.toString(), "Error traversing supplied path.");
				
				return false; 
				
			} else {
				
				currentNode = nextNode;
			
			}
			
		}
		
	    return true;
		
	}
	
	/**
	 * @return
	 */
	public String printRoundStats() {
		
		Utils.talk(toString(), "" + graph.latestRoundPaths(this));
		
		return "";
		
	}
	
	/**
	 * @param rounds
	 * @return
	 */
	public String printGameStats() {
		
		return "";
		
	}
	
	/**
	 * Record of the number of rounds passed 
	 */
	protected int roundsPassed = 0;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#endOfRound()
	 */
	public void endOfRound() {
		
		super.endOfRound();
		
		hideLocations.clear();
		
		roundsPassed++;
		
	}
	
	/**
	 * Given a vertex, do I hide in that vertex, based upon
	 * the content of this method
	 * @param vertex
	 * @return
	 */
	protected abstract boolean hideHere(StringVertex vertex);

}
