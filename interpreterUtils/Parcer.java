package classProject.interpreterUtils;

import java.util.LinkedList;
import java.util.List;

import classProject.interpreterModules.Atom;
import classProject.interpreterModules.SNode;
import classProject.interpreterModules.Token;


/**
 * construct the binary tree and compute the n.list value for each node*/
public class Parcer {
	//Store the root of different parseTrees
	public static List<SNode> treeRoot = new LinkedList<SNode>();
	private static boolean isTreeList;  // Denote if every inner node isList is true
	
	public static List<SNode> ParseStart(){
		isTreeList = true;
		SNode root = ParseSexp();
		//Check for end of file
		while(root != null){
			root.isSubtreeList = isTreeList;
			treeRoot.add(root);
			isTreeList = true;
			root = ParseSexp();
		}
		return treeRoot;
	}
	
	//This method parse the SEpression recursively
	public static SNode ParseSexp(){
		String token = MyScanner.getNextToken();
		SNode left = null;
		if(token.equals(Token.End)){
			return null;
		}else if(Atom.isAtom(token)){ //Create a node and return the node
			return new SNode(token);
		}else if(token.equals(Token.ERROR)){
			System.out.println("ERROR: line 111 in Interpreter.java: Invalid atom in input !");
			System.exit(0);
		}else if(!token.equals(Token.OpenParenthesis)){
			System.out.println("ERROR: line 114 in Interpreter.java: Missing open parenthesis !");
			System.exit(0);
		}else if(token.equals(Token.OpenParenthesis)){
			left = ParseSexp();
		}
		
		String middle = MyScanner.getNextToken();
		if(!middle.equals(Token.Dot)){
			System.out.println("ERROR: line 122 in Interpreter.java: Missing Dot !");
			System.exit(0);
		}
		SNode root = new SNode(middle);
		//Build the binary tree
		if(left != null){
			root.left = left;
		}
		SNode right = ParseSexp();
		if(right != null){
			root.right = right;
		}
		//Compute isList value for each inner node
		if(root.right.value.equals(Token.NIL) || root.right.isList){
			root.isList = true;
		}
		if(!root.isList){
			isTreeList = false;
		}
		String close = MyScanner.getNextToken();
		if(!close.equals(Token.ClosingParenthesis)){
			System.out.println("ERROR: line 143 in Interpreter.java: Missing Closing Paranthesis !");
			System.exit(0);
		}
		return root;
	}
	
}