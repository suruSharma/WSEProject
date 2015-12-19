package com.wseproject.crawler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wseproject.db.DatabaseUtils;
import com.wseproject.db.Record;
import com.wseproject.utils.Constants;

/**
 * @author Suruchi Sharma (sss665)
 *
 */
public class Crawler {

	public DatabaseUtils dbUtils;

	HashSet<Record> recordSet = null;
	Hashtable<String, Integer> recordsTable = null;
	String recordTable = Constants.RECORDSIND;
	int region = Constants.INDIA;

	public Crawler() {
		dbUtils = new DatabaseUtils();
		recordSet = new HashSet<Record>();
		recordsTable = new Hashtable<String, Integer>();
	}

	void addRecords() {
		System.out.println("-------------------------------------------------------------------------------------------------------------");
		System.out.println("Adding = "+recordSet.size()+" records to "+recordTable);
		for (Record rec : recordSet) {
			String sql = "INSERT INTO " + recordTable
					+ " (url,docText,keyword,title) VALUES " + "(?,?,?,?);";
			PreparedStatement stmt;
			try {
				System.out.println("Adding : "+rec.getTitle());
				stmt = dbUtils.conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, rec.getUrl());
				stmt.setString(2, rec.getDescription());
				stmt.setString(3, rec.getKeywords());
				stmt.setString(4, rec.getTitle());
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	Record cleanRecord(Record rec) {
		Record returnVal = null;
		ArrayList<String> toRemove = new ArrayList<String>();

		toRemove.add(" - CBS News");
		toRemove.add(" - LA Times");
		toRemove.add(" - Firstpost");
		toRemove.add(" - The Hindu");

		toRemove.add("|");
		String description = rec.getDescription() == null ? "" : rec
				.getDescription();
		String keyword = rec.getKeywords() == null ? "" : rec.getKeywords();
		String title = rec.getTitle() == null ? "" : rec.getTitle();
		for (String str : toRemove) {
			if (description.contains(str))
				description = description
						.substring(0, description.indexOf(str));
			if (keyword.contains(str))
				keyword = keyword.substring(0, keyword.indexOf(str));
			if (title.contains(str))
				title = title.substring(0, title.indexOf(str));

		}

		returnVal = new Record(rec.getUrl(), description, keyword, title);

		return returnVal;

	}

	void crawlPage(String url) {
		System.out.println("Processing seed link = "+url);
		Document doc;
		int count = 0;
		try {
			doc = Jsoup.connect(url).get();

			Elements links = doc.select("a[href]");
			Document childDoc;
			for (Element link : links) {
				if (count < Constants.MAX_COUNT) {
					try {
						String linkHref = link.attr("href");
						if (linkHref.startsWith("/")
								&& !linkHref.startsWith("//")) {
							linkHref = url + linkHref.substring(1);
						}
						if (!(linkHref.equals(url)) && linkHref.contains(url)
								&& isValidURL(linkHref)) {

							childDoc = Jsoup.connect(linkHref).get();
							String description = getDescription(childDoc);
							String keywords = getKeywords(childDoc);
							if (description == null || description.equals("")) {
								description = link.text();
							}

							String title = childDoc.title();
							Record rec = new Record(linkHref, description,
									keywords, title);

							if (!recordSet.contains(rec) && description != null
									&& !description.equals("")) {
								if (recordsTable.containsKey(linkHref)) {
									continue;
								} else {
									recordsTable.put(linkHref, 1);
									rec = cleanRecord(rec);
									recordSet.add(rec);
									count++;
								}

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else
					return;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String getDescription(Document doc) {
		ArrayList<String> descriptionTags = new ArrayList<String>();
		descriptionTags.add("meta[name=description]");
		descriptionTags.add("meta[itemprop=description]");
		descriptionTags.add("meta[property=og:description]");

		for (String tag : descriptionTags) {
			if (doc.select(tag) != null && doc.select(tag).first() != null) {
				return doc.select(tag).first().attr("content");
			}
		}

		return null;
	}

	String getKeywords(Document doc) {
		ArrayList<String> keyWordTags = new ArrayList<String>();
		keyWordTags.add("meta[name=keywords]");
		keyWordTags.add("meta[name=news_keywords]");

		for (String tag : keyWordTags) {
			if (doc.select(tag) != null && doc.select(tag).first() != null) {
				return doc.select(tag).first().attr("content");
			}
		}

		return null;
	}

	boolean isValidURL(String link) {
		ArrayList<String> validContains = new ArrayList<String>();
		validContains.add("/tech/tech-news/");
		validContains.add("/india/");
		validContains.add("/world/");
		validContains.add("/world-neighbours/article");
		validContains.add("/technology-science-and-trends/article/");
		validContains.add("/world-asia/article");
		validContains.add("/world-middle-east/article");
		validContains.add("/nation-current-affairs/article/");
		validContains.add("/sci-tech/");
		validContains.add("/net-news/");
		validContains.add("/nation-current-affairs/article/");
		validContains.add("/tech/");
		validContains.add("/india-news/");
		validContains.add("/world-news/");
		validContains.add("/scitech/");
		validContains.add("/news/");
		validContains.add("/politics/");
		validContains.add("/business/");
		validContains.add("/us/");
		validContains.add("/europe/");
		validContains.add("/asia/");
		validContains.add("/uk/");
		validContains.add("/2015/");

		ArrayList<String> invalidEndsWith = new ArrayList<String>();
		invalidEndsWith.add("index.html");
		invalidEndsWith.add("index.jsp");
		invalidEndsWith.add("index.jsp");
		invalidEndsWith.add("reviews");
		invalidEndsWith.add("more-gadgets");
		invalidEndsWith.add("/tech/tech-news");
		invalidEndsWith.add("/india");
		invalidEndsWith.add("/world");
		invalidEndsWith.add("/world-neighbours");
		invalidEndsWith.add("/technology-science-and-trends");
		invalidEndsWith.add("/world-asia/article");
		invalidEndsWith.add("/world-middle-east");
		invalidEndsWith.add("/nation-current-affairs");
		invalidEndsWith.add("/sci-tech");
		invalidEndsWith.add("/net-news");
		invalidEndsWith.add("/nation-current-affairs");
		invalidEndsWith.add("/tech");
		invalidEndsWith.add("/india-news");
		invalidEndsWith.add("/world-news");
		invalidEndsWith.add("/scitech");
		invalidEndsWith.add("/hindi");
		invalidEndsWith.add("/bengali");
		invalidEndsWith.add("/marathi");
		invalidEndsWith.add("/sports");
		invalidEndsWith.add("?cbzmid=14629&cbzpage=scorecard#scorecard");
		invalidEndsWith.add("/rss.html");
		invalidEndsWith.add("/schedule");
		invalidEndsWith.add("/events");
		invalidEndsWith.add("/slideshow");
		invalidEndsWith.add("/spl3-lid.aspx");
		invalidEndsWith.add("#navtype=navbar");
		invalidEndsWith.add("/#navtype=outfit");
		invalidEndsWith.add("/politics");
		invalidEndsWith.add("/battleground");
		invalidEndsWith.add("/white-house");
		invalidEndsWith.add("/us");
		invalidEndsWith.add("/europe");
		invalidEndsWith.add("/asia");
		invalidEndsWith.add("/uk");
		invalidEndsWith.add("/news");
		invalidEndsWith.add("/business");
		invalidEndsWith.add("/commentisfree");
		invalidEndsWith.add("/sport");
		invalidEndsWith.add("/technology");
		invalidEndsWith.add("/culture");
		invalidEndsWith.add("/environment");
		invalidEndsWith.add("/nepal");
		invalidEndsWith.add("/baltimore");
		invalidEndsWith.add("/technology");
		invalidEndsWith.add("/2015");
		invalidEndsWith.add("/al-qaida");
		invalidEndsWith.add("/syria");
		invalidEndsWith.add("?hp&target=comments#commentsContainer");
		invalidEndsWith.add("/breaking-news");
		invalidEndsWith.add("/south-america");
		invalidEndsWith.add("/africa");
		invalidEndsWith.add("/middle-east");
		invalidEndsWith.add("/pacific");
		invalidEndsWith.add("/north-america");
		invalidEndsWith.add("/south-america");
		invalidEndsWith.add("#comments");
		invalidEndsWith.add("#video");
		invalidEndsWith.add("/americas");
		invalidEndsWith.add("/mid-east");
		invalidEndsWith.add("/web");
		invalidEndsWith.add("/web");
		invalidEndsWith.add("/market");
		invalidEndsWith.add("/neighbours");
		invalidEndsWith.add("/autos");
		invalidEndsWith.add("/companies");
		invalidEndsWith.add("/economics");
		invalidEndsWith.add("/latest");
		invalidEndsWith.add("/market");
		invalidEndsWith.add("/international");
		invalidEndsWith.add("/national");
		invalidEndsWith.add("/cities");
		invalidEndsWith.add("/budget");
		invalidEndsWith.add("/industry");
		invalidEndsWith.add("/economy");
		invalidEndsWith.add("/markets");
		invalidEndsWith.add("/Industry");
		invalidEndsWith.add("/Economy");
		invalidEndsWith.add("/health");
		invalidEndsWith.add("/science");
		invalidEndsWith.add("/energy-and-environment");
		invalidEndsWith.add("/isreal");

		ArrayList<String> invalidContains = new ArrayList<String>();
		invalidContains.add("/hindi/");
		invalidContains.add("/bengali/");
		invalidContains.add("/marathi/");
		invalidContains.add("/videos/");
		invalidContains.add("/video/");
		invalidContains.add("/photos/");
		invalidContains.add("articlelistls");
		invalidContains.add("/slideshow/");
		invalidContains.add("/#navtype");
		invalidContains.add("/topics/");
		invalidContains.add("?utm_source=navigation");

		boolean retValue = false;
		for (String str : validContains) {
			if (link.contains(str)) {
				retValue = true;
				break;
			}

		}

		if (retValue) {
			for (String str : invalidEndsWith) {
				if (link.endsWith(str) || link.endsWith(str + "/")) {
					retValue = false;
					break;
				}
			}
		}

		if (retValue) {
			for (String str : invalidContains) {
				if (link.contains(str)) {
					retValue = false;
					break;
				}
			}
		}

		return retValue;
	}
	
	public void startCrawler(int region) {
		try {
			this.region = region;
			if (region == Constants.WORLD)
				recordTable = Constants.RECORDSWORLD;
			else
				recordTable = Constants.RECORDSIND;

			dbUtils.runQueryBool("TRUNCATE " + recordTable);
			System.out.println("Truncated records table");
		} catch (Exception e) {
			e.printStackTrace();
		}
		startCrawling();
		addRecords();
	}

	public void startCrawling() {
		try {
			String seedsTable;
			if (region == Constants.INDIA)
				seedsTable = Constants.SEEDINDIA;
			else
				seedsTable = Constants.SEEDWORLD;
			String sql = "select link from " + seedsTable;
			ResultSet rs = dbUtils.runQueryRS(sql);
			while (rs.next()) {
				String seedLink = rs.getString("link");
				crawlPage(seedLink);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}