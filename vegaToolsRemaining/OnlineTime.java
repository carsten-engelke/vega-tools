package org.vega.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OnlineTime {

	// <!DOCTYPE html>
	// <html>
	// <body>
	//
	// <h1>Time Server</h1>
	// Current UTC-Timestamp:

	// <time><?php echo time(); ?></time>
	// </body>
	// </html>

	URL urlTimeServer = null;

	OnlineTime(URL urlTimeServer) {

		this.urlTimeServer = urlTimeServer;
	}

	public Date getTime() {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(urlTimeServer.openStream()));
			String s = br.readLine();
			while (s != null) {

				if (s.contains("<time>") && s.contains("</time>")) {

					return new Date(Long.parseLong(s.substring(s.indexOf("<time>") + 6, s.indexOf("</time>"))) * 1000);
				}
				s = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {

		try {
			OnlineTime t = new OnlineTime(new URL("http://vega-north.org/engine/time.php"));
			Date d = t.getTime();
			System.out.print("Set time to: " + d.toString() + "...");
			DateFormat date = new SimpleDateFormat("dd-MM-yy");
			DateFormat time = new SimpleDateFormat("HH:mm:ss");
			
			Runtime.getRuntime().exec("cmd /C time " + time.format(d)); // hh:mm:ss
			Runtime.getRuntime().exec("cmd /C date " + date.format(d)); // dd-MM-yy
			System.out.println("done");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}