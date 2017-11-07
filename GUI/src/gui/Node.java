package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class Node {
	
	String name;
	ArrayList<String> value= null;
	Map<String,Node> map=null;
	//String cls_value;
	//ArrayList<Node> children = null;
	
	public Node(String name)
	{
		//children=new ArrayList<Node>();
		value=new ArrayList<String>();
		map=new HashMap<String, Node>();
	    this.name = name;
	    }

	  
}
