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
	private static double[] averageWait;
	private static int serviceStations;

	public static void main(String[] args) {
		
		serviceStations = 3;
		if(args.length <= 1) {
			if(args.length >= 1) {	serviceStations = Integer.parseInt(args[0]);	}
			
		} else {
			System.out.println("Exceeded amount of valid arguments.");
		}
		
		try {
			reader = new DataReader();
			averageWait = new double[reader.getFileNumber()];
			for(int file = 0; file<reader.getFileNumber(); file++) {
				readData = (Integer[]) reader.readDataFiles(file);
				printDataArray(readData);
				System.out.println("Working with File " + (file+1));
				if(readData.length == 0) {
					System.out.println("Input file does not meet the expected format or it is empty.");
				} else {
					SLLQueue<Client> clientQueue = fillClientQueue(readData);
					AbstractPolicy policy = new Policy4(serviceStations);
					
					int time = clientQueue.first().getArrivalTime();
					int diffTime = time - 1;
					boolean complete = false;
					while(!complete) {
						int servTime = time - diffTime;
						System.out.println("------------ Time: " + time + " ------------");
						
						//Service-Completed Events
						if(!policy.arePostsEmpty()) {		policy.removeCompleted(time);			}
						//Transfer Events, if any
						if(!policy.isWaitEmpty()) {			policy.distributeWait(time);			}
						//Service-Provided Events
						if(!policy.arePostsEmpty()) {		policy.provideService(time, servTime);	}
						//Arrival Events
						if(!clientQueue.isEmpty()) {
							Client tempClient = clientQueue.first();
							if(tempClient.getArrivalTime() == time) {
								if(!policy.distribute(tempClient, time)) {
									policy.addToWait(tempClient);
								}
								clientQueue.dequeue();
							}
						}
						//If everything is empty, simulation is done.
						if(clientQueue.isEmpty() && policy.isWaitEmpty() && policy.arePostsEmpty()) {
							complete = true;
						}
						diffTime = time;
						time = getNextTime(time, clientQueue, policy);
					} //Closing simulation loop.
					System.out.println();
					
					//Gets the completed jobs list for the chosen policy.
					ArrayList<Client>terminatedJobs = policy.getCompletedJobs();
					int avgTime = 0;
					for(int i=0; i<terminatedJobs.size(); i++) {
						avgTime += terminatedJobs.get(i).getWaitTime();
					}
					averageWait[file] = (avgTime/(double) terminatedJobs.size());
					System.out.println("Average waiting time: " + averageWait[file]);
				} //Closing if-else.
				System.out.println();
			} //Closing file for-loop.
			//Displays average waiting times for data of each file.
			for(int i=0; i<averageWait.length; i++) {
				System.out.printf("Average waiting time for file " + (i+1) + ": %10.2f\n", averageWait[i]);
			}
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
		int increment = 15;
		if(!policy.arePostsEmpty()) {
			for(int i=0; i<serviceStations; i++) {
				ServicePost tempPost = policy.getPost(i);
				if(!tempPost.isPostEmpty()) {
					if(!tempPost.getCurrentClient().isBeingServed()) {
						increment = 1;
					} else {
						int tempTime = tempPost.getCurrentClient().getRemainingTime();
						if(tempTime > 0) {
							if(tempTime < increment) {	increment = tempTime;	}
						} else {	
							increment = tempTime + 1;
						}
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
