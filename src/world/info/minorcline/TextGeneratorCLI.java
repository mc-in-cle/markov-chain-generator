package world.info.minorcline;

/**
* UI for MarkovGenerator, using TextFileIterator, to make a text-based Markov chain.
* Does not read text files into memory, but the MarkovMaps may take up a lot of memory
* and take a while to construct if lengthy files are used. May need to increase heap size for large files.
* @author M. Cline August 2014
* 
* November 2018 - Refactored
*/

import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextGeneratorCLI{
	private Scanner user = new Scanner(System.in);
	private int modelOrder;
	private int outputLength;
	private List<String> fileNames;
	private List<TextFileIterator> fileIterators;
	private MarkovGenerator<Character> mg;
	
	public void run() {
		queryUserForInput();
		buildMarkovGenerator();
		for (int i = 0; i < outputLength; i++) {
			System.out.print(mg.generate());
		}
		
	}
	
	private void queryUserForInput() {
		getFileNamesFromUser();
		makeIterators();
		getModelOrderFromUser();
		getOutputLengthFromUser();
	}
	
	private void buildMarkovGenerator() {
		mg = new MarkovGenerator<>(modelOrder);
		for (TextFileIterator tfi : fileIterators) {
			try{
				mg.addIterator(tfi);
			} catch (IllegalStateException e) {
				System.out.println("Not enough information in " + tfi.fileName() + 
						".\nContinue and skip this file? (Y or N)? ");
				if (!user.nextLine().trim().equalsIgnoreCase("Y"))
					System.exit(0);
			}
		}
		try{
			mg.finalizeGenerator();
		} catch (IllegalStateException e) {
			System.out.println("Not enough information available in input files.");
			System.exit(1);
		}
	}
	
	//Takes the user-given list of file names and tries to make a list of TextFileIterators with them.
	//Prints message when a file name is invalid.
	//Returns the desired list. Empty if no valid file names given.
	private void makeIterators(){
		fileIterators = new ArrayList<TextFileIterator>();
		for (String f : fileNames){
			try{
				TextFileIterator itr = new TextFileIterator(f);
				fileIterators.add(itr);
			} catch (IOException e){
				System.out.print("Invalid file: " + f + ". Continue and skip this file? (Y or N)? ");
				if (!user.nextLine().trim().equalsIgnoreCase("Y"))
					System.exit(0);
			}
		}
		if (fileIterators.isEmpty()) {
			System.out.println("No valid files provided. Exiting.");
			System.exit(0);
		}
	}
	
	private void getFileNamesFromUser() {
		System.out.print("Enter text file names separated by comma and space: ");
		String fileNamesInput = user.nextLine();
		Scanner fileNamesIter = new Scanner(fileNamesInput);
		fileNamesIter.useDelimiter(", ");
		fileNames = new ArrayList<String>();
		while(fileNamesIter.hasNext())
			fileNames.add(fileNamesIter.next());
		fileNamesIter.close();
	}
	
	//Ask user for input about order of model.
	//Returns appropriate user input.
	private void getModelOrderFromUser(){
		modelOrder = 0;
		boolean loop = false;
		do {
			System.out.print("Enter order of model: ");
			try{
				modelOrder = user.nextInt();
			} catch (Exception e){
				loop = true;
			}
			if (modelOrder < 1 || modelOrder > 20){
				loop = true;
				System.out.println("Enter an integer between 2 and 20.");
			}
		} while (loop);
	}

	//Ask user for input about length of output.
	//Returns appropriate user input.
	private void getOutputLengthFromUser(){
		outputLength = 0;
		boolean loop = false;
		do{
			System.out.print("Enter output length in characters: ");
			try{
				outputLength = user.nextInt();
			} catch (Exception e){
				loop = true;
			}
			if (outputLength < 1){
				loop = true;
				System.out.println("Enter a positive integer.");
			}
		} while (loop);
	}
	
	public static void main(String[] args){
		TextGeneratorCLI app = new TextGeneratorCLI();
		app.run();
	}
		
}