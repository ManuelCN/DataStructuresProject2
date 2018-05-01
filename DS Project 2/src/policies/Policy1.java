package policies;

import java.util.ArrayList;
import java.util.Iterator;

import p2MainClasses.Client;
import posts.BasicPost;
import posts.ServicePost;
import queue.Queue;
import queue.SLLQueue;
/**
 * Single Line Multiple Servers (SLMS)
 * In this policy, there is only one common waiting line.
 * If an arriving client can be distributed to an empty post, it will be assigned to the first empty post.
 * The post chosen will always the lowest index possible.
 * If the client cannot be distributed when it arrives, it will join the waiting queue.
 * As soon as a post is available, the first client of the waiting queue will then be distributed.
 * @author	Manuel E. Castañeda
 * 			802-15-1272
 * 			Section 090
 */
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
			BasicPost tempPost = (BasicPost) this.getPost(i);
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
		if(serviceStations > 1) {
			Iterator<Client> iter = waitingQueue.iterator();
			while(iter.hasNext()) {
				Client c = iter.next();
				for(int post=0; post<serviceStations; post++) {
					if(!this.getPost(post).isPostEmpty()) {
						Client temp = this.getPost(post).getCurrentClient();
						if(temp != c) {
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
