# Project Title

p2_40354020_172

Data Structures Programming Project 2

## Project Description

This program simulates a typical everyday scenario. A client enters a queue to receive service at a post. There are many ways to achieve this. Four policies are discussed, implemented and tested
in this program for this scenario.

## Running the code

First you will need to download the zip file. 
1. If running from the command prompt, extract the files into a folder.
2. If running from Eclipse, import the project via the "_General/Existing Eclipse Projects_" menu and selecting the archive file (zip file).

### Compiling the code
1. Command line: change directory (cd) to the project's folder. Then run the command:
							 `javac -d bin src/*/*.java`
2. Eclipse: Eclipse would have already compiled your code once you run the code.

### Running the code from the command prompt.

To run the simulation:
1. Make sure you have compiled all classes in the dataFiles, p2MainClasses, policies, posts and queue packages.
2. Once compiled, if no input files available, generate some by running FilesGeneratorMain in the dataFiles package. This will create an "inputFiles" directory if none exist where it will store the input files generated.
	`java -classpath bin datFiles.FilesGeneratorMain`	
3. If input files available, or after generating some, run the SimulationMain in the p2MainClasses package. This will create an "outputFiles" directory if none exists where it will store the output files generated.
	`java -classpath bin p2MainClasses.SimulationMain`
4. For a given input file, verify its corresponding output file to verify results. If input file is "data__0.txt", its output file will be "data__0__OUT.txt".

### Running the code from Eclipse.

To run the simulation:
1. Having imported the project, enter the dataFiles package and run the FilesGeneratorMain if not input files available.
2. If input files availble, or after having generated some, enter the p2MainClasses package and run the SimulationMain.
3. This will generate output files, containing: policy that was used, amount of servers, simulation completed time unit, average waiting time and average overpasses.
4. If the input file does not exists, does not meet the expected format or is empty, its corresponding output file will contain an appropriate message.

### Prerequisites

To run this program you will need to have Java 8 installed and the latest version of Eclipse (Oxygen).


## Authors

* **Manuel Castaneda**

## Acknowledgments

* **Pedro Rivera**