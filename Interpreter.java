package classProject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import classProject.interpreterModules.Atom;
import classProject.interpreterModules.SNode;
import classProject.interpreterModules.Token;
import classProject.interpreterUtils.Functions;
import classProject.interpreterUtils.MyScanner;
import classProject.interpreterUtils.Parcer;

public class Interpreter {
	    public static HashMap<String, SNode> bodyMap = new HashMap<>();
	    public static HashMap<String, List<String>> param = new HashMap<>();
	    
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
			
			//Printer.print(l);
			int i = 1;
		for(SNode a: l){
			System.out.println(i++ +"   " + eval(a, new HashMap<String, String>()));
			}
		}
		
		public static String eval (SNode s, HashMap<String, String> params){
			String exp = s.value;
			List<String> list = new ArrayList<String>();
			
			//If the expression is an atom
			if(Atom.isAtom(exp)){
				
				if(exp.equals("T") ||exp.equals(Token.NIL) || Functions.isInt(exp)){
					return exp;
				}else if(params.containsKey(exp)){
					s.value = params.get(exp);
					return params.get(exp);
				}else{ //unbound literal
					System.out.println("Error: Unbound literal !" + exp);
					System.exit(0);
				}
			}else if(exp.equalsIgnoreCase(Token.Dot)){ // the case of a function and update the value
				String function = s.left.value;
				if(function.equals("QUOTE")){
					//parameter should be a list with a single element
					if(!s.right.value.equals(Token.Dot) || !s.right.right.value.equals(Token.NIL)){
						System.out.println("Error: Illegal parameter for QUOTE !");
						System.exit(0);
					}
					s.value = Functions.quote(s.right.left);
					s.list = new LinkedList<>(s.right.left.list); // store the vlaue in quote
					return s.value;
				}
				if(function.equals("COND")){
					s.value = Functions.evcon(s.right, params);
					s.list = new LinkedList<>(s.right.list);
					return s.value;
				}
				if(function.equals("DEFUN")){
					// validate, save into list
					Functions.validateParam(s.right);
					return s.right.left.value;
				}
				else{
					// if it's not a list, is should be a value
					s.value = Functions.apply(function, s.right, params);
					s.list = new LinkedList<>(s.right.list);
					return s.value;
				}
			}
			return "";
		}
		
}
