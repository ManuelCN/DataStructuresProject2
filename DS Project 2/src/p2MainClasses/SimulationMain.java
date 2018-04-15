package p2MainClasses;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import dataGenerator.DataReader;
import policies.Policy1;
import queue.Queue;
import queue.SLLQueue;

public class SimulationMain {
	
	private static Integer[][] data;

	public static void main(String[] args) {
		
		int serviceStations = 3;
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
		printDataArray(data);
		
		int file = 0;
//		for(int file = 0; file<data.length; file++) {
			System.out.println("Working with File " + (file+1));
			Queue<Client> clientQueue = fillClientQueue(data[file]);
			Policy1 policy = new Policy1(clientQueue, serviceStations);
			
			ArrayList<Client>terminatedJobs = policy.runSimulation();
			
			for(int i=0; i<terminatedJobs.size(); i++) {
				System.out.println(terminatedJobs.get(i));
			}
			
//		}		
	}
	
	private static void printDataArray(Integer[][] data) {
		for(int i=0; i<1; i++) {
			System.out.println("File " + (i+1) + " data:");
			for(int j=0; j<data[i].length; j=j+2) {
				System.out.print(data[i][j] + ", ");
				System.out.print(data[i][j+1] + "\n");
			}
			System.out.println();
		}
	}
	
	private static Queue<Client> fillClientQueue(Integer[] data){
		Queue<Client> clientQueue = new SLLQueue<>();
		int id = 1;
		for(int i=0; i<data.length; i=i+2) {
			Client customer = new Client(id, data[i], data[i+1]);
			clientQueue.enqueue(customer);
			id++;
		}
		System.out.println("Finished adding to Queue\n");
		return clientQueue;
	}

}
