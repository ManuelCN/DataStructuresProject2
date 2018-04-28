package policies;

import java.util.ArrayList;

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

	public boolean distribute(Client customer, int time) {
		int postIndex = monitor.minWaitPost();
		AdvancedPost selectedPost = (AdvancedPost) servicePosts[postIndex];
		System.out.println("Added client " + customer.getClientID() +
				" to service post " + selectedPost.getID());
		selectedPost.addToPost(customer);
		monitor.addPostTime(postIndex, customer.getRemainingTime());
		return true;
	}

	public void provideService(int time, int service) {
		int index=0;
		for(int i=0; i<serviceStations; i++) {
			AdvancedPost tempPost = (AdvancedPost) servicePosts[i];
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
				monitor.addPostTime(i, -service);
				tempPost.serviceCustomer(service);
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
