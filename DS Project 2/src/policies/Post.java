package policies;

import p2MainClasses.Client;
import queue.Queue;
import queue.SLLQueue;

public class Post implements ServicePost {
	
	private Queue<Client> postQueue;
	private int ID;
	private int customers;
	
	public Post(int id) {
		postQueue = new SLLQueue<>();
		this.ID = id;
		customers = 0;
	}

	@Override
	public void addToPost(Client customer) {
		postQueue.enqueue(customer);
		customers++;
	}

	@Override
	public Client removeFromPost() {
		Client ctr = postQueue.dequeue();
		customers--;
		return ctr;
	}

	@Override
	public boolean serviceCustomer(int service) {
		postQueue.first().isServed(service);
		if(postQueue.first().getRemainingTime() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isPostEmpty() {
		return postQueue.isEmpty();
	}
	
	public int clientNum() {
		return postQueue.size();
	}
	
	public Client getCurrentClient() {
		return postQueue.first();
	}
	
	public int getID() {
		return ID;
	}

}
