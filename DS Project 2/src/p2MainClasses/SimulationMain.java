package p2MainClasses;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import dataGenerator.DataReader;
import policies.AbstractPolicy;
import policies.Policies;
import policies.Policy1;
import policies.Policy2;
import policies.Policy3;
import policies.Policy4;
import posts.ServicePost;
import queue.Queue;
import queue.SLLQueue;

public class SimulationMain {
	
	private static DataReader reader;
	private static Integer[] readData;
	private static int serviceStations;

	public static void main(String[] args) {
		
		serviceStations = 1;
		
		try {
			reader = new DataReader();
			int file = 0;
			readData = (Integer[]) reader.readDataFiles(file);
			printDataArray(readData);
			System.out.println("Working with File " + file);
			double averageWaiting = 0;
			double averageOverpasses = 0;
			int completedTime = 0;
			if(readData.length == 0) {
				System.out.println("Input file does not meet the expected format or it is empty.");
			} else {
				SLLQueue<Client> clientQueue = fillClientQueue(readData);
				AbstractPolicy policy = new Policy4(serviceStations);
				SLLQueue<Client> policyClientQueue = clientQueue.clone();
				
				int time = policyClientQueue.first().getArrivalTime();
				int diffTime = time - 1;
				boolean complete = false;
				while(!complete) {
					int servTime = time - diffTime;
					System.out.println("------------ Time: " + time + " ------------");
					//Transfer Events, if any
					if(!policy.isWaitEmpty()) {			policy.distributeWait(time);			}
					//Service-Provided Events (check for Service-Completed Events)
					if(!policy.arePostsEmpty()) {		policy.provideService(time, servTime);	}
					//Arrival Events
					if(!policyClientQueue.isEmpty()) {
						Client tempClient = policyClientQueue.first();
						if(tempClient.getArrivalTime() == time) {
							if(!policy.distribute(tempClient, time)) {
								policy.addToWait(tempClient);
							}
							policyClientQueue.dequeue();
						}
					}
						
					if(!policy.isWaitEmpty() || !policy.arePostsEmpty()) {
						policy.checkOverpass();
					}
					//If everything is empty, simulation is done.
					if(policyClientQueue.isEmpty() && policy.isWaitEmpty() && policy.arePostsEmpty()) {
						complete = true;
						completedTime = time;
					}
					
//					if(time >= 100 || time < 0) {
//						complete = true;
//						System.out.println("DEBUG THIS");
//					}
					diffTime = time;
					time = getNextTime(time, policyClientQueue, policy);
				} //Closing simulation loop.
				System.out.println();
				
				//Gets the completed jobs list for the chosen policy.
				ArrayList<Client>terminatedJobs = policy.getCompletedJobs();
				int avgTime = 0;
				int avgOverpasses = 0;
				for(int j=0; j<terminatedJobs.size(); j++) {
					avgTime += terminatedJobs.get(j).getWaitTime();
					avgOverpasses += terminatedJobs.get(j).getOverpassed();
				}
				averageWaiting = avgTime/(double) terminatedJobs.size();
				averageOverpasses = avgOverpasses/(double) terminatedJobs.size();
	
			} //Closing if-else.
			System.out.println();
			//Displays average waiting times for data of each file.
			System.out.println("Completed time: " + completedTime);
			System.out.printf("Average waiting time for file " + (file) + ": %10.2f", averageWaiting);
			System.out.printf("\nOverpassed customers: %10.2f", averageOverpasses);

		} catch (FileNotFoundException e1) {
			System.out.println("Directory and/or file not found!");
		}	
	}
	
	private static void printDataArray(Integer[] data) {
			for(int j=0; j<data.length; j=j+2) {
				System.out.print(data[j] + ", ");
				System.out.print(data[j+1] + "\n");
			}
			System.out.println();
	}
	
	private static SLLQueue<Client> fillClientQueue(Integer[] data){
		SLLQueue<Client> clientQueue = new SLLQueue<>();
		int id = 1;
		for(int i=0; i<data.length; i=i+2) {
			Client customer = new Client(id, data[i], data[i+1]);
			clientQueue.enqueue(customer);
			id++;
		}
		System.out.println("Finished adding to Queue\n");
		return clientQueue;
	}
	
	private static int getNextTime(int time, SLLQueue<Client> cq, Policies policy) {
		int increment = 50;
		if(!policy.arePostsEmpty()) {
			for(int i=0; i<serviceStations; i++) {
				ServicePost tempPost = policy.getPost(i);
				if(!tempPost.isPostEmpty()) {
					int tempTime = tempPost.getCurrentClient().getRemainingTime();
					if(tempTime < increment) {	increment = tempTime;	}
				} else {
					if(!policy.isWaitEmpty()) {
						increment = 1;
					}
				}
			}
		}
		
		if(!cq.isEmpty()) {
			int tempTime = cq.first().getArrivalTime();
			if(tempTime - time < increment) {	increment = tempTime - time;	}
			if(cq.size() > 1) {
				tempTime = cq.second().getArrivalTime();
				if(tempTime - time < increment) {	increment = tempTime - time;	}
			}
		}
		return time + increment;
	}

}
