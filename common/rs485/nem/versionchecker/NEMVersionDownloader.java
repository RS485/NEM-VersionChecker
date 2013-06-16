package rs485.nem.versionchecker;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import lombok.Getter;

import com.google.gson.Gson;

public class NEMVersionDownloader extends Thread {
	
	@Getter
	private final String mcVersion;
	
	public NEMVersionDownloader(String version) {
		mcVersion = version;
		this.start();
	}
	
	@SuppressWarnings("resource")
	public void run() {
		try {
			URL url = new URL("http://bot.notenoughmods.com/" + getMcVersion() + ".json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream inputStream = (InputStream) conn.getContent();
			Scanner s = new Scanner(inputStream).useDelimiter("\\A");
			String string = s.next();
			s.close();
			Gson gson = new Gson();
			NEMModInfo[] mods = gson.fromJson(string, NEMModInfo[].class);
			NEMVersionChecker.getInstance().setModInformation(mods);
			NEMVersionChecker.getInstance().getLog().info("Downloaded mod versions from bot.notenoughmods.com for version: " + getMcVersion());
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
