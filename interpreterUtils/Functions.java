package classProject.interpreterUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import classProject.Interpreter;
import classProject.interpreterModules.Atom;
import classProject.interpreterModules.SNode;
import classProject.interpreterModules.Token;

public class Functions {
	
	public static boolean isInt(String s){
		return s.matches("[+-]?[1-9][0-9]*") || s.matches("0");
	}
	
	public static String evcon(SNode s, HashMap<String,String> params){
		//validate the parameters
		if(Functions.isNull(s) || Functions.isNull(s.left) ||Functions.isNull(s.left.right) || !Functions.isNull(s.left.right.right)){
			System.out.println("Error: Illegal parameter list for COND !");
				System.exit(0);
			}
		if(Interpreter.eval(s.left.left, params).equals("T")){
			s.value = Interpreter.eval(s.left.right.left, params);
			s.list = s.left.right.left.list;
		}else{
			s.value = evcon(s.right, params);
		}
		return s.value;
	}
	//QUOTE
	public static String quote(SNode s){
		if(!s.value.equals(Token.Dot)){
			return s.value;
		}
		String left = quote(s.left);
		String right = quote(s.right);
		
		// end of the list, treat as a list
		if(right.equals(Token.NIL)){
			s.list.add(left);
			s.value = "(" + left + ")";
			return "(" + left + ")";
		}
		// both is atom
		if(Atom.isAtom(right) && Atom.isAtom(left)){
			s.list.add(left);
			s.list.add(right);
			return "(" + left + "." + right + ")";
		}
		// one of them is not atom combine the list
		else{
			s.list = new LinkedList<>(s.right.list);
			s.list.addFirst(left);
			String result = "(";
			for(String str: s.list){
				result += str;
			}
			result +=")";
			return result;
		}
	}	
	
	//evlist: recursively evaluate the parameter list of the function
		public static void evlist(SNode s,  HashMap<String,String> params){
			if(s.value.equals(Token.NIL)){
				return;
			}
			
			Interpreter.eval(s.left, params);
			evlist(s.right, params);
		}
		
	//NULL
		public static boolean isNull(SNode s){
			return s.value.equals(Token.NIL);
		}
	
	//ValidateParams list
		public static void validateParam(SNode s){
			// name should be literal atom 
			String name = s.left.value;
			if(!name.matches("[A-Z][0-9A-Z]*") || Token.buildIn.contains(name)){
				System.out.println("Function name should be a literal atom different from built in functions !");
				System.exit(0);
			}
			// validate parameter list
			SNode p = s.right.left;
			if(!(p.value.equals(Token.Dot) || p.value.equals(Token.NIL))){
				System.out.println("Function parameters should be a list !");
				System.exit(0);
			}
			if(p.value.equals(Token.Dot)){
				HashSet<String> memory = new HashSet<>();
				memory.add("T");
				memory.add("NIL");
				while(!p.value.equals(Token.NIL)){
					String param1 = p.left.value;
					if(!param1.matches("[A-Z][0-9A-Z]*") || memory.contains(param1)){
						System.out.println("Invalid parameter list for user-defined function!");
						System.exit(0);
					}
					memory.add(param1);
					p = p.right;
				}
			}
			// Add stuff to d list
			Interpreter.bodyMap.put(name, s.right.right.left); 
			// add parameter in the list
			Interpreter.param.put(name, new ArrayList<String>());
			SNode p2 = s.right.left;
			while(!p2.value.equals(Token.NIL)){
				Interpreter.param.get(name).add(p2.left.value);
				p2 = p2.right;
			}
		}
		
