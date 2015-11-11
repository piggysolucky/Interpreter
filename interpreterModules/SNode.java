package classProject.interpreterModules;

import java.util.LinkedList;

/**
 * This class defines the Node for the S expression*/
public class SNode {
	public String value;
	public SNode left, right;
	public boolean isList;
	public boolean isSubtreeList; // whether all the inner node of tree is list
	public LinkedList<String> list = new LinkedList<String>(); // store the result of the evaluation list
	
	public SNode(String val){
		this.value = val;
		left = null;
		right = null;
	}
	
	// deep copy a node
	public SNode(SNode s){
		this(new String(s.value));
		this.isList = s.isList;
		this.isSubtreeList = s.isSubtreeList;
		this.list = new LinkedList<>(s.list);
	}
	
	// copy the tree rooted as SNode
	public static SNode copyTree(SNode s){
		if(s == null){
			return null;
		}
		SNode root = new SNode(s);
		root.left = copyTree(s.left);
		root.right = copyTree(s.right);
		return root;
	}
}
