package com.wseproject.cluster;

import java.util.ArrayList;

import com.wseproject.db.Record;

/**
 * @author Saurabh Pujar(ssp437)
 *
 */
public class JsonCluster {
	private String clusName;
	private ArrayList<Record> records;

	public JsonCluster() {
		clusName = "";
		records = new ArrayList<Record>();
	}

	public String getClusName() {
		return clusName;
	}

	public ArrayList<Record> getRecords() {
		return records;
	}

	public void setClusName(String clusName) {
		this.clusName = clusName;
	}

	public void setRecords(ArrayList<Record> records) {
		this.records = records;
	}
}
