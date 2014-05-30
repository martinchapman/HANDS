package Utility;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import HideAndSeek.graph.StringVertex;

/**
 * @author Martin
 *
 */
public class BehaviourPrediction  {

	/**
	 * Each position in array is a new Hashtable, added at the start of every round, and thus only recording
	 * frequencies from that round onwards. Allows to access an offset of the data.
	 * 
	 */
	private ArrayList<Hashtable<StringVertex, Integer>> hideLocationsList = new ArrayList<Hashtable<StringVertex, Integer>>();
	
	/**
	 * Each position in array is a new Hashtable, added at the start of every round, and thus only recording
	 * probabilities from that round onwards. Allows to access an offset of the data.
	 * 
	 */
	private ArrayList<Hashtable<StringVertex, Double>> hideProbabilitiesList = new ArrayList<Hashtable<StringVertex, Double>>();
	
	/**
	 * 
	 */
	private int beginningGap = 0;
	
	/**
	 * @param gap
	 */
	public void setBeginningGap(int gap) { beginningGap = gap; }
	
	/**
	 * 
	 */
	public BehaviourPrediction() {
		
		hideLocationsList.add(new Hashtable<StringVertex, Integer>());
		
		hideProbabilitiesList.add(new Hashtable<StringVertex, Double>());
		
	}
	
	/**
	 * @param hideLocation
	 */
	public void recordHideLocation(StringVertex hideLocation) {
		
		for ( Hashtable<StringVertex, Integer> hideLocations : hideLocationsList ) {
			
			if (hideLocations.keySet().contains(hideLocation)) {
				
				hideLocations.put(hideLocation, hideLocations.get(hideLocation) + 1);
				
			} else {
				
				hideLocations.put(hideLocation, 1);
				
			}
			
		}
		
	}
	
	/**
	 * Should happen after the end of each round 
	 */
	public synchronized void endRound() {
		
		int i = 0;
		
		for ( Hashtable<StringVertex, Integer> hideLocations : hideLocationsList ) {
		
			double scale = 0.0;
			
			for ( Map.Entry<StringVertex, Integer> entry : hideLocations.entrySet() ) {
				
				scale += entry.getValue();
				
			}
			
			for ( Map.Entry<StringVertex, Integer> entry : hideLocations.entrySet() ) {
				
				hideProbabilitiesList.get(i).put(entry.getKey(), entry.getValue() / scale);
				
			}
			
			i++;
		
		}
		
		hideLocationsList.add(new Hashtable<StringVertex, Integer>());
		
		hideProbabilitiesList.add(new Hashtable<StringVertex, Double>());
		
	}
	
	/**
	 * @return
	 */
	private Hashtable<StringVertex, Integer> hideLocations() {
		
		if( beginningGap + 1 > hideLocationsList.size() ) { return new Hashtable<StringVertex, Integer>(); }
		
		return hideLocationsList.get(beginningGap);
		
	}
	
	/**
	 * @return
	 */
	private Hashtable<StringVertex, Double> hideProbabilities() {
		
		if( beginningGap + 1 > hideProbabilitiesList.size() ) { return new Hashtable<StringVertex, Double>(); }
		
		return hideProbabilitiesList.get(beginningGap);
		
	}
	
	/**
	 * @param range number of high probability nodes to include
	 * @return
	 */
	public ArrayList<StringVertex> rankLikelyHideLocations(int range) {
		
		ArrayList<StringVertex> likelyNodes = new ArrayList<StringVertex>();
		
		Hashtable<StringVertex, Double> hideProbabilitiesLocal = new Hashtable<StringVertex, Double>(hideProbabilities());
		
		ArrayList<StringVertex> multipleMaxValues = new ArrayList<StringVertex>();
		
		// If range is too high, just take all values
		if (hideProbabilitiesLocal.keySet().size() < range) range = hideProbabilitiesLocal.keySet().size(); 
		
		// Key = Vertex; Value = Probability
		for (int i = 0; i < range; i++) {
			
			StringVertex maxKey = null;
			
			Double maxValue = Double.MIN_VALUE; 
			
			multipleMaxValues.clear();
			
			for ( Map.Entry<StringVertex,Double> entry : hideProbabilitiesLocal.entrySet() ) {
				
			    if ( entry.getValue() > maxValue ) {
			    	 
			    	 // Clear any previous multiple max values, as this value takes precedent
			    	 multipleMaxValues.clear();
			    	 
			    	 // Update new value and new key
			         maxValue = entry.getValue(); 
			         maxKey = entry.getKey();
			         
			         // Add this value, as it may be 
			         multipleMaxValues.add(maxKey);
			        
			     } else if (entry.getValue().equals(maxValue)) {
			    	
			    	 // Update new max key and value (although value will be same)
			    	 maxValue = entry.getValue(); 
			    	 maxKey = entry.getKey();
			         
			         multipleMaxValues.add(maxKey);
			  
			     }
			     
			}
			
			if (multipleMaxValues.size() > 1) {
				
				maxKey = multipleMaxValues.get((int)(Math.random() * multipleMaxValues.size()));
				
			} 
			
			likelyNodes.add(maxKey);
			
			hideProbabilitiesLocal.remove(maxKey);
			
		}
		
		return likelyNodes;
		
	}
	
	public double getProbability(StringVertex vertex) {
		
		return hideProbabilities().get(vertex);
		
	}
	
	public String toString() {
		
		String returner = "";
		
		ArrayList<StringVertex> likelyHideLocations = rankLikelyHideLocations(Integer.MAX_VALUE);
		
		for ( StringVertex likelyLocation : likelyHideLocations ) {
			
			returner += ("Location: " + likelyLocation + " Percentage: " + hideProbabilities().get(likelyLocation));
			
			returner += "\n";
			
		}
		
		return returner;
		
	}
	
}
