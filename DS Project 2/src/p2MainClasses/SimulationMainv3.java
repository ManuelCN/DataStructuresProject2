package p2MainClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import dataGenerator.DataReader;
import policies.AbstractPolicy;
import policies.Policies;
import policies.Policy1;
import policies.Policy2;
import policies.Policy3;
import policies.Policy4;
import posts.ServicePost;
import queue.SLLQueue;

public class SimulationMainv3 {
	public static void main(String[] args) {
		File parentDir = new File("outputFiles");
		if(!parentDir.exists()) {
			parentDir.mkdir();
		}
		try {
			DataReader reader = new DataReader();
			for(int file = 0; file < reader.getFileNumber(); file++) {
				System.out.println("--- Working with File " + file + " ---\n");
				PrintWriter outputFile = new PrintWriter(new File(parentDir.toString(), "data_" + file + "_OUT.txt"));
				Integer[] data = reader.readDataFiles(file);
				if(data.length != 0 && data[0] == -1) {
					outputFile.print("Input file not found.");
				} else if(data.length == 0 || data[0] == -2) {
					outputFile.print("Input file does not meet the expected format or it is empty.");
				} else {
					outputFile.println("Number of customers is: " + data.length / 2);
					AbstractPolicy policy;
					for(int pol = 0; pol < 4; pol++) {
						for(int posts = 1; posts <= 5; posts+=2) {
							
							if(pol == 0) 		{	policy = new Policy1(posts);	}
							else if(pol == 1) 	{	policy = new Policy2(posts);	}
							else if(pol == 2)	{	policy = new Policy3(posts);	}
							else				{	policy = new Policy4(posts);	}
							
							System.out.println(policy.getPolicyName() + " with " + posts + " post(s).");
							SLLQueue<Client> policyClientQueue = fillClientQueue(data);
							int time = policyClientQueue.first().getArrivalTime();
							int diffTime = time - 1;
							boolean complete = false;
							int completedTime = 0;
							
							while(!complete) {
								int servTime = time - diffTime;
								
								//Transfer Events, if any
								if(!policy.isWaitEmpty()) {			policy.distributeWait(time);			}
								//Service-Provided Events
								if(!policy.arePostsEmpty()) {		policy.provideService(time, servTime);	}
								
								if(!policy.isWaitEmpty() || !policy.arePostsEmpty()) {
									policy.checkOverpass();
								}
								
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
//								if(!policy.isWaitEmpty() || !policy.arePostsEmpty()) {
//									policy.checkOverpass();
//								}
								
								//If everything is empty, simulation is done.
								if(policyClientQueue.isEmpty() && policy.isWaitEmpty() && policy.arePostsEmpty()) {
									complete = true;
									completedTime = time;
								}
								diffTime = time;
								time = getNextTime(time, policyClientQueue, policy, posts);
							} //Closing simulation loop.
							
							ArrayList<Client> terminatedJobs = policy.getCompletedJobs();
							
							double avgTime = 0;
							double avgOverpasses = 0;
							for(int i=0; i<terminatedJobs.size(); i++) {
								avgTime += terminatedJobs.get(i).getWaitTime();
								avgOverpasses += terminatedJobs.get(i).getOverpassed();
							}
							avgTime = avgTime/(double) terminatedJobs.size();
							avgOverpasses = avgOverpasses/(double) terminatedJobs.size();
							
							DecimalFormat df = new DecimalFormat("#.##");
							df.setRoundingMode(RoundingMode.FLOOR);
							double avgT = new Double(df.format(avgTime));
							double avgO = new Double(df.format(avgOverpasses));
							
							if(pol <= 1) {
								outputFile.println(policy.getPolicyName() + " " + posts + ":\t\t" + completedTime +
										"\t" + avgT + "\t" + avgO);
							} else {
								outputFile.println(policy.getPolicyName() + " " + posts + ":\t" + completedTime +
										"\t" + avgT + "\t" + avgO);
							}
						}
					}
				}
			outputFile.println();
			outputFile.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input directory 'inputFiles' not found.");
		}
		
	}
	
	private static SLLQueue<Client> fillClientQueue(Integer[] data) {
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
	
	private static int getNextTime(int time, SLLQueue<Client> cq, Policies policy, int posts) {
		int increment = 50;
		if(!policy.arePostsEmpty()) {
			for(int i=0; i<posts; i++) {
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
		} else {
			if(!policy.isWaitEmpty()) {
				increment = 1;
			}
		}
		
		if(!cq.isEmpty()) {
			int tempTime = cq.first().getArrivalTime();
			if(tempTime - time < increment && tempTime - time >= 0) {	increment = tempTime - time;	}
			if(cq.size() > 1) {
				tempTime = cq.second().getArrivalTime();
				if(tempTime - time < increment) {	increment = tempTime - time;	}
			}
		}
		return time + increment;
	}
}
