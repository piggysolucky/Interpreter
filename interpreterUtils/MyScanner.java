package classProject.interpreterUtils;

import java.io.BufferedReader;
import java.io.IOException;

import classProject.interpreterModules.Atom;
import classProject.interpreterModules.Token;

/**
 * This class file servers as the scanner which reads from a file
 * It also has some logic to check the input token of the file
 * */
public class MyScanner {
	public static BufferedReader reader;
	private static Character prev = null;
	
	public static void setReader(BufferedReader newreader){
		reader = newreader;
	}
	//Read the chars from the input file
	public static String getNextToken(){
		 if(prev != null && !String.valueOf(prev).matches("\\s")){
			 Character r = prev;
			 prev = null;
			 return String.valueOf(r);
		 }
		 
	     int c;
	     try {
			while((c = reader.read()) != -1){
				 char character = (char)c;
				 if(character == '('){
					 return Token.OpenParenthesis;
				 }else if(character == ')'){
					 return Token.ClosingParenthesis;
				 }else if(character == '.'){
					 return Token.Dot;
				 }else if ((String.valueOf(character)).matches("\\s")){   //white space
					 continue;
				 }else{ // The case for atom (literal or numeric atom)
					 StringBuffer stringBuffer = new StringBuffer();
					 while(String.valueOf(character).matches("[0-9A-Z]")){
						 stringBuffer.append(character);
						 character = (char) reader.read();
					 }
					 prev = character;
					 String token = stringBuffer.toString();
					 
					 if(Atom.isAtom(token)){
						 return token;
					 }else {
						 return Token.ERROR;
					 }
				 }
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	     
	     return Token.End;
	}
}