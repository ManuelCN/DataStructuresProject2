package policies;

import java.util.ArrayList;

import p2MainClasses.Client;
import queue.Queue;
import queue.SLLQueue;

public class Policy1 extends AbstractPolicy{
	
	private Queue<Client> waitingQueue;
	private static ServicePost[] servicePosts;
	private static int serviceStations;
	private static ArrayList<Client> completedJobs;
	
	public Policy1(int postNum) {
		this.waitingQueue = new SLLQueue<>();
		serviceStations = postNum;
		completedJobs = new ArrayList<>();
		servicePosts = super.initPosts(serviceStations);
	}
	
	public Post getPost(int index) {	return (Post) servicePosts[index];	}
	
	public boolean distribute(Client customer) {
		for(int i=0; i<serviceStations; i++) {
			Post tempPost = (Post) servicePosts[i];
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
			Post tempPost = (Post) servicePosts[i];
			if(!tempPost.isPostEmpty()) {
				System.out.println("Servicing customer " + tempPost.getCurrentClient().getClientID() +
						" at post " + tempPost.getID());
				if(tempPost.serviceCustomer(service)) {
					Client temp = tempPost.removeFromPost();
					temp.setDepartureTime(time);
					System.out.println("Finished servicing customer " + temp.getClientID());
					completedJobs.add(temp);
				}
			}
		}
	}
	
	public boolean arePostsEmpty() {
		for(int i=0; i<serviceStations; i++) {
			if(!servicePosts[i].isPostEmpty()) {	return false;	}
		}
		return true;
	}
	/**
	 * Add the client to the waiting queue if all service posts are currently unavailable.
	 * @param customer	Client to be added to the waiting queue.
	 */
	public void addToWait(Client customer) {
		System.out.println("Added customer " + customer.getClientID() + " to wait queue.");
		waitingQueue.enqueue(customer);
	}
	/**
	 * If the waiting queue is not empty, verify if its first client can be distributed
	 * to a service post. If available, that first client will be moved from the waiting queue
	 * to the service post.
	 */
	public void distributeWait() {
		Client tempClient = waitingQueue.first();
		if(this.distribute(tempClient)) {	waitingQueue.dequeue();	}
	}
	/**
	 * Verifies if the waiting queue is empty.
	 * @return	True if the waiting queue is empty; otherwise, returns False.
	 */
	public boolean isWaitEmpty() {	return waitingQueue.isEmpty();	}

	public ArrayList<Client> getCompletedJobs() {	return completedJobs;	}

}
