package com.wseproject.cluster;

import java.util.ArrayList;

/**
 * @author Saurabh Pujar(ssp437)
 *
 */
public class JsonClusterSuper {
	
	private ArrayList<JsonCluster> clusters;
	
	public JsonClusterSuper()
	{
		clusters = new ArrayList<JsonCluster>();
	}

	public ArrayList<JsonCluster> getClusters() {
		return clusters;
	}

	public void setClusters(ArrayList<JsonCluster> clusters) {
		this.clusters = clusters;
	}

}
