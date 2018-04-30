package queue;

import java.util.Iterator;

public interface Deque<E> extends Iterable<E> {
	void addFirst(E e);
	void addLast(E e);
	E removeFirst();
	E removeLast();
	E first();
	E last();
	int size();
	boolean isEmpty();
	Iterator<E> iterator();
}
