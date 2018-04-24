package policies;

public abstract class AbstractPolicy implements Policies {

	protected ServicePost[] initPosts(int serviceStations) {
		ServicePost[] servicePosts = new Post[serviceStations];
		for(int i=0; i<serviceStations; i++) {
			servicePosts[i] = new Post(i+1);
		}
		return servicePosts;
	}
	
}
