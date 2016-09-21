package org.vega.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

public class FileSorter {

	public static void main(String[] args) {

		if (args.length >0) {
			System.exit(0);
		}
		ArrayList<File> fileList = new ArrayList<File>();

		addFiles(fileList, new File(System.getProperty("user.dir")));
		String[] sort = new String[] { "a,A:A", "b,B:B", "c,C:C", "d,D:D", "e,E:E", "f,F:F", "g,G:G", "h,H:H",
				"i,I,j,J:IJ", "k,K:K", "l,L:L", "m,M:M", "n,N:N", "o,O:O", "p,P,q,Q:PQ", "r,R:R", "s,S:S", "t,T:T", "u,U,v,V:UV", "w,W:W", "x,X,y,Y,z,Z:XYZ", "[english],[English],[englisch],[Englisch]:Englisch"};
		String[][] sort2 = new String[sort.length][7];
		int i = 0;
		for (String c: sort) {
			
			sort2[i][6] = c.split(":")[1];
			String[] a = c.split(":")[0].split(",");
			for (int j = 0; j< a.length; j++) {
				
				sort2[i][j] = a[j];
			}
			i++;
		}
		System.out.println(fileList.size() + " files found");
		for (File f: fileList) {
			
			boolean found = false;
			for (i = 0; i< sort.length; i++) {
				
				for (int j = 0; j< 7; j++) {
					
					if (sort2[i][j] != null) {
						
						if (f.getName().startsWith(sort2[i][j])) {
							
							File dest = new File(System.getProperty("user.dir") + "/" + sort2[i][6]);
							if (!dest.exists()) {
								dest.mkdirs();
							}
							found = true;
							try {
								Files.move(f.toPath(), new File(dest.getPath() + "/" + f.getName()).toPath());
								System.out.println(f.getName() + " moved");
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						}
					}
				}
				if (found) {
					break;
				}
			}
			if (!found) {
				
				File dest = new File(System.getProperty("user.dir") + "/notFound");
				if (!dest.exists()) {
					dest.mkdirs();
				}
				try {
					Files.move(f.toPath(), new File(dest.getPath() + "/" + f.getName()).toPath());
					System.out.println(f.getName() + " not found");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		ArrayList<File> dirs = new ArrayList<File>();
		
		addDirs(dirs, new File(System.getProperty("user.dir")));
		
		Collections.reverse(dirs);
		for (File dir : dirs) {
			
			if (dir.isDirectory() && dir.listFiles().length == 0) {
				
				try {
					Files.delete(dir.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	static void addFiles(ArrayList<File> l, File dir) {

		File[] files = dir.listFiles();
		
		System.out.print("\rFiles found: " + l.size());
		for (File f : files) {

			if (f.isDirectory()) {

				addFiles(l, f);
			} else {

				l.add(f);
			}
		}
	}
	
	static void addDirs(ArrayList<File> l, File dir) {

		File[] files = dir.listFiles();
		
		System.out.print("\rDirs found: " + l.size());
		for (File f : files) {

			if (f.isDirectory()) {
				
				l.add(f);
				addFiles(l, f);
			}
		}
	}

}
