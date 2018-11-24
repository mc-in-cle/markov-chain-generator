package world.info.minorcline;

/*
 * A hot and loose data structure that must be used with caution.
 * HashPile is like a HashSet, but can store multiple distinct
 * objects that are equal.
 * Permits constant-time remove and contains operations.
 * "Remove" is constant-time on average. Worst case is linear time,
 * which occurs all the objects in the HashPile are equal.
 * 
 * Caution: HashPile is not appropriate for collections of objects
 * that will be mutated. Objects that are equal when they are stored 
 * in the HashPile must continue to be equal, or the HashPile will 
 * fail to behave correctly.
 * 
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Deque;

public class HashPile<T> {
	private HashMap<T, Wad<T>> pile;
	private int size;
	
	public HashPile(){
		pile = new HashMap<>();
		size = 0;
	}
	
	public void clear() {
		pile.clear();
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public void add(T item) {
		Wad<T> wad = pile.get(item);
		if (wad == null) {
			pile.put(item, new Wad<T>(item));
		}
		else {
			wad.add(item);
		}
		size++;
	}
	
	public boolean contains(T item) {
		Wad<T> wad = pile.get(item);
		if (wad == null)
			return false;
		else {
			return wad.contains(item);
		}
	}
	
	/*
	 * Remove the exact item 'item'. Unlike the remove() specified
	 * in Collections, this method will not remove the first item
	 * found which is equal to item; it will only remove the same item.
	 */
	public void removeExactly(T item) {
		Wad<T> wad = pile.get(item);
		if (wad == null) {
			return;
		}
		else {
			int before = wad.size();
			wad.removeExactly(item);
			size -= before - wad.size();
			if (wad.isEmpty())
				pile.remove(item);
		}
	}
	
	/*
	 * This method will always group like items together.
	 */
	public Iterator<T> iterator(){
		return new Iterator<T>() {
			Iterator<Wad<T>> wadItr = pile.values().iterator();
			Iterator<T> currentWad = wadItr.next().iterator();
			
			public boolean hasNext() {
				return currentWad.hasNext() || wadItr.hasNext();
			}
			
			public T next() {
				if (currentWad.hasNext())
					return currentWad.next();
				else {
					currentWad = wadItr.next().iterator();
					return currentWad.next();
				}
			}
		};
	}
	
	/*
	 * This is a collection of objects that are equal.
	 * They may or may not be the same.
	 */
	private class Wad<U>{
		private Deque<WrappedItem<U>> list;
		
		Wad(U item){
			list = new LinkedList<WrappedItem<U>>();
			list.push(new WrappedItem<U>(item));
		}
		
		//Ignores items that are not equal to the other items in the Wad.
		void add(U item) {
			if (!list.peek().value.equals(item))
				return;
			else
				list.push(new WrappedItem<U>(item));
		}

		boolean isEmpty() {
			return list.isEmpty();
		}
		
		void removeExactly(U item) {
			for (HashPile<T>.WrappedItem<U> u : list) {
				if (u.value == item) {
					list.remove(u);
				}
			}
		}
		
		int size() {
			return list.size();
		}
		
		U item() {
			return list.peek().value;
		}
		
		boolean contains(U item) {
			for (HashPile<T>.WrappedItem<U> u : list) {
				if (u.value == item) {
					return true;
				}
			}
			return false;
		}
		
		Iterator<U> iterator(){
			return new Iterator<U>() {
				Iterator<WrappedItem<U>> itr = list.iterator();
				
				public boolean hasNext() {
					return itr.hasNext();
				}
				
				public U next() {
					return itr.next().value;
				}
			};
		}
		
	}
	
	private class WrappedItem<V> {
		V value;
		
		WrappedItem(V item){
			value = item;
		}
	}
}
