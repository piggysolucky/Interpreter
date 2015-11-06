package classProject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import classProject.interpreterModules.Atom;
import classProject.interpreterModules.SNode;
import classProject.interpreterModules.Token;
import classProject.interpreterUtils.Functions;
import classProject.interpreterUtils.MyScanner;
import classProject.interpreterUtils.Parcer;
import classProject.interpreterUtils.Printer;

public class Interpreter {
	
		public static void main(String[] args){
			// Initialize the BufferedReader, and set the reader for scanner
			String filename = args[2];
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), Charset.forName("US-ASCII")));
				MyScanner.setReader(reader);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			List<SNode> l = Parcer.ParseStart();
			for(SNode a: l){
				System.out.println(eval(a));
			}
		}
		
		public static String eval (SNode s){
			String exp = s.value;
			List<String> list = new ArrayList<String>();
			
			//If the expression is an atom
			if(Atom.isAtom(exp)){
				
				if(exp.equals("T") ||exp.equals(Token.NIL) || Functions.isInt(exp)){
					return exp;
				}else{ //unbound literal
					System.out.println("Error: Unbound literal !" + exp);
					System.exit(0);
				}
			}else if(exp.equalsIgnoreCase(Token.Dot)){ // the case of a function and update the value
				String function = s.left.value;
				
				//QUOTE
				if(function.equals("QUOTE")){
					//parameter should be a list with a single element
					if(!s.right.right.value.equals(Token.NIL) || Atom.isAtom(s.right.left.value)){
						System.out.println("Error: Illegal parameter for QUOTE !");
						System.exit(0);
					
					}
					String param = Functions.quote(s.right);
					s.value = "(" + param + ")";
					return s.value;
				}else if(function.equals("COND")){
					s.value = Functions.evcon(s.right);
					return Functions.evcon(s.right);
				}else{ // other functions
					return Functions.apply(function, s);
				}
			}
			return "";
		}
}
