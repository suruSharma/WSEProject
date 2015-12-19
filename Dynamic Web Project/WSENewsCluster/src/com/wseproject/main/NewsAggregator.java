package com.wseproject.main;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.*;
import com.wseproject.cluster.BagOfWords;
import com.wseproject.cluster.JsonClusterSuper;
import com.wseproject.cluster.KMeansCluster;
import com.wseproject.utils.Constants;

/**
 * @author Saurabh Pujar(ssp437)
 *
 */
public class NewsAggregator {

	public String getJson(int clusterCount, int region) {

		String recordsTable = Constants.RECORDSIND;
		if(region == Constants.WORLD)
			recordsTable = Constants.RECORDSWORLD;
		else if(region == Constants.INDIA)
			recordsTable = Constants.RECORDSIND;
		BagOfWords bow = new BagOfWords(recordsTable);
		try {
			bow.createBOW();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		bow.createWC();

		KMeansCluster kObj = new KMeansCluster(bow, Constants.K, Constants.ITERATIONS, clusterCount, Constants.LINKS);

		kObj.Cluster();

		kObj.getKeywords();

		kObj.reformCLuster2();

		kObj.getKeywords();
		
		kObj.reformCLuster2();

		kObj.formDisplayCluster();

		JsonClusterSuper jsonCluster = kObj.printToJsonObj();
		Gson gson = new Gson();

		String json = gson.toJson(jsonCluster);

		return json;
	}

}