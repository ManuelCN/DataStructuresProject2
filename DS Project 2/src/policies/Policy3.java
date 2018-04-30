package policies;

import java.util.ArrayList;
import java.util.Iterator;

import p2MainClasses.Client;
import posts.AdvancedPost;
import posts.BasicPost;
import posts.ServicePost;

public class Policy3 extends AbstractPolicy {

	private static Monitor monitor;
	
	public Policy3(int postNum) {
		serviceStations = postNum;
		monitor = new Monitor(serviceStations);
		completedJobs = new ArrayList<>();
		servicePosts = initAdvancedPosts(serviceStations);
	}
	
	public String getPolicyName() {
		return "MLMSBLL";
	}

	public boolean distribute(Client customer, int time) {
		AdvancedPost selectedPost = (AdvancedPost) servicePosts[0];
		int size = selectedPost.clientNum();
		int index = 0;
		if(serviceStations > 1) {
			for(int i=1; i<serviceStations; i++) {
				if(servicePosts[i].clientNum() < size) {
					selectedPost = (AdvancedPost) servicePosts[i];
					size = selectedPost.clientNum();
					index = i;
				}
			}
		}
		selectedPost.addToPost(customer);
		monitor.setPostLength(index, selectedPost.clientNum());
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
		if(!isWaitEmpty()) {;
			if(!monitor.postsHaveSameLength()) {
				int minIndex = monitor.minLengthPost();
				int maxIndex = monitor.maxLengthPost();
				if(maxIndex < minIndex) {
					AdvancedPost minLenPost = (AdvancedPost) this.getPost(minIndex);
					AdvancedPost maxLenPost = (AdvancedPost) this.getPost(maxIndex);
					Client temp = maxLenPost.getLastClient();
					if(maxLenPost.clientNum() > 1 && !temp.isBeingServed()) {
						monitor.moveClient(maxLenPost, minLenPost);
					}
				}
			}
		}
		
	}

	public boolean isWaitEmpty() {
		return this.arePostsEmpty();
	}
	
	public void checkOverpass() {
		if(servicePosts.length > 1) {
			for(int post = 0; post < servicePosts.length; post++) {
				Iterator<Client> iter = ((AdvancedPost) servicePosts[post]).getQueue().iterator();
				while(iter.hasNext()) {
					Client c = iter.next();
					if(!this.arePostsEmpty()) {
						for(int i=0; i<servicePosts.length; i++) {
							if(!servicePosts[i].isPostEmpty()) {
								if(servicePosts[i].getCurrentClient() != c) {
									Client temp = servicePosts[i].getCurrentClient();
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
	
	private static class Monitor {
		
		private int[] queueLengths;
		public Monitor(int servicePosts) {
			queueLengths = new int[servicePosts];
		}
		
		public void setPostLength(int index, int length) {
			queueLengths[index] = length;
		}
		
		public int getPostLength(int index) {
			return queueLengths[index];
		}
		
		public void moveClient(AdvancedPost original, AdvancedPost target) {
			Client ctm = original.removeFromLast();
			target.addToPost(ctm);
			int originalNewLength = original.clientNum();
			int targetNewLength = target.clientNum();
			this.setPostLength(original.getID()-1, originalNewLength);
			this.setPostLength(target.getID()-1, targetNewLength);
		}
		
		public boolean postsHaveSameLength() {
			int size = queueLengths[0];
			if(queueLengths.length > 1) {
				for(int i=1; i<queueLengths.length; i++) {
					if(getPostLength(i) != size) {
						return false;
					}
				}
			}
			return true;		
		}
		
		public int minLengthPost() {
			int index = 0;
			int minSize = queueLengths[index];
			if(queueLengths.length > 1) {
				for(int i=index + 1; i<queueLengths.length; i++) {
					if(getPostLength(i) <= minSize) {
						index = i;
						minSize = getPostLength(index);
					}
				}
			}
			return index;
		}
		
		public int maxLengthPost() {
			int index = 0;
			int maxSize = queueLengths[index];
			if(queueLengths.length > 1) {
				for(int i=index + 1; i<queueLengths.length; i++) {
					if(getPostLength(i) > maxSize) {
						index = i;
						maxSize = getPostLength(i);
					}
				}
			}
			return index;
		}
	}

}
