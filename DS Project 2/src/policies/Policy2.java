package policies;

import java.util.ArrayList;
import java.util.Iterator;

import p2MainClasses.Client;
import posts.BasicPost;
import posts.ServicePost;

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
		BasicPost selectedPost = (BasicPost) servicePosts[0];
		int size = selectedPost.clientNum();
		if(serviceStations > 1) {
			for(int i=1; i<serviceStations; i++) {
				if(servicePosts[i].clientNum() < size) {
					selectedPost = (BasicPost) servicePosts[i];
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
			BasicPost tempPost = (BasicPost) servicePosts[i];
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
		if(servicePosts.length > 1) {
			for(int post = 0; post < servicePosts.length; post++) {
				Iterator<Client> iter = ((BasicPost) servicePosts[post]).getQueue().iterator();
				while(iter.hasNext()) {
					Client c = iter.next();
					if(!this.arePostsEmpty()) {
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
}
