package com.wseproject.main;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.wseproject.crawler.Crawler;
import com.wseproject.utils.Constants;

/**
 * @author Saurabh Pujar(ssp437)
 *
 */
public class NewsCrawler {
	static int region = Constants.INDIA;
	static FileWriter fileWritter = null;
	static PrintWriter printWriter = null;

	void startCrawler() {

		Timer timer = new Timer();
		TimerTask hourlyTask = new TimerTask() {
			@Override
			public void run() {
				try {
					System.out.println(region + " Starting Crawler : "
							+ (new Date()));

					Crawler c = new Crawler();
					c.startCrawler(region);

					System.out.println(region + " Ending Crawler : "
							+ (new Date()));
					System.out
							.println("----------------------------------------------------------------------------");
					if (region == Constants.INDIA) {
						region = Constants.WORLD;
					} else if (region == Constants.WORLD) {
						region = Constants.INDIA;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		timer.schedule(hourlyTask, 0l, Constants.MILLISECONDS
				* Constants.SECONDS * Constants.MINUTES);
	}
}
