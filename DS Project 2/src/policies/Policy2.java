package policies;

import java.util.ArrayList;

import p2MainClasses.Client;
import posts.BasicPost;
import posts.ServicePost;

public class Policy2 extends AbstractPolicy {
	
	public Policy2(int postNum) {
		serviceStations = postNum;
		completedJobs = new ArrayList<>();
		servicePosts = initBasicPosts(serviceStations);
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
		System.out.println("Added client " + customer.getClientID() +
				" to service post " + selectedPost.getID());
		selectedPost.addToPost(customer);
		return true;
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
	}

	public void distributeWait(int time) {
	}

	public boolean isWaitEmpty() {
		return this.arePostsEmpty();
	}
}
