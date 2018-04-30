package policies;

import java.util.ArrayList;

import p2MainClasses.Client;
import posts.ServicePost;
import queue.Queue;

public interface Policies {
	
	/**
	 * @return	Returns the name of the policy used.
	 */
	String getPolicyName();
	/**
	 * Returns one of the service posts.
	 * @param index	The index of the service post to be returned.
	 * @return	A service post.
	 */
	ServicePost getPost(int index);
	/**
	 * Adds a client to a service post.
	 * @param customer	The client to be added to the service post.
	 * @param time	Time unit at which the client tried to be distributed.
	 * @return	True if client was successfully added to a service post;
	 * if otherwise, returns False.
	 */
	boolean distribute(Client customer, int time);
	/**
	 * For each post that is not empty, provide service for the amount of time specified 
	 * to their current first customer.
	 * @param time	The time at which the service-given event was done. Necessary to
	 * set the departure time of a client if their service has been completed.
	 * @param service	The amount of service time to be given.
	 */
	void provideService(int time, int service);
	/**
	 * Verifies each service post to see whether or not they are empty.
	 * @return	True if all posts are empty. False if at least one service post is not empty.
	 */
	boolean arePostsEmpty();
	/**
	 * Add the client to the waiting queue if all service posts are currently unavailable.
	 * @param customer	Client to be added to the waiting queue.
	 */
	void addToWait(Client customer);
	/**
	 * If the waiting queue is not empty, verify if its first client can be distributed
	 * to a service post. If available, that first client will be moved from the waiting queue
	 * to the service post.
	 * @param time	Time at which the customers in the waiting queue are redistributed.
	 */
	void distributeWait(int time);
	/**
	 * Verifies if the waiting queue is empty.
	 * @return	True if the waiting queue is empty; otherwise, returns False.
	 */
	boolean isWaitEmpty();
	/**
	 * For each of the customers, checks the amount of other customers that arrived after
	 * them but began receiving service before them.
	 */
	void checkOverpass();
	/**
	 * As the clients are done being serviced, they are added to a completed jobs ArrayList.
	 * @return	The ArrayList containing all the clients that were serviced in the simulation.
	 */
	ArrayList<Client> getCompletedJobs();	
}
