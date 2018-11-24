package world.info.minorcline;

/**
* Represents one node in a Markov chain.
* Used in conjunction with MarkovGenerator.
* @author M. Cline August 2014
* 
* November 2018 Refactored for minor optimization and to generalize for any Iterable.
*/

import java.util.TreeMap;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.List;

public class MarkovMap<T>{
	/**Number of documented occurrences of the node sequence*/
	private int freq;
	/**The Markov state-change map itself, with frequencies in integers rather than probabilities*/
	private TreeMap<T, Integer> occurrenceMap;
	/**A prepared O(1) map for fast Markov chain generation**/
	private ArrayList<T> randMap;
	private boolean ready;
	
	/**Construct a new MarkovMap for the node state T <code>state</code>
	* @param state the sequence that becomes the node of this particular map.
	*/
	public MarkovMap(){
		freq = 0;
		ready = false;
		occurrenceMap = new TreeMap<T, Integer>();
		randMap = null;
	}
	
	/**Document an instance of a suffix following the node.
	* If called after <code>getReady(), getReady()</code> must be called again before <code>random()</code>.
	* @param T a suffix observed to follow the node sequence.
	* @return true if suffix was documented in this map for the first time.
	*/
	public boolean add(T suffix){
		if (ready) ready = false;
		int f = 0;
		try{
			f = occurrenceMap.get(suffix);
		}catch (NullPointerException e){
			f = 0;
		}
		occurrenceMap.put(suffix, ++f);
		freq++;
		if (f == 1) return true;
		return false;
	}
	
	 /**
	 * Produces a random suffix state based on the model.
	 * @throws IllegalStateException if getReady() has not been called yet.
	 */
	public T random() throws IllegalStateException{
		if(!ready)
			throw new IllegalStateException("This MarkovMap has not been readied for text generation yet.");
		int rand = (int)(Math.random()*freq);
		return randMap.get(rand);
	}
	
	 /**
	 * Prepares the MarkovMap for fast random chain generation. Must be called before <code>random()</code> can be called.
	 */
	public void getReady(){
		randMap = new ArrayList<T>(freq);
		Iterator<Map.Entry<T,Integer>> itr = occurrenceMap.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<T,Integer> nextEntry = itr.next();
			int nextVal = nextEntry.getValue();
			T nextKey = nextEntry.getKey();
			for(int i=0; i<nextVal; i++){
				randMap.add(nextKey);
			}
		}
		ready = true;
	}

	/**
	 * Useful for testing purposes or viewing the collected frequency data.
	 * @return all data contained in the map.
	 */
	public String showData(){
		StringBuilder sb = new StringBuilder(freq + " ");
		
		Iterator<Map.Entry<T,Integer>> itr = occurrenceMap.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<T,Integer> nextEntry = itr.next();
			sb.append(" " + nextEntry.getValue() + " " + nextEntry.getKey()+ "\n");
		}
		
		return sb.toString();		
	}
}
