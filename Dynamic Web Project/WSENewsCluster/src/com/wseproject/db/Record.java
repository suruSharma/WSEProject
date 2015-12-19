package com.wseproject.db;

/**
 * @author Suruchi Sharma (sss665)
 *
 */
public final class Record {
	private String url;
	private String description;
	private String keywords;
	private String title;

	public Record(String url, String description, String keyword, String title) {
		this.url = url;
		this.description = description;
		this.keywords = keyword;
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public String getDescription() {
		return description;
	}

	public String getKeywords() {
		return keywords;
	}

	public String getTitle() {
		return title;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		Record rhs = (Record) obj;

		return this.url.equals(rhs.url)
				&& this.description.equals(rhs.description)
				&& this.title.equals(rhs.title);
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = url.hashCode();
		hashcode += description.hashCode();
		hashcode += title.hashCode();

		return hashcode;
	}

	public String toString() {
		return this.url + "::" + this.description + "::" + this.title;
	}

}
