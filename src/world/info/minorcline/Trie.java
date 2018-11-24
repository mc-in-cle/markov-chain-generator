package world.info.minorcline;


import java.lang.Iterable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Trie<K, V>{

    private Node<K,V> head;
    private int size;
    private HashPile<V> values;
    

    public Trie(){
        head = new Node<K,V>();
        size = 0;
        values = new HashPile<>();
    }

    public void clear(){
        head = new Node<K,V>();
        size = 0;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void put(Iterable<K> sequence, V value){
        put (sequence.iterator(), value);
    }

    public void put (K[] sequence, V value){
        put (iteratorForArray(sequence), value);
    }

    //if you give null for value, it will do nothing.
    //should I throw something here?
    //consult behavior of collections class
    private void put (Iterator<K> itr, V value){
    	if (value == null)
    		return;
    	
        Node<K,V> node = head;
        while (itr.hasNext()){
            K k = itr.next();
            Node<K,V> child = node.children.get(k);
            if (child == null){
                child = new Node<>();
                node.children.put(k, child);
            }
            node = child;
        }
        if (node.value == null)
            size ++;
        if (node.value != value) {
        	values.add(value);
        	values.removeExactly(node.value);
            node.value = value;
        }
    }

    public boolean containsKey(K[] sequence){
        return containsKey(iteratorForArray(sequence));
    }
    
    public boolean containsKey(Iterable<K> sequence){
        return containsKey(sequence.iterator());
    }

    private boolean containsKey(Iterator<K> itr){
        Node<K,V> child = head;
        Node<K,V> node = head;
        while (itr.hasNext()){
            K k = itr.next();
           child = node.children.get(k);
            if (child == null){
                return false;
            }
            node = child;
        }
        return child.value != null;
    }

    public void remove(K[] sequence){
        remove(iteratorForArray(sequence));
    }
    
    public void remove(Iterable<K> sequence){
        remove(sequence.iterator());
    }

    private void remove(Iterator<K> itr){
        Node<K,V> child = head;
        Node<K,V> node = head;

        K emptyBranchParent = null;
        Node<K,V> lastNonEmpty = head;

        while (itr.hasNext()){
            K k = itr.next();
            child = node.children.get(k);
            if (child == null){
                return;
            }
            if (node.value != null && itr.hasNext()){
                lastNonEmpty = node;
                emptyBranchParent = k;
            }
            if (itr.hasNext()){
                node = child;
            }
        }
        size--;
        values.removeExactly(child.value);
        lastNonEmpty.children.remove(emptyBranchParent);
    }

    public V get(K[] sequence){
        return get(iteratorForArray(sequence));
    }
    
    public V get(Iterable<K> sequence){
        return get(sequence.iterator());
    }

    private V get(Iterator<K> itr){
        Node<K,V> node = head;
        while (itr.hasNext()){
            K k = itr.next();
            Node<K,V> child = node.children.get(k);
            if (child == null){
                return null;
            }
            node = child;
        }
        return node.value;
    }
    
    public Iterator<V> valuesIterator(){
    	return values.iterator();
    	
    }

    private Iterator<K> iteratorForArray(K[] array){
        return new Iterator<K>(){
            int i = 0;
            
            public boolean hasNext(){
                return i < array.length;
            }

            public K next(){
                return array[i++];
            }
        };
    }
    
    private class Node<X,Y>{
        HashMap<X, Node<X,Y>> children;
        Y value;
        Node(){
            children = new HashMap<>();
        }
    }
}
