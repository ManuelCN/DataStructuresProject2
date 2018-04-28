package dataGenerator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author pedroirivera-vega
 *
 */
public class DataReader {

	private ArrayList<String> fileNames;
	private String parentDirectory; 
	

	public DataReader() throws FileNotFoundException {
		fileNames = new ArrayList<>();
		parentDirectory = "inputFiles";
		File input = new File(parentDirectory, "dataFiles.txt");
		if(!input.exists()) {
			throw new FileNotFoundException("Directory or file does not exist!");
		}
		Scanner dataFiles = new Scanner(input);
		while(dataFiles.hasNextLine()) {
			fileNames.add(dataFiles.nextLine());
		}
		dataFiles.close();
	}
	
	public int getFileNumber() {
		return fileNames.size();
	}
	
	public Object[] readDataFiles(int file) throws FileNotFoundException {
		String fileName = fileNames.get(file);
		Scanner inputFile = new Scanner(new File(parentDirectory, fileName));
		ArrayList<Integer> fileContent = new ArrayList<>();
		while(inputFile.hasNext()) {
			fileContent.add(inputFile.nextInt());
			fileContent.add(inputFile.nextInt());
		}
		inputFile.close();
		return fileContent.toArray(new Integer[fileContent.size()]);

	}
	
	private void printArray(Integer[] numbers) {
		for (int i=0; i<numbers.length; i++) 
			System.out.print(numbers[i] + "  "); 
		System.out.println(); 
	}
}
