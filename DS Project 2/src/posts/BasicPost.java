package posts;

import p2MainClasses.Client;
import queue.Queue;
import queue.SLLQueue;

public class BasicPost implements ServicePost {
	
	private Queue<Client> postQueue;
	private int ID;
	
	public BasicPost(int id) {
		postQueue = new SLLQueue<>();
		this.ID = id;
	}

	public void addToPost(Client customer) {
		postQueue.enqueue(customer);
	}

	public Client removeFromPost() {
		Client ctr = postQueue.dequeue();
		return ctr;
	}
	
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
	
	public Queue<Client> getQueue() {
		 return postQueue;
	}
	
	public int getID() {
		return ID;
	}

}
