package org.vega.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

public class MergeGPX {

	public static void main(String[] args) {

		String dir = "";
		if (args.length > 0) {
			dir = args[0];
		} else {
			dir = System.getProperty("user.dir");
		}
		File[] gpxFiles = new File(dir).listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {

				if (name.endsWith(".gpx")) {
					return true;
				} else {
					return false;
				}
			}
		});

		if (gpxFiles.length < 2) {
			System.out
					.println("Not enough gpx files found - specify directory");
			System.exit(0);
		}
		File merged = new File("merged.gpx");
		String mergedString = "";

		try {
			BufferedReader br = new BufferedReader(new FileReader(gpxFiles[0]));
			String s = br.readLine();
			while (s != null) {

				mergedString += s + "</>";
				s = br.readLine();
			}
			mergedString = mergedString.substring(0, mergedString.length() - 3);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 1; i < gpxFiles.length; i++) {

			String merge2 = "";
			try {
				BufferedReader br = new BufferedReader(new FileReader(
						gpxFiles[i]));
				String s = br.readLine();
				while (s != null) {

					merge2 += s + "</>";
					s = br.readLine();
				}
				if (!merge2.contains("<trk>") || !merge2.contains("</trk>")) {
					System.out.println("no tracks found inside gpx file"
							+ gpxFiles[i].getPath());
					continue;
				}
				merge2 = merge2.substring(merge2.indexOf("<trk>"),
						merge2.lastIndexOf("</trk>") + 6);
				br.close();
				System.out.println(merge2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mergedString = mergedString.substring(0,
					mergedString.lastIndexOf("</trk>") + 6)
					+ merge2
					+ mergedString
							.substring(mergedString.lastIndexOf("</trk>") + 6);
		}

		mergedString = mergedString.replace("</>", "\n");
		try {
			FileWriter fw = new FileWriter(merged);
			fw.write(mergedString);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
