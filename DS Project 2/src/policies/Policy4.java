package policies;

import java.util.ArrayList;
import java.util.Iterator;

import p2MainClasses.Client;
import posts.AdvancedPost;
import posts.ServicePost;

public class Policy4 extends AbstractPolicy {

	private static Monitor monitor;
	
	public Policy4(int postNum) {
		serviceStations = postNum;
		monitor = new Monitor(serviceStations);
		completedJobs = new ArrayList<>();
		servicePosts = initAdvancedPosts(serviceStations);
	}
	
	public String getPolicyName() {
		return "MLMSBWT";
	}

	public boolean distribute(Client customer, int time) {
		int postIndex = monitor.minWaitPost();
		AdvancedPost selectedPost = (AdvancedPost) servicePosts[postIndex];
		System.out.println("Added client " + customer.getClientID() +
				" to service post " + selectedPost.getID());
		selectedPost.addToPost(customer);
		monitor.addPostTime(postIndex, customer.getRemainingTime());
		if(selectedPost.getCurrentClient() == customer) {
			customer.setServiceInitTime(time);
		}
		return true;
	}

	public void provideService(int time, int service) {
		for(int i=0; i<serviceStations; i++) {
			AdvancedPost tempPost = (AdvancedPost) servicePosts[i];
			if(!tempPost.isPostEmpty()) {
				if(!tempPost.getCurrentClient().isBeingServed()) {
					tempPost.getCurrentClient().receivingService();
				}
				monitor.addPostTime(i, -service);
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
		if(!this.arePostsEmpty()) {
			for(int post = 0; post < servicePosts.length; post++) {
				if(!servicePosts[post].isPostEmpty()) {
					Iterator<Client> iter = ((AdvancedPost) servicePosts[post]).getQueue().iterator();
					while(iter.hasNext()) {
						Client c = iter.next();
						if(c != servicePosts[post].getCurrentClient()) {
							Client temp = servicePosts[post].getCurrentClient();
							if(!temp.isBeingServed() && (temp.getArrivalTime() > c.getArrivalTime())) {
								c.overpassed();
							}
						}
					}
				}
			}
		}
			
			
//			for(int post = 0; post < servicePosts.length; post++) {
//				Iterator<Client> iter = ((AdvancedPost) servicePosts[post]).getQueue().iterator();
//				while(iter.hasNext()) {
//					Client c = iter.next();
//					for(int i=post + 1; i<servicePosts.length; i++) {
//						if(!servicePosts[i].isPostEmpty()) {
//							Client temp = servicePosts[i].getCurrentClient();
//							if(temp.getArrivalTime() > c.getArrivalTime() && !temp.isBeingServed() && !c.isBeingServed()) {
//								c.overpassed();
//							}
//						}
//					}
//				}
//			}
	}
	
	private static class Monitor {
		
		private int[] queueTimes;
		
		public Monitor(int servicePosts) {
			queueTimes = new int[servicePosts];
			for(int i=0; i<queueTimes.length; i++) {
				queueTimes[i] = 0;
			}
		}
		
		public void addPostTime(int index, int time) {
			queueTimes[index] += time;
		}
		
		private int getPostTime(int index) {
			return queueTimes[index];
		}
		
		public int minWaitPost() {
			int index=0;
			int minWait = queueTimes[index];
			if(queueTimes.length > 1) {
				for(int i=index + 1; i<queueTimes.length; i++) {
					if(getPostTime(i) < minWait) {
						index = i;
						minWait = getPostTime(i);
					}
				}
			}
			return index;
		}	
	}

	
}
