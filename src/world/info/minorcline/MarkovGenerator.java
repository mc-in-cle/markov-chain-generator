package world.info.minorcline;

/**
 * Establishes a Markov chain for sequenced objects, and uses this model to generate random output 
 * based on the input sequence.
 * A rewrite and generalization of CS 151 project in April 2012 which handled Strings.
 * @author M. Cline
 * August 2014
 * 
 * November 2018: Refactored with minor optimizations, and to use Trie data structure instead of HashMap.
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class MarkovGenerator<T> {
    
    private int order;
    private Trie<T,MarkovMap<T>> subsequences;
    private List<T> firstInputSubseq;
    private boolean finalized;
    private LinkedList<T> outputSubseq;
    
    /**
     * Creates a new Markov chain from the data found by the given file reader.
     * @param order the order of the Markov model; length of observed subsequences
     * @param r iterates through the sequence on which the model will be based.
     */
    public MarkovGenerator(int order) {
		finalized = false;
		this.order = order;
		subsequences = new Trie<T, MarkovMap<T>>();
    }
    
    
    /**
     * Reads all the input from another iterator and adds that information to the Markov chain,
     *  unless the generator has already been finalized.
     * @param dataIterator iterates through the next input sequence.
     * @return true if the information from the iterator was added to the model, or false if the model 
     * was not changed as a result of the call.
	 * @throws IllegalStateException when <code>dataIterator</code> does not provide enough data to 
	 * furnish the generator.
     */
    public boolean addIterator(Iterator<T> dataIterator) throws IllegalStateException{
		if(finalized)
			return false;
		Iterator<T> dataReader = dataIterator;
		getFirst(dataReader);
		buildMarkovs(dataReader);
		return true;
    }
	
	 /**
     * Obtains the first subsequence of length <code>order</code> from the iterator.
     */
    private void getFirst(Iterator<T> dataReader) {
    	firstInputSubseq = new ArrayList<>();
		int i = 0;
		while (dataReader.hasNext() && i < order){
			firstInputSubseq.add(dataReader.next());
			i++;
		}
		if(firstInputSubseq.size() < order)
			throw new IllegalStateException("Not enough data provided. Obtained only " + firstInputSubseq.size() + 
					"items.");
    }
    
    /**
     * Reads through the entire file and builds Markov objects for each subsequence found in the file.
     * Does not build a Markov object for the last subsequence found in the input because there is no 
     * data to follow it.
     */
    private void buildMarkovs(Iterator<T> dataReader) {
    	
		subsequences.put(firstInputSubseq, new MarkovMap<T>());
		LinkedList<T> subSeq = new LinkedList<T>();
		for (T t: firstInputSubseq) subSeq.add(t);
		
		while (dataReader.hasNext()){
			T nextItem = dataReader.next();
			MarkovMap<T> m = subsequences.get(subSeq);
			if (m == null) {
				m = new MarkovMap<T>();
				subsequences.put(subSeq, m);
			}
			m.add(nextItem);
			//shift left
			subSeq.remove();
			subSeq.add(nextItem);
		}
	}
    
    /**
     * Prepares for generation. Must be called before <code>nextRandom()</code> can be used.
     * @throws IllegalStateException if not enough information has been offered through 
     * <code>addIterator()</code>.
     */
    public void finalizeGenerator() throws IllegalStateException{
    	if (subsequences.isEmpty()) {
    		throw new IllegalStateException("Not enough information provided through addIterator() to finalize MarkovGenerator.");
    	}
		Iterator<MarkovMap<T>> maps = subsequences.valuesIterator();
		while(maps.hasNext()){
			maps.next().getReady();
		}
		finalized = true;
		outputSubseq = new LinkedList<T>(firstInputSubseq);
    }
    
    /**
     * Gets the number of distinct subsequences of length <code>order</code> in the entire input.
     * @return the number of distinct subsequences of length <code>order</code> in the entire input.
     */
    public int size(){
		return subsequences.size();
    }
    
    /**
     * Gets the next random item based on the Markov chain for given sequence.
     * @param seq the substring of length k which is referred to in the Markov chain.
     * @return a random item based on the Markov chain, or null if the last subsequence in 
     * the source is inputted, since it is excluded from the model.
	 * @throws IllegalStateException if <code>finalize()</code> has not been called.
     */
    private T nextRandom(Iterable<T> seq) throws IllegalStateException{
		if(!finalized)
			throw new IllegalStateException("MarkovGenerator has not been finalized.");
		MarkovMap<T> m;
		T t;
		try{
			m = subsequences.get(seq);
			t = m.random();
		}catch(NullPointerException e){
			return null;
		}	    
		return t;
    }
 
    
    /**
     * Displays the whole language model: how many unique subsequences are in the model, 
     * how often each occurs, and how often each suffix occurs.
     */
    public String toString(){
		StringBuilder sb = new StringBuilder();
		System.out.println(subsequences.size() + " k-tuples:");
		Iterator<MarkovMap<T>> itr = subsequences.valuesIterator();
		while(itr.hasNext())
			sb.append(itr.next().toString()+"\n");
		return sb.toString();
    }
	
	/**
	* Uses the complete Markov model to generate a random sequence of given length
	* based on the model and the previous sequence of items generated.
	* @param length the length of output to generate
	* @throws IllegalStateException if <code>finalize()</code> has not been called.
	*/
	public List<T> generateList(int length) throws IllegalStateException{
		List<T> fullSeq = new ArrayList<T>(length);
		fullSeq.addAll(outputSubseq);
		while(fullSeq.size() < length){
			fullSeq.add(generate());
		}
		return fullSeq;
	}
	
	/**
	* Uses the complete Markov model to generate a random item based on the model
	* and the previous sequence of items generated.
	* @throws IllegalStateException if MarkovGenerator has not been finalized.
	*/
	public T generate() throws IllegalStateException{
		if (!finalized)
			throw new IllegalStateException("MarkovGenerator has not yet been finalized.");
		//This is how the program keeps track of having reset to the start of the sequence.
		if (outputSubseq.size() < order) {
			int index = outputSubseq.size() - 1;
			outputSubseq.push(firstInputSubseq.get(index + 1));
			return outputSubseq.peekLast();
		}
		else {
			T nextRand = nextRandom(outputSubseq);
			//If nextRandom returns null, that means outputSubeq is the last subsequence in the source 
			//and has no Markov model associated with it.
			//In this case, reset to the first subsequence.
			if (nextRand == null) {
				outputSubseq.clear();
				outputSubseq.push(firstInputSubseq.get(0));
				return outputSubseq.peekLast();
			}
			else{
				//shift left
				outputSubseq.remove();
				outputSubseq.add(nextRand);
	        }
			return nextRand;
		}
	}

}

