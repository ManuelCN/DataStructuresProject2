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

	private Integer[][] data;
	String[] fileNames = new String[7];
	private String parentDirectory; 
	

	public DataReader() throws FileNotFoundException {
		parentDirectory = "inputFiles";
		File input = new File(parentDirectory, "dataFiles.txt");
		if(!input.exists()) {
			throw new FileNotFoundException("Directory or file does not exist!");
		}
		Scanner dataFiles = new Scanner(input);
		int index = 0;
		while(dataFiles.hasNextLine()) {
			fileNames[index] = dataFiles.nextLine();
			index++;
		}
		dataFiles.close();
	}
	
	/**
	 * 
	 * @return
	 * @throws FileNotFoundException 
	 */
	public Object[][] readDataFiles() throws FileNotFoundException {
		
		data = new Integer[fileNames.length][];
		for(int i=0; i<fileNames.length; i++) {
			String fileName = fileNames[i];
			Scanner inputFile = new Scanner(new File(parentDirectory, fileName));
			ArrayList<Integer> fileContent = new ArrayList<>();
			while(inputFile.hasNext()) {
				
				fileContent.add(inputFile.nextInt());
				fileContent.add(inputFile.nextInt());
			}
			inputFile.close();
			data[i] = (Integer[]) fileContent.toArray(new Integer[0]);
		}
		return data;
	}
	
	private void printArray(Integer[] numbers) {
		for (int i=0; i<numbers.length; i++) 
			System.out.print(numbers[i] + "  "); 
		System.out.println(); 
	}
}
