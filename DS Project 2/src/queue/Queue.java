package queue;

import java.util.Iterator;

public interface Queue<E> extends Iterable<E>, Cloneable {
	
	int size();
	boolean isEmpty(); 
	E first(); 
	E dequeue(); 
	void enqueue(E e);
	
}
