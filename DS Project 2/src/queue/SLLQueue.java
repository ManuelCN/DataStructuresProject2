package queue;

public class SLLQueue<E> implements Queue<E> {

	private static class Node<E>{
		private E element;
		private Node<E> next;
		
		public Node() {
			this(null, null);
		}
		
		public Node(E e) {
			this(e, null);
		}
		
		public Node(E e, Node<E> n) {
			element = e;
			next = n;
		}
		
		public E getElement() {
			return element;
		}
		
		public void setElement(E e) {
			element = e;
		}
		
		public Node<E> getNext(){
			return next;
		}
		
		public void setNext(Node<E> n) {
			next = n;
		}
		
		public void clean() {
			element = null;
			next = null;
		}
	}
	
	private Node<E> first, last;
	private int size;
	
	public SLLQueue() {
		first = last = null;
		size = 0;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public E first() {
		if(isEmpty()) {	return null;	}
		return first.getElement();
	}
	
	public E second() {
		if(size == 1) {
			return null;
		}
		return first.getNext().getElement();
	}

	public void enqueue(E element) {
		Node<E> nuevo = new Node<>(element);
		if(size == 0) {
			first = last = nuevo;
		} else {
			last.setNext(nuevo);
			last = nuevo;
		}
		size++;
	}

	public E dequeue() {
		if(isEmpty()) {	return null;	}
		Node<E> ntr = first;
		E etr = ntr.getElement();
		first = ntr.getNext();
		ntr.clean();
		size--;
		return etr;
	}
}
