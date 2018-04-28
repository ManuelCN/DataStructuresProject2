package policies;

import java.util.ArrayList;

import p2MainClasses.Client;
import posts.AdvancedPost;
import posts.BasicPost;
import posts.ServicePost;
import queue.Queue;

public abstract class AbstractPolicy implements Policies {

	protected static ServicePost[] servicePosts;
	protected static int serviceStations;
	protected static ArrayList<Client> completedJobs;

	protected ServicePost[] initBasicPosts(int serviceStations) {
		ServicePost[] servicePosts = new BasicPost[serviceStations];
		for(int i=0; i<serviceStations; i++) {
			servicePosts[i] = new BasicPost(i+1);
		}
		return servicePosts;
	}
	
	protected ServicePost[] initAdvancedPosts(int serviceStations) {
		ServicePost[] servicePosts = new AdvancedPost[serviceStations];
		for(int i=0; i<serviceStations; i++) {
			servicePosts[i] = new AdvancedPost(i+1);
		}
		return servicePosts;
	}
	
	public ServicePost getPost(int index) {	
		return servicePosts[index];
	}
	
	public void removeCompleted(int time) {
		for(int i=0; i<servicePosts.length; i++) {
			ServicePost tempPost =  this.getPost(i);
			if(!tempPost.isPostEmpty()) {
				if(tempPost.getCurrentClient().getRemainingTime() == 0) {
					Client temp = tempPost.removeFromPost();
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
	
	public ArrayList<Client> getCompletedJobs() {
		return completedJobs;
	}
	
}
