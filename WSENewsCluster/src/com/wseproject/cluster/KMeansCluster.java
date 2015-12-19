package com.wseproject.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import com.wseproject.db.Record;

/**
 * @author Saurabh Pujar(ssp437)
 *
 */
public class KMeansCluster {

	public HashMap<Integer, Cluster> clus;
	public HashMap<Integer, Cluster> dispclus;
	private BagOfWords bow;
	private int k;
	private int iter;
	private int totaldispclus;
	private int linksperclus;
	public ArrayList<Cluster> disporder;

	public KMeansCluster(BagOfWords b, int kval, int it, int t, int l) {
		bow = b;
		k = kval;
		iter = it;
		totaldispclus = t;
		linksperclus = l;
		clus = new HashMap<Integer, Cluster>();
		dispclus = new HashMap<Integer, Cluster>();
		disporder = new ArrayList<Cluster>();
		initClusters();
	}

	public KMeansCluster(BagOfWords b) {
		bow = b;
		k = 5;
		iter = 5;
		totaldispclus = 5;
		linksperclus = 5;
		clus = new HashMap<Integer, Cluster>();
		disporder = new ArrayList<Cluster>();
		initClusters();
	}

	public void initClusters() {
		Random random = new Random();
		ArrayList<Integer> keys = new ArrayList<Integer>(bow.getWordcount()
				.keySet());
		for (int i = 0; i < k; i++) {
			Cluster k = new Cluster();
			int id = keys.get(random.nextInt(keys.size() - 1));
			k.id = i + 1;
			k.coordinates = bow.getWordcount().get(id);
			clus.put(id, k);
		}
		for (int i : bow.getWordcount().keySet()) {
			int c = nearestCenter(i);
			Cluster k = clus.get(c);
			k.elements.add(i);
		}
	}

	public double EuclDist(int id1, int id2) {
		double sum = 0;
		double dist = 0;
		int n = bow.getGlobalwordlist().size();
		for (int i = 0; i < n; i++) {
			double diff = clus.get(id1).coordinates.get(i)
					- bow.getWordcount().get(id2).get(i);
			sum = sum + (diff * diff);
		}

		dist = Math.sqrt(sum);

		return dist;
	}

	public int nearestCenter(int id) {
		int minid = clus.entrySet().iterator().next().getKey();
		double mindist = EuclDist(minid, id);
		for (int i : clus.keySet()) {
			double dist = EuclDist(i, id);
			if (dist < mindist) {
				minid = i;
				mindist = dist;
			}
		}
		return minid;
	}

	public ArrayList<Integer> getCentroid(ArrayList<Integer> points) {
		int n = bow.getGlobalwordlist().size();
		ArrayList<Integer> cent = new ArrayList<Integer>(Collections.nCopies(n,
				0));
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < points.size(); j++) {
				int check = bow.getWordcount().get(points.get(j)).get(i);
				int sum = cent.get(i) + check;
				cent.set(i, sum);
			}
		}
		for (int i = 0; i < n; i++) {
			int c = (cent.get(i) / points.size());
			cent.set(i, c);
		}
		return cent;
	}

	public void Cluster() {
		int n = iter;
		while (n > 0) {
			for (int i : clus.keySet()) {
				if (!clus.get(i).elements.isEmpty()) {
					clus.get(i).coordinates = getCentroid(clus.get(i).elements);
					clus.get(i).elements = new ArrayList<Integer>();
				}
			}
			for (int i : bow.getWordcount().keySet()) {
				int c = nearestCenter(i);
				clus.get(c).elements.add(i);
			}
			n = n - 1;
		}
	}

	public void getKeywords() {
		for (Cluster k : clus.values()) {
			int keyloc = 0;
			int keyval = -1;
			for (int i = 0; i < k.coordinates.size(); i++) {
				if (k.coordinates.get(i) > keyval) {
					keyloc = i;
					keyval = k.coordinates.get(i);
					k.keywords = new ArrayList<String>();
					k.keywords.add(bow.getGlobalwordlist().get(keyloc));
					k.clubkey = bow.getGlobalwordlist().get(keyloc);
					k.clubkey.toLowerCase();
				} else if (k.coordinates.get(i) == keyval && (i == keyloc + 1)) {
					if (k.keywords.size() > 0) {
						k.keywords.add(bow.getGlobalwordlist().get(i));
						k.clubkey = k.clubkey + bow.getGlobalwordlist().get(i);
						k.clubkey.toLowerCase();
					}
				}
			}
		}
	}

	public void reformCLuster2() {
		HashMap<Integer, Cluster> newclus = new HashMap<Integer, Cluster>();
		for (Cluster k : clus.values()) {
			if (!k.elements.isEmpty()) {
				int check = 0;
				for (Cluster n : newclus.values()) {
					for (String str : k.keywords) {
						if (n.keywords.contains(str)) {
							n.elements.addAll(k.elements);
							check = 1;
							break;
						}
					}
				}
				if (check == 0) {
					newclus.put(k.id, k);
				}
			}

		}
		clus = newclus;
	}

	public void reformCLuster() {
		HashMap<Integer, Cluster> newclus = new HashMap<Integer, Cluster>();
		for (Cluster k : clus.values()) {

			if (!k.elements.isEmpty()) {
				int check = 0;
				for (Cluster n : newclus.values()) {
					if (n.clubkey.equals(k.clubkey)) {
						n.elements.addAll(k.elements);
						check = 1;
						break;
					}
				}
				if (check == 0) {
					newclus.put(k.id, k);
				}
			}

		}
		clus = newclus;
	}

	public void formDisplayCluster() {
		int total = totaldispclus;
		HashMap<Integer, Cluster> check = clus;
		while (total > 0) {
			int maxid = 0;
			int id = 0;
			int each = linksperclus;
			for (Cluster k : check.values()) {
				if (!(dispclus.containsKey(k.id))) {
					if (k.elements.size() > maxid) {
						maxid = k.elements.size();
						id = k.id;
					}
				}
			}
			Cluster k = check.get(id);

			ArrayList<Integer> links = k.elements;
			k.elements = new ArrayList<Integer>();
			while (each > 0) {
				if (links.size() > 0) {
					int minid = 0;
					double mindist = EuclDist(k.id, links.get(minid));
					for (int i = 0; i < links.size(); i++) {
						double dist = EuclDist(k.id, links.get(i));
						if (dist < mindist) {
							mindist = dist;
							minid = i;
						}
					}
					k.elements.add(links.get(minid));
					links.remove(minid);
				}
				each = each - 1;
			}
			disporder.add(k);
			dispclus.put(k.id, k);
			total = total - 1;
		}
	}
	
	public JsonClusterSuper printToJsonObj() {
		JsonClusterSuper arr = new JsonClusterSuper();
		ArrayList<JsonCluster> c = arr.getClusters();
		for (Cluster i : disporder) {
			JsonCluster j = new JsonCluster();
			String title = "";
			for (String s : i.keywords) {
				s = s.substring(0, 1).toUpperCase() + s.substring(1);
				title = title + " " + s;
			}
			j.setClusName(title);
			ArrayList<Record> rarr = j.getRecords();
			for (int id : i.elements) {
				rarr.add(bow.getRecords().get(id));
				
			}
			j.setRecords(rarr);
			c.add(j);			
		}
		arr.setClusters(c);
		return arr;
	}
}
