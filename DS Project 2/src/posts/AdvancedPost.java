package posts;

import p2MainClasses.Client;
import queue.DLLDeque;
import queue.Deque;

public class AdvancedPost implements ServicePost {
	
	private Deque<Client> postQueue;
	private int ID;
	
	public AdvancedPost(int id) {
		postQueue = new DLLDeque<>();
		this.ID = id;
	}

	public void addToPost(Client customer) {
		postQueue.addLast(customer);	
	}

	public Client removeFromPost() {
		Client ctr = postQueue.removeFirst();
		return ctr;
	}
	
	public Client removeFromLast() {
		Client ctr = postQueue.removeLast();
		return ctr;
	}

//	public boolean serviceCustomer(int service) {
//		postQueue.first().isServed(service);
//		if(postQueue.first().getRemainingTime() == 0) {
//			return true;
//		}
//		return false;
//	}
	
	public void serviceCustomer(int service) {
		postQueue.first().isServed(service);
	}

	public boolean isPostEmpty() {
		return postQueue.isEmpty();
	}

	public int clientNum() {
		return postQueue.size();
	}

	public Client getCurrentClient() {
		return postQueue.first();
	}
	
	public Client getLastClient() {
		return postQueue.last();
	}

	public int getID() {
		return ID;
	}

}
