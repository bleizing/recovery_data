package com.bleizing.recovery.helper;

import java.util.Calendar;

public class FileHelper {
	public static String createFileNameByDate() {
		String filename = "GeneratedSQL";
		filename += "-" + Calendar.getInstance().getTimeInMillis();
		filename += ".pdf";
		
		return filename;
	}
}
