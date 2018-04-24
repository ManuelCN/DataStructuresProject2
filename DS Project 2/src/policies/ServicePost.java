package policies;

import p2MainClasses.Client;

public interface ServicePost {
	
	void addToPost(Client customer);
	Client removeFromPost();
	boolean serviceCustomer(int service);
	boolean isPostEmpty();
	int clientNum();
	Client getCurrentClient();
	int getID();
	
	

}
