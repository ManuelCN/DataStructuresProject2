package p2MainClasses;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import dataGenerator.DataReader;
import policies.AbstractPolicy;
import policies.Policies;
import policies.Policy1;
import policies.Policy2;
import policies.Post;
import queue.Queue;
import queue.SLLQueue;

public class SimulationMainv2 {
	
	private static Integer[][] data;
	private static int serviceStations;

	public static void main(String[] args) {
		
		serviceStations = 3;
		if(args.length <= 1) {
			if(args.length >= 1) {	serviceStations = Integer.parseInt(args[0]);	}
			
		} else {
			System.out.println("Exceeded amount of valid arguments.");
		}
		
		try {
			DataReader reader = new DataReader();
			data = (Integer[][]) reader.readDataFiles();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		int file = 1;
//		for(int file = 0; file<data.length; file++) {
			printDataArray(data, file);
			if(data[file].length == 0) {
				System.out.println("Input file does not meet the expected format or it is empty.");
			} else {
				System.out.println("Working with File " + (file+1));
				SLLQueue<Client> clientQueue = fillClientQueue(data[file]);
				AbstractPolicy policy = new Policy2(serviceStations);
				
				int time = 1;
				int diffTime = 0;
				boolean complete = false;
				while(!complete) {
					int servTime = time - diffTime;
					System.out.println("Time: " + time);
					
					if(!policy.arePostsEmpty()) {
						policy.provideService(time, servTime);
					}
					
					if(!clientQueue.isEmpty()) {
						Client tempClient = clientQueue.first();
						if(tempClient.getArrivalTime() == time) {
							if(!policy.distribute(tempClient)) {
								policy.addToWait(tempClient);
							}
							clientQueue.dequeue();
						}
					}
					
					if(!policy.isWaitEmpty()) {	policy.distributeWait();	}
					
					if(clientQueue.isEmpty() && policy.isWaitEmpty() && policy.arePostsEmpty()) {
						complete = true;
					}
					if(time >= 70) {	complete = true;	}
					diffTime = time;
					time = getNextTime(time, clientQueue, policy);
				}
				
				System.out.println();
				
				ArrayList<Client>terminatedJobs = policy.getCompletedJobs();
				int avgTime = 0;
				for(int i=0; i<terminatedJobs.size(); i++) {
					System.out.println(terminatedJobs.get(i));
					avgTime += terminatedJobs.get(i).getAverageTime();
				}
				System.out.println("Average waiting time: " + (avgTime/terminatedJobs.size()));
			}
			
//		}		
	}
	
	private static void printDataArray(Integer[][] data, int file) {
			System.out.println("File " + (file+1) + " data:");
			for(int j=0; j<data[file].length; j=j+2) {
				System.out.print(data[file][j] + ", ");
				System.out.print(data[file][j+1] + "\n");
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
				Post tempPost = policy.getPost(i);
				if(!tempPost.isPostEmpty()) {
					int tempTime = tempPost.getCurrentClient().getRemainingTime();
					
					if(tempTime < increment) {	increment = tempTime;	}
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
