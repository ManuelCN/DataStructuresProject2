package queue;

import java.util.Iterator;

public class DLLDeque<E> implements Deque<E> {

	private static class Node<E>{
		private E element;
		private Node<E> next;
		private Node<E> prev;
		
		public Node() {
			this(null, null, null);
		}
		
		public Node(E e) {
			this(e, null, null);
		}
		
		public Node(E e, Node<E> n, Node<E> p) {
			element = e;
			next = n;
			prev = p;
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
		
		public Node<E> getPrev() {
			return prev;
		}
		
		public void setPrev(Node<E> p) {
			prev = p;
		}
		
		public void clean() {
			element = null;
			next = null;
			prev = null;
		}
	}
	
	private Node<E> header, trailer;
	private int size;
	
	public DLLDeque() {
		header = new Node<>();
		trailer = new Node<>(null, null, header);
		header.setNext(trailer);
		size = 0;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void addFirst(E e) {
		Node<E> nuevo = new Node<>(e);
		addBetween(nuevo, header, header.getNext());
		size++;
	}

	public void addLast(E e) {
		Node<E> nuevo = new Node<>(e);
		addBetween(nuevo, trailer.getPrev(), trailer);
		size++;		
	}

	public E removeFirst() {
		if(isEmpty()) {	return null;	}
		size--;
		return remove(header.getNext(), header, header.getNext().getNext());
	}

	public E removeLast() {
		if(isEmpty()) {	return null;	}
		size--;
		return remove(trailer.getPrev(), trailer.getPrev().getPrev(), trailer);
	}

	public E first() {
		if(isEmpty()) {	return null;	}
		return header.getNext().getElement();
	}

	public E last() {
		if(isEmpty()) {	return null;	}
		return trailer.getPrev().getElement();
	}
	
	private static <E> void addBetween(Node<E> nuevo, Node<E> prev, Node<E> next) {
		prev.setNext(nuevo);
		nuevo.setPrev(prev);
		nuevo.setNext(next);
		next.setPrev(nuevo);
	}
	private static <E> E remove(Node<E> ntr, Node<E> prev, Node<E> next) {
		E etr = ntr.getElement();
		ntr.clean();
		prev.setNext(next);
		next.setPrev(prev);
		return etr;
	}

	@Override
	public Iterator<E> iterator() {
		return new DequeIterator();
	}
	
	private class DequeIterator implements Iterator<E> {

		private Node<E> current = DLLDeque.this.header.getNext();
		
		@Override
		public boolean hasNext() {
			return current != DLLDeque.this.trailer;
		}

		@Override
		public E next() {
			E currElement = current.getElement();
			current = current.getNext();
			return currElement;
		}
		
	}
}
