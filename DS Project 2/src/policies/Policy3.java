package policies;

import java.util.ArrayList;
import java.util.Iterator;

import p2MainClasses.Client;
import posts.AdvancedPost;
import posts.BasicPost;
import posts.ServicePost;
/**
 * Multiple Lines Multiple Servers and Balanced Line Lengths (MLMLBLL)
 * In this policy, each post has its own waiting queue.
 * As clients arrive, they are distributed to the lowest index post which is either empty or has
 * the least amount of clients.
 * As service is provided, if a post becomes empty or its queue length decreases enough to benefit
 * the last customer of another post's waiting, then that last customer will be transferred to the
 * specified post.
 * @author	Manuel E. Castañeda
 * 			802-15-1272
 * 			Section 090
 */
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
		AdvancedPost selectedPost = (AdvancedPost) this.getPost(0);
		int size = selectedPost.clientNum();
		int index = 0;
		if(serviceStations > 1) {
			for(int i=1; i<serviceStations; i++) {
				if(this.getPost(i).clientNum() < size) {
					selectedPost = (AdvancedPost) this.getPost(i);
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
			AdvancedPost tempPost = (AdvancedPost) this.getPost(i);
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
	 * This Monitor allows the transfer of a client from one post to the other.
	 * Determines when a client can benefit from a transfer, the post to which
	 * said client belongs and the destination post of the transfer.
	 * Will always transfer the last client of any queue, by arrival time priority.
	 * @author Manuel E. Castañeda
	 *
	 */
	private static class Monitor {
		
		private int[] queueLengths;
		public Monitor(int servicePosts) {
			queueLengths = new int[servicePosts];
		}
		/**
		 * Sets the chosen post's queue length.
		 * @param index	Index of the selected post.
		 * @param length	New length for the selected post.
		 */
		public void setPostLength(int index, int length) {
			queueLengths[index] = length;
		}
		/**
		 * @param index	The selected post's index.
		 * @return	Returns the selected post's queue length.
		 */
		public int getPostLength(int index) {
			return queueLengths[index];
		}
		/**
		 * Moves a customer from its original post to its target post.
		 * @param original	The post from where the transferred client will come from.
		 * @param target	The post where the transferred client will be moved to.
		 */
		public void moveClient(AdvancedPost original, AdvancedPost target) {
			Client ctm = original.removeFromLast();
			target.addToPost(ctm);
			int originalNewLength = original.clientNum();
			int targetNewLength = target.clientNum();
			this.setPostLength(original.getID()-1, originalNewLength);
			this.setPostLength(target.getID()-1, targetNewLength);
		}
		/**
		 * @return	Returns whether all posts have the same length (true) or if at least one
		 * has a different length than the others (false).
		 */
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
		/**
		 * @return	Returns the smallest length post. Will always choose the one with the biggest index.
		 */
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
		/**
		 * @return	Returns the largest length post. Will always choose the one with the lowest index.
		 */
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
