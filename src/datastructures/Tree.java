package datastructures;

import java.util.List;
import java.lang.Object;

class TreeNode{
	/**This is a node of binary tree implementation
	 * ====Variables====
	 * data  - holds a number on which tree may be sorted on
	 * left  - left node
	 * right - right node
	 * parent- parent node, to help navigation
	 * extra - Extra data that may be included in the node like a pointer or something
	 * ====Function====
	 * appendExtra - Adds an object to the node
	 * getExtra    - Returns a list of objects
	 * */
	public int data;
	private List<Object> extra;
	public TreeNode left;
	public TreeNode right;
	public TreeNode parent;
	public TreeNode(int data) {
		this.data = data;
	}
	public void appendExtra(Object obj) {
		extra.add(obj);
	}
	public List<Object> getExtra(){
		return extra;
	}
}
public class Tree {
	/**This is the binary tree generic implementation
	 * ====Variables====
	 * root    - holds the root node
	 * current - current working node
	 * ====Function====
	 * moveLeft     - Selects the current node as the left node
	 * moveRight    - Selects the current node as the right node
	 * moveParent   - Selects the current node as the parent of the current node
	 * addLeftNode  - Adds a node to the left of the current node
	 * addRightNode - Adds a node to the right of the current node  
	 * */
	public TreeNode root;
	private TreeNode current;
	public Tree(int data) {
		root = new TreeNode(data);
		root.left=null;
		root.right=null;
		root.parent=null;
		current = root;
	}
	public void moveLeft() throws Exception {
		if(current.left != null) {
			current = current.left;
		}
		else {
			throw new Exception("Left Node does not exist!");
		}
	}
	public void moveRight() throws Exception {
		if(current.right != null) {
			current = current.right;
		}
		else {
			throw new Exception("Right Node does not exist!");
		}
	}
	public void moveParent() throws Exception {
		if(current.parent != null) {
			current = current.parent;
		}
		else {
			throw new Exception("Is a root node, it does not have parent!");
		}
	}
	public void addLeftNode(int data) throws Exception {
		if(current.left!=null) {
			throw new Exception("Node already exists, cannot overwrite!");
		}
		current.left = new TreeNode(data);
		current.left.left = null;
		current.left.right = null;
		current.left.parent = current;
	}
	public void addRightNode(int data) throws Exception {
		if(current.right!=null) {
			throw new Exception("Node already exists, cannot overwrite!");
		}
		current.right = new TreeNode(data);
		current.right.left = null;
		current.right.right = null;
		current.right.parent = current;
	}
	public int getData() {
		return current.data;
	}
}
