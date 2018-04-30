package dataGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import dataGenerator.DataGenerator;

public class FilesGeneratorMain {

	public static void main(String[] args) throws FileNotFoundException {
		
		if (args.length <= 4) {
			int n = 7; 						//Number of files
			int l = 200;						//Number of clients
			int t = 4;						//Arrival Times
			int s = 10;						//Service times
			
			if (args.length >= 1)	{	n = Integer.parseInt(args[0]);	}
			if (args.length >= 2)	{	l = Integer.parseInt(args[1]);	}
			if (args.length >= 3)	{	t = Integer.parseInt(args[2]);	}
			if (args.length == 4)	{	s = Integer.parseInt(args[3]);	}
			generateFiles(n, l, t, s); 
		}	else	{
			System.out.println("Invalid number of parameters. Must be <= 2.");
		}

 
	}

	private static void generateFiles(int fileNum, int lineNum, int arrivalTimes, int serviceTimes) throws FileNotFoundException {
		String parentDirectory = "inputFiles";   // must exist in current directory
		Random rand = new Random();
		
		
		File dir = new File(parentDirectory);
		if(!dir.exists())
			dir.mkdir();
		

		PrintWriter dataFile = new PrintWriter(new File(parentDirectory, "dataFiles.txt"));
		for (int i=0; i<fileNum; i++) {
			String fileName = "data_" + i + ".txt"; 
			dataFile.println(fileName);
			PrintWriter out = new PrintWriter(new File(parentDirectory, fileName)); 
			int arriv = 1;
			for (int k=0; k<rand.nextInt(lineNum); k++) {
				out.print(arriv);
				out.print("\t");
				out.print(rand.nextInt(serviceTimes) + 3);
				out.println();
				arriv = arriv + rand.nextInt(arrivalTimes) + 1;
//				arriv++;
			}				
			out.close();
		}
		dataFile.close();


	}
}
