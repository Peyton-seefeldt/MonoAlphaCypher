package AlphaCypher;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/*@Author 
 * Peyton Seefeldt 2019
 * 
 * @Description
 * This program will take in any size txt file and run through line by line gathering the total of each character in it.
 * Then caluclates frequency by dividing the character total / total. From there it will make it's first estimation of the keys.
 * Then shows what about 30 lines of your text file looks like with the estimation applied. After that allows you to swap letters 
 * around 1 by 1 using your best guesses with the understanding of the human language. After the passage looks good and readable you type
 * "stop" and the program will show you your finished key and write the whole txt document to a new place of your choosing with the
 * correct key applied. 
 */


public class MonoAlphaCypherAnalyzer {
	
	static HashMap<Character, Integer> Lettermap = new HashMap<Character, Integer>(); //Hashmap for all the letters and there count
	static HashMap<Character, Double> Frequencymap = new HashMap<Character, Double>(); //Hashmap for the percentages of frequenc for each letter
	static HashMap<Character, Character> Decryption = new HashMap<Character, Character>(); //Hashmap for key
	
	static double total = 0;
	
	static String Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String FrequencyOrder = "ETAOINSRHDLUCMFYWGPBVKXQJZ";
	
	public static void main(String[] args) throws IOException {
		
		String filepath = "C:\\Users\\Peyote\\Desktop\\Java\\AlphaCypher\\src\\AlphaCypher\\Encrypted1.txt"; //Text file to analyze
		String Writepath = "C:\\Users\\Peyote\\Desktop\\Java\\AlphaCypher\\src\\AlphaCypher\\decrypted.txt";	//File path to right new decrypted file to
		System.out.println("Loading Document from: "+ filepath);
		
		Fileread(filepath);

		System.out.println("Done!");
		
		getTotal();
		getFrequency();

		System.out.println();
		System.out.println("Total of each character in the document: ");
		System.out.println(Lettermap);
		System.out.println("total letters: "+total);
		System.out.println();
		System.out.println("Percentage of each characture in document: ");
		System.out.println(Frequencymap);
		
		System.out.println();
		System.out.println("Calculating decryption...");
		setLetters();
		System.out.println("Done!");

		System.out.println();
		System.out.println("Estimated letters to each in decryption according to frequency gathered: ");
		System.out.println(Decryption);

		System.out.println();
		System.out.println("Done!");
		Scanner sc = new Scanner(System.in);
		System.out.println("Press the enter key to begin editing the estimated letters...");
		String input = sc.nextLine();
		
		String changeFrom = "", changeTo = "";
		while(changeFrom != "STOP" || changeTo != "STOP") {
			System.out.println("Heres a passage from the document with changes made:  ");
			presentPassage(filepath);
		
			System.out.println();
			System.out.println();
			System.out.println("Pick a letter to change(type 'stop' to end and print): ");
			changeFrom = sc.nextLine().toUpperCase();
			if(changeFrom.contains("STOP")) {
				break;
			}
			System.out.println();
			System.out.println("Pick a letter to change it to(type 'stop' to end and print): ");
			changeTo = sc.nextLine().toUpperCase();			
			if(changeTo.contains("STOP")) {
				break;
			}
			System.out.println();
			System.out.println("Swapping all "+changeFrom+"'s to "+changeTo);
			
			swapLetters(changeFrom, changeTo);
		}
		
		
		System.out.println();
		System.out.println("Stopping editing process...");
		
		System.out.println();
		System.out.println("Final key shown below:");		
		System.out.println(Decryption);
		
		System.out.println();
		System.out.println("Rewriting new file at: "+Writepath);
		FileWrite(filepath, Writepath);
		
		sc.close();
	}
	
	//Method to swap letters in the key
	public static void swapLetters(String changeFrom, String changeTo) {
		char f = changeFrom.charAt(0);
		char t = changeTo.charAt(0);
		int index = getIndexValue(f);
		swapInverseLetters(changeTo, changeFrom);
		Decryption.replace(Alphabet.charAt(index), t);
	}
	
	//Swaps the inverse letters to avoid duplicates
	public static void swapInverseLetters(String changeFrom, String changeTo) {
		char f = changeFrom.charAt(0);
		char t = changeTo.charAt(0);
		int index = getIndexValue(f);
		
		Decryption.replace(Alphabet.charAt(index), t);
	}
	
	//Gets the index inside the hash map of the given letter
	public static int getIndexValue(char c) {
		for(int i = 0;i < Decryption.size();i++) {
			char test = Alphabet.charAt(i);
			if(Decryption.get(test) == c) {
				return i;
			}			
		}
		return 0;
	}
	
