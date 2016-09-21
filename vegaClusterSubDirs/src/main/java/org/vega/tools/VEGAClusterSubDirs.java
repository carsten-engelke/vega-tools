package org.vega.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class VEGAClusterSubDirs {

	// java -jar packCluster.jar clusterSize suffix copy/move directory
	public static void main(String[] args) {

		int cluster = 15;
		File f = new File(System.getProperty("user.dir"));
		String suffix = "";
		boolean copy = true;
		if (args.length > 0) {

			if (args[0].startsWith("-h") || args[0].startsWith("help")) {

				System.out.println("java -jar packCluster.jar clusterSize suffix copy/move directory");
			}
			cluster = Integer.parseInt(args[0]);
			if (args.length > 1) {

				suffix = args[1];

				if (args.length > 2) {

					if (args[2].equals("move") || args[2].equals("0") || args[2].equals("false")) {
						copy = false;
					}

					if (args.length > 3) {

						f = new File(args[3]);
					}
				}
			}
		}

		try {

			packCluster(f, cluster, suffix, copy);
			System.out.println("Files successfully submerged: " + f.getPath() + " " + cluster + " " + suffix);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	static void packCluster(File dir, int clusterSize, final String suffix, boolean copy) throws IOException {

		int count = 0;
		int subcount = 1;
		File Sub = new File(dir.getPath() + "/1");
		Sub.mkdirs();
		for (File f : dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {

				System.out.print(file.getPath());
				if (file.isFile() && (suffix == "" || file.getName().endsWith(suffix))) {
					System.out.println(".accepted");
					return true;
				}
				System.out.println(".rejected");
				return false;
			}
		})) {

			if (count >= clusterSize) {

				subcount++;
				count = 0;
				Sub = new File(dir.getPath() + "/" + subcount);
				Sub.mkdirs();
				System.out.println("Created subdir: " + Sub.getPath());
			}

			if (copy) {

				Files.copy(f.toPath(), new FileOutputStream(Sub.getPath() + "/" + f.getName()));
			} else {

				Files.move(f.toPath(), new File(Sub.getPath() + "/" + f.getName()).toPath());
			}
			count++;
		}
	}
}
