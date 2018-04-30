package policies;

import java.util.ArrayList;
import java.util.Iterator;

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
	
	public String getPolicyName() {
		return "SLMS";
	}
	
	public boolean distribute(Client customer, int time) {
		for(int i=0; i<serviceStations; i++) {
			BasicPost tempPost = (BasicPost) servicePosts[i];
			if(tempPost.isPostEmpty()) {
				tempPost.addToPost(customer);
				tempPost.getCurrentClient().setServiceInitTime(time);
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
					tempPost.getCurrentClient().receivingService();
				}
				tempPost.serviceCustomer(service);
				if(tempPost.getCurrentClient().getRemainingTime() == 0) {
					Client temp = tempPost.removeFromPost();
					completedJobs.add(temp);
				}
			}
		}
	}
	
	public void addToWait(Client customer) {
		waitingQueue.enqueue(customer);
	}
	
	public void distributeWait(int time) {
		Client tempClient = waitingQueue.first();
		while(this.distribute(tempClient, time)) {
			waitingQueue.dequeue();
			if(!waitingQueue.isEmpty()) {
				tempClient = waitingQueue.first();
			} else {
				break;
			}
		}

	}
	
	public boolean isWaitEmpty() {	return waitingQueue.isEmpty();	}

	public void checkOverpass() {
		if(servicePosts.length > 1) {
			Iterator<Client> iter = waitingQueue.iterator();
			while(iter.hasNext()) {
				Client c = iter.next();
				for(int post=0; post<servicePosts.length; post++) {
					if(!servicePosts[post].isPostEmpty()) {
						if(servicePosts[post].getCurrentClient() != c) {
							Client temp = servicePosts[post].getCurrentClient();
							if(!temp.isBeingServed() && (temp.getArrivalTime() > c.getArrivalTime())) {
								c.overpassed();
							}
						}
					}
				}
			}
		}
	}
}
