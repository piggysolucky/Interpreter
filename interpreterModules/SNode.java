package classProject.interpreterModules;
/**
 * This class defines the Node for the S expression*/
public class SNode {
	public String value;
	public SNode left, right;
	public boolean isList;
	public boolean isSubtreeList; // whether all the inner node of tree is list
	
	public SNode(String val){
		this.value = val;
		left = null;
		right = null;
	}
}
