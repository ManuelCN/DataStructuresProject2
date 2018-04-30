package policies;

import java.util.ArrayList;
import java.util.Iterator;

import p2MainClasses.Client;
import posts.BasicPost;
import posts.ServicePost;
/**
 * Multiple Lines Multiple Servers (MLMS)
 * In this policy, each post has its own waiting queue.
 * When a client arrives, it will be distributed to the lowest index post which is either empty
 * or has the least amount of customers in its waiting queue with respect to the other posts
 * (if any).
 * @author Manuel E. Castañeda
 *
 */
public class Policy2 extends AbstractPolicy {
	
	public Policy2(int postNum) {
		serviceStations = postNum;
		completedJobs = new ArrayList<>();
		servicePosts = initBasicPosts(serviceStations);
	}
	
	public String getPolicyName() {
		return "MLMS";
	}

	public boolean distribute(Client customer, int time) {
		BasicPost selectedPost = (BasicPost) this.getPost(0);
		int size = selectedPost.clientNum();
		if(serviceStations > 1) {
			for(int i=1; i<serviceStations; i++) {
				if(this.getPost(i).clientNum() < size) {
					selectedPost = (BasicPost) this.getPost(i);
					size = selectedPost.clientNum();
				}
			}
		}
		selectedPost.addToPost(customer);
		if(selectedPost.getCurrentClient() == customer) {
			customer.setServiceInitTime(time);
		}
		return true;
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
					if(!tempPost.isPostEmpty()) {
						tempPost.getCurrentClient().setServiceInitTime(time);
					}
				}
			}
		}
	}


	public void addToWait(Client customer) {
	}

	public void distributeWait(int time) {
	}

	public boolean isWaitEmpty() {
		return this.arePostsEmpty();
	}
	
	public void checkOverpass() {
		if(serviceStations > 1) {
			for(int post = 0; post < serviceStations; post++) {
				Iterator<Client> iter = ((BasicPost) this.getPost(post)).getQueue().iterator();
				while(iter.hasNext()) {
					Client c = iter.next();
					if(!this.arePostsEmpty()) {
						for(int i=0; i<serviceStations; i++) {
							if(!this.getPost(i).isPostEmpty()) {
								Client temp = this.getPost(i).getCurrentClient();
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
	}
}
