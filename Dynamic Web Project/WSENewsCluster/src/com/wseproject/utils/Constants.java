package com.wseproject.utils;

/**
 * @author Suruchi Sharma(sss665)
 * 
 * Constants that are used in the application. New constants to be added here.
 *
 */
public final class Constants {

	// Database Tables
	public static final String RECORDSIND = "recordsindia";
	public static final String RECORDSWORLD = "recordsworld";
	public static final String SEEDINDIA = "seedsindia";
	public static final String SEEDWORLD = "seedsworld";

	// Region constants
	public static final int INDIA = 1;
	public static final int WORLD = 2;

	// Maximum number of valid links from each seed link
	public final static int MAX_COUNT = 20;

	public final static int MILLISECONDS = 1000;
	public final static int SECONDS = 60;
	public final static int MINUTES = 180;
	
	//Clustering
	public final static int K = 350;
	public final static int ITERATIONS = 12;
	public final static int LINKS = 5;
}
