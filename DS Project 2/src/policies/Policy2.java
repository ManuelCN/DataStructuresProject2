package policies;

import java.util.ArrayList;

import p2MainClasses.Client;

public class Policy2 extends AbstractPolicy {

	private static ServicePost[] servicePosts;
	private static int serviceStations;
	private static ArrayList<Client> completedJobs;
	
	public Policy2(int postNum) {
		serviceStations = postNum;
		completedJobs = new ArrayList<>();
		servicePosts = initPosts(serviceStations);
	}
	
	public Post getPost(int index) {	return (Post) servicePosts[index];	}

	public boolean distribute(Client customer) {
		Post selectedPost = (Post) servicePosts[0];
		int size = selectedPost.clientNum();
		if(serviceStations > 1) {
			for(int i=1; i<serviceStations; i++) {
				if(servicePosts[i].clientNum() < size) {
					selectedPost = (Post) servicePosts[i];
					size = selectedPost.clientNum();
				}
			}
		}
		System.out.println("Added client " + customer.getClientID() +
				" to service post " + selectedPost.getID());
		selectedPost.addToPost(customer);
		return true;
	}

	@Override
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

	@Override
	public boolean arePostsEmpty() {
		for(int i=0; i<serviceStations; i++) {
			if(!servicePosts[i].isPostEmpty()) {	return false;	}
		}
		return true;
	}

	public void addToWait(Client customer) {
	}

	public void distributeWait() {
	}

	public boolean isWaitEmpty() {
		return this.arePostsEmpty();
	}

	@Override
	public ArrayList<Client> getCompletedJobs() {
		return completedJobs;
	}

}
