package policies;

import java.util.ArrayList;

import p2MainClasses.Client;

public class Policy3 extends AbstractPolicy {
	
	private static ServicePost[] servicePosts;
	private static int serviceStations;
	private static ArrayList<Client> completedJobs;
	
	public Policy3(int postNum) {
		
	}

	@Override
	public Post getPost(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean distribute(Client customer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void provideService(int time, int service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean arePostsEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addToWait(Client customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distributeWait() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isWaitEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Client> getCompletedJobs() {
		// TODO Auto-generated method stub
		return null;
	}

}