	//Apply for general functions
	public static String apply(String function, SNode s,  HashMap<String,String> params){
		//evaluate the parameter list
				evlist(s, params);
				//CAR
				if(function.equals("CAR")){
					// Take a list parameter
					if((s.value.equals(Token.NIL)) || !s.right.value.equals(Token.NIL) || Atom.isAtom(s.left.value)){
						System.out.println("Error: parameter for CAR should be list !");
						System.exit(0);
					}
					s.value = s.left.list.get(0);  // only a list
				}
				//CDR
				else if(function.equals("CDR")){
					// Take a list parameter
					if((s.value.equals(Token.NIL)) || !s.right.value.equals(Token.NIL) || Atom.isAtom(s.left.value)){
						System.out.println("Error: parameter for CDR should be list !");
						System.exit(0);
					}
					s.list = new LinkedList<>(s.left.list);
					s.list.remove();
					if(s.list.size() == 1 && s.left.value.contains(".")){
						s.value = s.list.get(0);
						s.list.clear();
					}else{
						s.value = "(";
						for(String str: s.list){
							s.value += str;
						}
						s.value += ")";
					}
				}
				//EQ 
				else if(function.equals("EQ")){
					// takes two atoms
					if(s.value.equals(Token.NIL) || s.right.value.equals(Token.NIL) || !s.right.right.value.equals(Token.NIL) 
							|| !Atom.isAtom(s.left.value) || !Atom.isAtom(s.right.left.value)){
						System.out.println("Error: parameter for EQ should be two atoms !");
						System.exit(0);
					}
					s.value = s.left.value.equals(s.right.left.value) ? "T" :"NIL";
					}
				
				//CONS
				else if(function.equals("CONS")){
					// takes two parameters
					if(s.value.equals(Token.NIL) || s.right.value.equals(Token.NIL) || !s.right.right.value.equals(Token.NIL)){
						System.out.println("Error: parameter for CONS should be two parameters !");
						System.exit(0);
					}
					if(s.left.list.size() == 0 && s.right.left.list.size() == 0){
						s.value = "(" + s.left.value + "." + s.right.left.value + ")";
						s.list.add(s.value);
					}else{
						s.list = new LinkedList<>(s.right.left.list);
						s.list.addFirst(s.left.value);
						//s.list.add(s.right.left.value);
						s.value = "(";
						for(String str: s.list){
							s.value += str + " ";
						}
						s.value += ")";
					}
				}
				//INT
				else if(function.equals("INT")){
					// one parameter
					if(s.value.equals(Token.NIL) || !s.right.value.equals(Token.NIL)){
						System.out.println("Error: parameter for INT should be one parameter !");
						System.exit(0);
					}
					s.value = isInt(s.left.value) ? "T" :"NIL";
				}
				//NULL
				else if(function.equals("NULL")){
					if(s.value.equals(Token.NIL) || !s.right.value.equals(Token.NIL)){
						System.out.println("Error: parameter for NULL should be one parameter !");
						System.exit(0);
					}
					s.value = isNull(s.left) ? "T" :"NIL";
				}
				//ATOM
				else if(function.equals("ATOM")){
					if(s.value.equals(Token.NIL) || !s.right.value.equals(Token.NIL)){
						System.out.println("Error: parameter for NULL should be one parameter !");
						System.exit(0);
					}
					s.value = Atom.isAtom(s.left.value)? "T" :"NIL";
				}
				//Arithmatic functions
				else if(Token.arith.contains(function)){
					//take two integers
					if(s.value.equals(Token.NIL) || s.right.value.equals(Token.NIL) || !s.right.right.value.equals(Token.NIL)){
						System.out.println("Error: parameter for " + function+" should be two parameter !");
						System.exit(0);
					}
					String s1 = s.left.value;
					String s2 = s.right.left.value;
					if(!isInt(s1) || !isInt(s2)){
						System.out.println("Error: parameter for " + function+" should be integers !");
						System.exit(0);
					}
					int a = Integer.parseInt(s1);
					int b = Integer.parseInt(s2);
					if(function.equals("PLUS")){
						s.value = plus(a, b);
					}
					if(function.equals("MINUS")){
						s.value = minus(a, b);
					}
					if(function.equals("TIMES")){
						s.value = times(a, b);
					}
					if(function.equals("QUOTIENT")){
						s.value = quotient(a, b);
					}
					if(function.equals("REMAINDER")){
						s.value = remainder(a, b);
					}
					if(function.equals("GREATER")){
						s.value = greater(a, b);
					}
					if(function.equals("LESS")){
						s.value = less(a, b);
					} 
				}
				// user defined functions
				else if(Interpreter.param.containsKey(function)){
					//validate the parameters
					SNode p = s;
					int count = 0;
					while(!p.value.equals(Token.NIL)){
						count++;
						p = p.right;
					}
					if(count != Interpreter.param.get(function).size()){
						System.out.println("Error: parameters don't match the formal list for :" + function);
						System.exit(0);
					}
					// bound the parameters
					
					p = s;
					for(String param: Interpreter.param.get(function)){
						params.put(param, p.left.value);
						p = p.right;
					}
				
					s.value = Interpreter.eval(Interpreter.bodyMap.get(function), params);
					// need to remove here?
					
				}
				else{
					System.out.println("Error: Invlaid function name :" + function);
					System.exit(0);
				}
				return s.value;
	}
	
	//PLUS
		public static String plus(int a, int b){
			int result = a+b;
			return String.valueOf(result);
		}
		
	    //MINUS
		public static String minus(int a, int b){
			int result = a-b;
			return String.valueOf(result);
		}
		
		//TIMES
		public static String times(int a, int b){
			return String.valueOf(a * b);
		}
		
		//Quotient
		public static String quotient(int a, int b){
			int result = a/b;
			return String.valueOf(result);
		}
		
		//REMAINDER
		public static String remainder(int a, int b){
			return String.valueOf(a % b);
		}
		
		//LESS
		public static String less(int a, int b){
			return a < b ? "T" :"NIL";
		}
		
		//GREATER
		public static String greater(int a, int b){
			return a > b ? "T" :"NIL";
		}
		
}