package org.vega.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateBatchFile {

	public static void main(String[] args) {

		try {
			FileWriter fw = new FileWriter("cmd.bat");

			for (File f : new File(System.getProperty("user.dir")).listFiles()) {

				if (f.getName().endsWith(".epub")) {

					fw.write("call \"C:/Users/carst/Programme/Tools/Calibre Portable/Calibre/ebook-convert.exe\" \""
							+ f.getPath() + "\" \"" + f.getPath().substring(0, f.getPath().length() - 5) + ".mobi\"\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
