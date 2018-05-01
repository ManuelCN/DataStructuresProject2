package dataFiles;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * 
 * @author pedroirivera-vega
 *
 */
public class FileReader {

	private ArrayList<String> fileNames;
	private String parentDirectory; 
	

	public FileReader() throws FileNotFoundException {
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
	
	public String getFileName(int index) {
		return fileNames.get(index);
	}
	
	public Integer[] readDataFiles(int file) throws FileNotFoundException, NoSuchElementException {
		String fileName = fileNames.get(file);
		try {
		Scanner inputFile = new Scanner(new File(parentDirectory, fileName));
		ArrayList<Integer> fileContent = new ArrayList<>();
		while(inputFile.hasNextLine()) {
			fileContent.add(Integer.parseInt(inputFile.findInLine("[0-9]+")));
			fileContent.add(Integer.parseInt(inputFile.findInLine("[0-9]+")));
			inputFile.nextLine();
		}
		inputFile.close();
		return fileContent.toArray(new Integer[fileContent.size()]);
		} catch (FileNotFoundException e) {
			Integer[] result = {-1};
			return result;
		} catch (NoSuchElementException a) {
			Integer[] result = {-2};
			return result;
		} catch (NumberFormatException b) {
			Integer[] result = {-2};
			return result;
		}
	}
}
