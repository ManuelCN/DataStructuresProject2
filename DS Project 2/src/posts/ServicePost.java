package posts;

import p2MainClasses.Client;

public interface ServicePost {
	
	void addToPost(Client customer);
	Client removeFromPost();
	void serviceCustomer(int service);
	boolean isPostEmpty();
	int clientNum();
	Client getCurrentClient();
	int getID();
	
	

}
