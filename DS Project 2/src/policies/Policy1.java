package policies;

import java.util.ArrayList;

import p2MainClasses.Client;
import posts.BasicPost;
import posts.ServicePost;
import queue.Queue;
import queue.SLLQueue;

public class Policy1 extends AbstractPolicy{
	
	private Queue<Client> waitingQueue;
	
	public Policy1(int postNum) {
		this.waitingQueue = new SLLQueue<>();
		serviceStations = postNum;
		completedJobs = new ArrayList<>();
		servicePosts = super.initBasicPosts(serviceStations);
	}
	
	public boolean distribute(Client customer, int time) {
		for(int i=0; i<serviceStations; i++) {
			BasicPost tempPost = (BasicPost) servicePosts[i];
			if(tempPost.isPostEmpty()) {
				System.out.println("Added client " + customer.getClientID() +
						" to service post " + tempPost.getID());
				tempPost.addToPost(customer);
				return true;
			}
		}
		return false;
	}
	
	public void provideService(int time, int service) {
		for(int i=0; i<serviceStations; i++) {
			BasicPost tempPost = (BasicPost) servicePosts[i];
			if(!tempPost.isPostEmpty()) {
				if(!tempPost.getCurrentClient().isBeingServed()) {
					System.out.println("Began servicing customer " + tempPost.getCurrentClient().getClientID() +
							" at post " + tempPost.getID());
					tempPost.getCurrentClient().receivingService();
					tempPost.getCurrentClient().setServiceInitTime(time);
				} else {
					System.out.println("Servicing customer " + tempPost.getCurrentClient().getClientID() +
							" at post " + tempPost.getID());
				}
				tempPost.serviceCustomer(service);
			}
		}
	}
	
	public void addToWait(Client customer) {
		System.out.println("Added customer " + customer.getClientID() + " to wait queue.");
		waitingQueue.enqueue(customer);
	}
	
	public void distributeWait(int time) {
		Client tempClient = waitingQueue.first();
		if(this.distribute(tempClient, time)) {	waitingQueue.dequeue();	}
	}
	
	public boolean isWaitEmpty() {	return waitingQueue.isEmpty();	}
}
