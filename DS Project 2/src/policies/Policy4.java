package policies;

import java.util.ArrayList;
import java.util.Iterator;

import p2MainClasses.Client;
import posts.AdvancedPost;
import posts.ServicePost;
/**
 * Multiple Lines Multiple Servers and Balanced Waiting Times (MLMLBWT)
 * In this policy, each post has its own waiting queue.
 * As clients arrive, they are distributed to the lowest index post which is either empty or 
 * has the lowest expected waiting time with respect to the others.
 * @author	Manuel E. Castañeda
 * 			802-15-1272
 * 			Section 090
 */
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
		AdvancedPost selectedPost = (AdvancedPost) this.getPost(postIndex);
		selectedPost.addToPost(customer);
		monitor.addPostTime(postIndex, customer.getRemainingTime());
		if(selectedPost.getCurrentClient() == customer) {
			customer.setServiceInitTime(time);
		}
		return true;
	}

	public void provideService(int time, int service) {
		for(int i=0; i<serviceStations; i++) {
			AdvancedPost tempPost = (AdvancedPost) this.getPost(i);
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
		if(serviceStations > 1) {
			for(int post = 0; post < serviceStations; post++) {
				Iterator<Client> iter = ((AdvancedPost)this.getPost(post)).getQueue().iterator();
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
	/**
	 * The monitor will store the expected waiting time for each post.
	 * When a client arrives, will always choose the post with the lowest index
	 * and expected waiting time.
	 * @author Manuel E. Castañeda
	 *
	 */
	private static class Monitor {
		
		private int[] queueTimes;
		
		public Monitor(int servicePosts) {
			queueTimes = new int[servicePosts];
			for(int i=0; i<queueTimes.length; i++) {
				queueTimes[i] = 0;
			}
		}
		/**
		 * Will increase or decrease the selected post's expected waiting time.
		 * @param index	The selected post's index.
		 * @param time	Will increase (positive number) or decrease (negative number) the selected
		 * post's expected waiting time.
		 */
		public void addPostTime(int index, int time) {
			queueTimes[index] += time;
		}
		/**
		 * @param index	The selected post's index.
		 * @return	Returns the expected waiting time for the selected post.
		 */
		private int getPostTime(int index) {
			return queueTimes[index];
		}
		/**
		 * @return	Returns the lowest expected waiting time post's index.
		 */
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
