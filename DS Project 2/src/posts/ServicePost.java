package posts;

import p2MainClasses.Client;
import queue.Queue;
/**
 * Interface for service posts. Will do work on clients, like providing service,
 * adding or removing from the queue, and will return information regarding
 * the process.
 * @author Manuel E. Castañeda
 *
 */
public interface ServicePost {
	/**
	 * Adds a client to the service post's queue.
	 * @param customer	The client to be added.
	 */
	void addToPost(Client customer);
	/**
	 * Removes a client from the service post's queue.
	 * @return	The removed client.
	 */
	Client removeFromPost();
	/**
	 * Provides service to the post's first client (if any) for the given amount of time.
	 * @param service	The amount of service time to be provided.
	 */
	void serviceCustomer(int service);
	/**
	 * @return	Returns whether the post is empty (true) or not (false).
	 */
	boolean isPostEmpty();
	/**
	 * @return	Returns the amount of clients in the service post's queue.
	 */
	int clientNum();
	/**
	 * @return	Returns the post's current client. In other words, the first client in
	 * the service post's queue.
	 */
	Client getCurrentClient();
	/**
	 * @return	Returns the service post's id.
	 */
	int getID();
	
	

}
