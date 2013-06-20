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
	private String nemMcVersion;

	private final Gson gson;
	
	public NEMVersionDownloader(String mcVersion) {
		gson = new Gson();
		nemMcVersion = mcVersion;
		
		// Start the thread
		this.start();
	}
	
	@SuppressWarnings("resource")
	public void run() {
		// Get the appropriate NEM version string
		InputStream inputStream = null;
		Scanner s = null;
		try {
			URL url = new URL("http://bot.notenoughmods.com/?json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			inputStream = (InputStream) conn.getContent();
			s = new Scanner(inputStream).useDelimiter("\\A");
			String string = s.next();
			String[] availableMcVersions = gson.fromJson(string, String[].class);
			for(String v : availableMcVersions) {
				if(v.contains(nemMcVersion)) {
					nemMcVersion = v;
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				s.close();
				inputStream.close();
			} catch(Throwable e2) { }
		}
		
		try {
			URL url = new URL("http://bot.notenoughmods.com/" + getNemMcVersion() + ".json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			inputStream = (InputStream) conn.getContent();
			s = new Scanner(inputStream).useDelimiter("\\A");
			String string = s.next();
			s.close();
			NEMModInfo[] mods = gson.fromJson(string, NEMModInfo[].class);
			NEMVersionChecker.getInstance().setModInformation(mods);
			NEMVersionChecker.getInstance().getLog().info("Downloaded mod versions from bot.notenoughmods.com for version: " + getNemMcVersion());
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			NEMVersionChecker.getInstance().getLog().severe("Cannot get a modlist json for your Minecraft version " + nemMcVersion);
			NEMVersionChecker.getInstance().disable();
			e.printStackTrace();
		} finally {
			try {
				s.close();
				inputStream.close();
			} catch(Throwable e2) { }
		}
	}
}
