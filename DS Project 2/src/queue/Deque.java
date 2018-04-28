package queue;

public interface Deque<E> {
	void addFirst(E e);
	void addLast(E e);
	E removeFirst();
	E removeLast();
	E first();
	E last();
	int size();
	boolean isEmpty();
}
