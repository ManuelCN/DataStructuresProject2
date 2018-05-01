package p2MainClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import dataFiles.FileReader;
import policies.AbstractPolicy;
import policies.Policies;
import policies.Policy1;
import policies.Policy2;
import policies.Policy3;
import policies.Policy4;
import posts.ServicePost;
import queue.SLLQueue;
/**
 * This class runs the simulation of clients receiving service at varying amount of posts
 * according to different policies.
 * First reads the data from given files. Verifies if the data is valid according to
 * specific restrictions.
 * For each file and varying amounts of posts and different policies, simulates the whole 
 * scenario. The scenario simulation will return the average waiting time and the average 
 * amount of overpasses for the given amount of clients.
 * Will generate an output file with this information for each file.
 * @author	Manuel E. Castañeda
 * 			802-15-1272
 * 			Section 090
 *
 */
public class SimulationMain {
	public static void main(String[] args) {
		File parentDir = new File("outputFiles");
		if(!parentDir.exists()) {
			parentDir.mkdir();
		}
		try {
			FileReader reader = new FileReader();
			for(int file = 0; file < reader.getFileNumber(); file++) {
				System.out.println("--- Working with File " + file + " ---\n");
				//Prepares to write the output file for the given input file.
				PrintWriter outputFile = new PrintWriter(new File(parentDir.toString(), "data_" + file + "_OUT.txt"));
				//Reads the data from the input files.
				Integer[] data = reader.readDataFiles(file);
				if(data.length != 0 && data[0] == -1) {
					outputFile.print("Input file not found.");
				} else if(data.length == 0 || data[0] == -2) {
					outputFile.print("Input file does not meet the expected format or it is empty.");
				} else {
					outputFile.println("Number of customers is: " + data.length / 2);
					AbstractPolicy policy;
					
					for(int pol = 0; pol < 4; pol++) {	//Iterates over the different policies.
						for(int posts = 1; posts <= 5; posts+=2) {	//Iterates over the varying amount of posts.
							
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
								//Checks overpasses
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
								//If everything is empty, simulation is done.
								if(policyClientQueue.isEmpty() && policy.isWaitEmpty() && policy.arePostsEmpty()) {
									complete = true;
									completedTime = time;
								}
								diffTime = time;
								time = getNextTime(time, policyClientQueue, policy, posts);
							} //Closing simulation loop.
							
							ArrayList<Client> terminatedJobs = policy.getCompletedJobs();
							//Calculates the average waiting time and overpasses.
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
							//Writes into the output file the calculated average values.
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
			//Closes the output file.
			outputFile.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input directory 'inputFiles' not found.");
		}
		
	}
	/**
	 * Fills the input client queue with the file data, located in an array.
	 * @param data	The array containing the file data.
	 * @return	Returns the queue containing the input file data.
	 */
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
	/**
	 * Calculates the increment in time.
	 * @param time	The previous time unit.
	 * @param cq	The input client queue.
	 * @param policy	The current policy.
	 * @param posts	The current amount of posts.
	 * @return	Returns the current time unit plus the calculated increment.
	 */
	private static int getNextTime(int time, SLLQueue<Client> cq, Policies policy, int posts) {
		int increment = 50;		//Dummy max value.
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
