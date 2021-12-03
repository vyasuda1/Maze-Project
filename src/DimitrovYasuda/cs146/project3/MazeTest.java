package DimitrovYasuda.cs146.project3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MazeTest {
	Random rand;
	Maze maze1;
	Maze maze2;
	Maze maze3;
	Maze maze4;
	Maze maze5;
	Maze maze6;
	Maze maze7;
	Maze maze8;
	Maze maze9;
	Maze maze10;
	
	@BeforeEach
	void initiaizeMazes() {
		rand = new Random(666);
		maze1 = new Maze(1, rand);
		maze2 = new Maze(2, rand);
		maze3 = new Maze(3, rand);
		maze4 = new Maze(4, rand);
		maze5 = new Maze(5, rand);
		maze6 = new Maze(6, rand);
		maze7 = new Maze(7, rand);
		maze8 = new Maze(8, rand);
		maze9 = new Maze(9, rand);
		maze10 = new Maze(10, rand);
	}
	
	@Test
	void testMazeOutput() {
		maze1.saveToFile("maze1-666.txt");
		maze2.saveToFile("maze2-666.txt");
		maze3.saveToFile("maze3-666.txt");
		maze4.saveToFile("maze4-666.txt");
		maze5.saveToFile("maze5-666.txt");
		maze6.saveToFile("maze6-666.txt");
		maze7.saveToFile("maze7-666.txt");
		maze8.saveToFile("maze8-666.txt");
		maze9.saveToFile("maze9-666.txt");
		maze10.saveToFile("maze10-666.txt");
		
		for (int i = 1; i <= 10; i++) {
			String outputFileName = "maze" + i + "-666.txt";
			String expectedFileName = "golden-" + outputFileName;
			String outputFileContents = "Output File Does Not Exist";
			String expectedFileContents = "Expected File Does Not Exist";
			try {
	            File file = new File(outputFileName);
	            if (file.exists()) {
	                Scanner inputFile = new Scanner(file);
	                outputFileContents = "";
	                while (inputFile.hasNextLine()) {
	                    outputFileContents += inputFile.nextLine();
	                }
	                inputFile.close();
	            }
	            file = new File(expectedFileName);
	            if (file.exists()) {
	                Scanner inputFile = new Scanner(file);
	                expectedFileContents = "";
	                while (inputFile.hasNextLine()) {
	                    expectedFileContents += inputFile.nextLine();
	                }
	                inputFile.close();
	            }
	        }
	        catch (FileNotFoundException exception) {
	            exception.printStackTrace();
	        }
			assertEquals(outputFileContents, expectedFileContents);
		}
	}
	

}
