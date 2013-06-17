package rs485.nem.versionchecker;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.lwjgl.opengl.Display;

import lombok.Getter;

import com.google.gson.Gson;

public class NEMVersionDownloader extends Thread {
	
	@Getter
	private String nemMcVersion;

	private final Gson gson;
	
	public NEMVersionDownloader(String mcVersion) {
		gson = new Gson();
		nemMcVersion = mcVersion;
		
		// Get the appropriate NEM version string
		try {
			URL url = new URL("http://bot.notenoughmods.com/?json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream inputStream;
			inputStream = (InputStream) conn.getContent();
			Scanner s = new Scanner(inputStream).useDelimiter("\\A");
			String string = s.next();
			s.close();
			String[] availableMcVersions = gson.fromJson(string, String[].class);
			for(String v : availableMcVersions) {
				if(v.contains(mcVersion)) {
					nemMcVersion = v;
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Start the thread
		this.start();
	}
	
	@SuppressWarnings("resource")
	public void run() {
		try {
			URL url = new URL("http://bot.notenoughmods.com/" + getNemMcVersion() + ".json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream inputStream = (InputStream) conn.getContent();
			Scanner s = new Scanner(inputStream).useDelimiter("\\A");
			String string = s.next();
			s.close();
			NEMModInfo[] mods = gson.fromJson(string, NEMModInfo[].class);
			NEMVersionChecker.getInstance().setModInformation(mods);
			NEMVersionChecker.getInstance().getLog().info("Downloaded mod versions from bot.notenoughmods.com for version: " + getNemMcVersion());
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			NEMVersionChecker.getInstance().getLog().severe("Cannot get a modlist json for your Minecraft version " + nemMcVersion);
			e.printStackTrace();
		}
	}
}
