package classProject.interpreterUtils;

import java.util.List;

import classProject.interpreterModules.SNode;
import classProject.interpreterModules.Token;

public class Printer {
	public static void print(List<SNode> list){
		for(SNode l: list){
			//Different case for dot and list notation
			//list notation
			if(l.isSubtreeList){
				if(l.right == null && l.left == null){
					System.out.println(l.value);
					continue;
				}
				System.out.print(Token.OpenParenthesis);
				printListNotation(l);
				System.out.print(Token.ClosingParenthesis);
				System.out.println("\n");
			}else{
				printDotNotation(l);
				System.out.println("\n");
			}
			
		}
	}
	
	// print the list in list notation
	public static void printListNotation(SNode s){
		//print and return if leaf node and if it's not NILL
		if(s.value.equals(Token.NIL)){
			return;
		}
		if(s.left == null && s.right == null){
			System.out.print(s.value + " ");
			return;
		}
		
		if(s.left.value.equals(Token.Dot) && (s.left.right.value.equals(Token.NIL)||
				s.left.right.value.equals(Token.Dot))){
			System.out.print(Token.OpenParenthesis);
			printListNotation(s.left);
			System.out.print(Token.ClosingParenthesis);
		} else {
			printListNotation(s.left);
		}
		// Bracket is from left to right
		printListNotation(s.right);	
	}
	
	//Print the Dot notation
	public static void printDotNotation(SNode s){
		    if(s.left == null && s.right == null){
		    	System.out.print(s.value);
		    	return;
		    }
			System.out.print(Token.OpenParenthesis);
			printDotNotation(s.left);
			System.out.print(s.value);
			printDotNotation(s.right);
			System.out.print(Token.ClosingParenthesis);
		
	}
}
