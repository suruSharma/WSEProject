package com.wseproject.cluster;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Saurabh Pujar (ssp437)
 *
 */
public class Cluster {

	public int id;
	public ArrayList<Integer> coordinates;
	public ArrayList<Integer> elements;
	public HashMap<Integer,Integer> dist;
	public ArrayList<String> keywords;
	public String clubkey;
	
	public Cluster()
	{
		id = 0;
		clubkey = "";
		coordinates = new ArrayList<Integer>();
		elements = new ArrayList<Integer>();
		dist = new HashMap<Integer,Integer>();
		keywords = new ArrayList<String>();
		
	}
}
