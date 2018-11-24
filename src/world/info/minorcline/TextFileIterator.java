package world.info.minorcline;

/**
* Iterator that wraps a FileReader to read text from a file
* one char at a time. Uses a buffer that holds one character
* to detect the end of the file and IO exceptions.
* IO exceptions are surpressed. Instead, the iterator stops producing output.
* @author M. Cline August 2014
*/

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class TextFileIterator implements Iterator<Character>{
	private String fileName;
	private FileReader reader;
	private int buff;

	
	public TextFileIterator(String fileName) throws IOException{
		reader = new FileReader(fileName);
		buff = reader.read();
		try{
			buff = reader.read();
		} catch (IOException i){
			buff = -1;
		}
	}

	public Character next(){
		Character r = null;
		if (buff != -1) {
			r = (char)(buff);
			try{
				buff = reader.read();
			} catch (IOException i){
				buff = -1;
			}
		}
		return r;
	}
	
	public boolean hasNext(){
		if (buff == -1) return false;
		else return true;
	}
	
	
	public String fileName(){ 
		return fileName;
	}
	
}