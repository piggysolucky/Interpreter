package classProject.interpreterUtils;

import java.util.ArrayList;
import java.util.List;

import classProject.Interpreter;
import classProject.interpreterModules.Atom;
import classProject.interpreterModules.SNode;
import classProject.interpreterModules.Token;

public class Functions {
	
	public static boolean isInt(String s){
		return s.matches("[+-]?[1-9][0-9]*") || s.matches("0");
	}
	
	//EVCON
	public static String evcon(SNode s){
		// a list with two element
		if(Functions.isNull(s) || Atom.isAtom(s.left.value) ||Functions.isNull(s.left.right) || !Functions.isNull(s.left.right.right)){
		System.out.println("Error: Illegal parameter list for COND !");
			System.exit(0);
		}
		if(Interpreter.eval(s.left.left).equals("T")){
			s.value = Interpreter.eval(s.left.right.left);
		}else{
			s.value = evcon(s.right);
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
		s.value = left +(right.equals(Token.NIL) ? "":(" "+right));
		return s.value;
	}
	
	//Null need to be modified more
	public static boolean isNull(SNode s){
		if(s.value.equals(Token.NIL) || s.value.matches("")){
			return true;
		}
		return false;
	}
	
	//CONS
	public static String cons(SNode a, SNode b){
		String p1 = a.value;
		String p2 = b.value;
		if(Atom.isAtom(p1) && Atom.isAtom(p2)){
			return "(" + a.value + "." + b.value +")";
		}
		return "(" + a.value + " " + b.value +")";
	}
	//apply
	public static String apply(String function, SNode s){
		//evaluate the parameter list
		evlist(s.right);
		
		//EQ: Check if two atoms
		if(function.equals("EQ")){
			String param1 = s.right.left.value;
			String param2 = s.right.right.left.value;
			if(!Atom.isAtom(param1) || !Atom.isAtom(param2) || !s.right.right.right.value.equals(Token.NIL) ){
				System.out.println("Error: Invlaid parameter list for EQ!");
				System.exit(0);
			}
			s.value = eq(param1, param2);
		}
		
		//CONS:Check if input contains two parameters
		if(function.equals("CONS")){
			if(isNull(s.right) || isNull(s.right.right) || !s.right.right.right.value.equals(Token.NIL)){
				System.out.println("Error: Invlaid parameter list for CONS!");
				System.exit(0);
			}
			s.value = cons(s.right.left, s.right.right.left);
		}
		
		//case 1: unary function
		if(Token.unary.contains(function)){
			//validate if 1 parameter
			if(!s.right.right.value.equals(Token.NIL)){
				System.out.println("Error: Invlaid parameter list !");
				System.exit(0);
			}
			if(function.equals("INT")){
				s.value = isInt(s.right.left.value) ? "T" :"NIL";
			}
			if(function.equals("ATOM")){
				s.value = Atom.isAtom(s.right.left.value) ? "T" :"NIL";
			}
			if(function.equals("CAR")){
				// Take a list parameter
				if(Atom.isAtom(s.right.left.value)){
					System.out.println("Error: parameter for CAR should be list !");
					System.exit(0);
				}
				String s1 = s.right.left.value;
		        String[] result = s1.substring(1, s1.length()-1).split(" ");
				s.value = result[0];
			}
			if(function.equals("CDR")){
				if(Atom.isAtom(s.right.left.value)){
					System.out.println("Error: parameter for CAR should be list !");
					System.exit(0);
				}
				s.value ="(" + quote(s.right.left.right.left.right) + ")";
			}
			if(function.equals("NULL")){
				s.value = isNull(s.right.left) ? "T" : "NIL";
			}
		}
		
		//case 2: binary arithmatic function
		if(Token.binary.contains(function)){
			//validate the parameters, not int, 2 parameters
			if(isNull(s.right) || isNull(s.right.right)){
				System.out.println("Error: Invlaid parameter list for arighmetic function:" + function);
				System.exit(0);
			}
			String param1 = s.right.left.value;
			String param2 = s.right.right.left.value;
			if(!isInt(param1) || !isInt(param2) || !s.right.right.right.value.equals(Token.NIL) ){
				System.out.println("Error: Invlaid parameter list for arighmetic function :" + function);
				System.exit(0);
			}
			int a = Integer.parseInt(param1);
			int b = Integer.parseInt(param2);
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
		if(s.value.equals(Token.Dot)){
			System.out.println("Error: Invlaid function name :" + function);
			System.exit(0);
		}
		return s.value;
	}
	
	//evlist: recursively evaluate the parameter list of the function
	public static void evlist(SNode s){
		if(s.value.equals(Token.NIL)){
			return;
		}
		if(s.left.value.equals(Token.Dot)){
			s.left.value = Interpreter.eval(s.left);
		}
		evlist(s.right);
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
	
	//EQ
	public static String eq(String a, String b){
		return a.equals(b) ? "T" :"NIL";
	}
}