	//Shows 30 lines of the text file at whatever stage of decryption it is at in the terminal
	public static void presentPassage(String filepath) throws IOException {
		
		File file = new File(filepath); 
	      
    	BufferedReader csvReader = new BufferedReader(new FileReader(file));
		
    	String row;
    	int count = 0;
    	try {
    		while ((row = csvReader.readLine()) != null) {
    			 String[] data = row.split("");
    			 System.out.println();
    			 for (String i : data) {	
    			    if(i.isEmpty()) {
    			    	break;
    			    }
    			    else {
    			    	CharSequence c = i;
			    		if(Alphabet.contains(c)) {
			    			System.out.print(Decryption.get(c.charAt(0)));
			    		}
			    		else {
			    			System.out.print(i);
			    		}
    			    }			    		
    			 }
    			 count++;
    			 if(count == 30) {
    				 break;
    			 }
    		}   			
    			
    		} catch (IOException e) {
    			System.out.println(e);
    		}
    		
    		try {
    			csvReader.close();
    		} catch (IOException e) {
    			System.out.println(e);
    		}
	}
	
	//Sets up the key hash map given the the frequency map is done
	public static void setLetters() {
		char tmp = 0;
		for(int i = 0 ; i<FrequencyOrder.length();i++){
			
			double max = 0;
			for(int j = 0; j< Frequencymap.size();j++) {
				
				char c = Alphabet.charAt(j);
				if(max<Frequencymap.get(c)) {
					max = Frequencymap.get(c);
					tmp = c;
				}
			}
			char dL = FrequencyOrder.charAt(i);
			
			Decryption.put(tmp, dL);
			
			Frequencymap.replace(tmp, (double) 0);
		}
	}
	
	//Sets up the frequency hash map
	@SuppressWarnings("deprecation")
	public static void getFrequency() {
		for(int i = 0 ; i < Alphabet.length(); i++) {
			char c = Alphabet.charAt(i);
			Double CharAmount = new Double(Lettermap.get(c));
			
			double frequency = CharAmount / total * 100;
			double frequencyRounded = Math.round(frequency * 100D) / 100D;
			
			Frequencymap.put(c, frequencyRounded);
		}
	}
	
	//Calculates total Alphabetical letters without using any symbols
	public static void getTotal() {		
		for(int i = 0 ; i < Alphabet.length(); i++) {
			total += Lettermap.get(Alphabet.charAt(i));
		}
	}
	
	//Adds a letter/symbol to the count map or adds one if its already there
	public static void addtoMap(String i) {
		for (int j = 0; j < i.length(); j++) {
			
			char c = i.charAt(j);
			Integer val = Lettermap.get(c);
			
			if (val != null) {
				Lettermap.put(c, val + 1);
			}	
			else {
				Lettermap.put(c, 1);
			}
		}
	}
	
	//Writes the new file given the key
	public static void FileWrite(String filepath, String writepath) throws IOException { //This just needs a finished hash map with your key to access and to be given the filepath of the decrypted document and new path to write the new one to
		File file = new File(filepath); 
		File file2 = new File(writepath); 
	      
		BufferedReader csvReader = new BufferedReader(new FileReader(file));
		BufferedWriter writer = new BufferedWriter(new FileWriter(file2));
	
		String row;
		try {
			while ((row = csvReader.readLine()) != null) {
			    String[] data = row.split(""); //breaks the line the document into an array of characters and spaces
			    writer.newLine();
			    for (String i : data) {	//for each of those characters or spaces assign it to i and run through the loop
			    	if(i.isEmpty()) { //if nothing is in the line just break the loop and write a new line
			    		writer.newLine();
			    		break;
			    	}
			    	else {
			    		CharSequence c = i; //assigns i to the char
			    		if(Alphabet.contains(c)) { //Check if it is a letter
			    			writer.write(Decryption.get(c.charAt(0))); //Writes the hashmap value of that character. This should return the right value
			    		}
			    		else {
			    			writer.write(i); //Otherwise if its not a letter just write the space or ./! etc
			    		}
			    	}		    		
			    }
			}   			
			
		} catch (IOException e) {
			System.out.println(e);
		}
		
		try {
			csvReader.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		writer.close();
	}
	
	//Reads file and creates the first count map
	public static void Fileread(String filepath) throws IOException {
	 
   		File file = new File(filepath); 
      
    	BufferedReader csvReader = new BufferedReader(new FileReader(file));
		
    	String row;
    	try {
    		while ((row = csvReader.readLine()) != null) {
    			 String[] data = row.split("");
    			 for (String i : data) {	
    			    if(i.isEmpty()) {
    			    	break;
    			    }
    			    else {
    			    	addtoMap(i);
    			    }			    		
    			 }
    		}   			
    			
    		} catch (IOException e) {
    			System.out.println(e);
    		}
    		
    		try {
    			csvReader.close();
    		} catch (IOException e) {
    			System.out.println(e);
    		}
    	  }
}