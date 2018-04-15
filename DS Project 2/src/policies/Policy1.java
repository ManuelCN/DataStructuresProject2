package policies;

import java.util.ArrayList;

import p2MainClasses.Client;
import queue.Queue;
import queue.SLLQueue;
/**
 * SINGLE LINE, MULTIPLE SERVERS
 * One waiting line only. First person in line will be assigned to first empty post.
 * If no post is empty, person will remain in line.
 * If more than one post is available, person will be assigned to lowest index post.
 * @author manny
 *
 */
public class Policy1 {
	
	private Queue<Client> clientQueue;
	private Queue<Client> waitingQueue;
	private static Queue<Client>[] servicePosts;
	private int serviceStations;
	
	
	public Policy1(Queue<Client> cq, int ss) {
		this.clientQueue = cq;
		this.waitingQueue = new SLLQueue<>();
		this.serviceStations = ss;
	}
	
	public ArrayList<Client> runSimulation(){
		
		servicePosts = new SLLQueue[serviceStations];
		for(int i = 0; i < servicePosts.length; i++) {
			servicePosts[i] = new SLLQueue<>();
		}
		
		ArrayList<Client>terminatedJobs = new ArrayList<>();
		
		int timeUnit = 1;
		boolean done = false;
		while(!done) {
			System.out.println("Time unit: " + timeUnit);
			if(!clientQueue.isEmpty()) {
				Client tempClient = clientQueue.first();

				if(tempClient.getArrivalTime() == timeUnit) {
					System.out.println("Added client " + tempClient.getClientID() + " to waiting queue at " + timeUnit + " time units!");
					System.out.println(tempClient);
					waitingQueue.enqueue(clientQueue.dequeue());
				}
			}
			
			if(!waitingQueue.isEmpty()) {
				Client tempClient = waitingQueue.first();
				for(int i=0; i<servicePosts.length; i++) {
					if(servicePosts[i].isEmpty()) {
						System.out.println("Added client " + tempClient.getClientID() + " to service post " + (i+1) + "!");
						servicePosts[i].enqueue(waitingQueue.dequeue());
						break;
					}
				}
			}
			
			for(int i=0; i<servicePosts.length; i++) {
				if(!servicePosts[i].isEmpty()) {
					Client tempClient = servicePosts[i].first();
					System.out.println("Servicing client " + tempClient.getClientID() + " at service post " + (i+1) + "!");
					tempClient.isServed(1);
					System.out.println("Current client remaining time: " + tempClient.getRemainingTime());
					if(tempClient.getRemainingTime() == 0) {
						System.out.println("Finished servicing client " + tempClient.getClientID() + "!");
						tempClient.setDepartureTime(timeUnit);
						terminatedJobs.add(servicePosts[i].dequeue());
					}
				}
			}
			
			int donePosts = 0;
			for(int i=0; i<servicePosts.length; i++) {
				if(waitingQueue.isEmpty() && servicePosts[i].isEmpty()) {
					donePosts++;
				}
			}
			
			if(donePosts == servicePosts.length || timeUnit == 100) {
				System.out.println("All posts finished!");
				done = true;
			}
			timeUnit++;
			System.out.println();
		}
		
		return terminatedJobs;
	}

}
