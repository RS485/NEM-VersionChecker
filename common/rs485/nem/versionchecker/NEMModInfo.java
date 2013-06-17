package rs485.nem.versionchecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

public class NEMModInfo {
	@Getter
	private String name;
	@Getter
	private String version;
	@Getter
	private String longurl;
	@Getter
	private String shorturl;
	@Getter
	private String aliases;
	@Getter
	private String comment;
	@Getter
	private String modid;
	
	public boolean isUpToDate(String modVersion, String displayVersion) {
		if(modVersion.equals("%VERSION%") || displayVersion.equals("%VERSION%")) {
			return false;
		}
		
		modVersion = Utils.patchVersion(modVersion);
		displayVersion = Utils.patchVersion(displayVersion);
		
		Pattern versionPattern = Pattern.compile(Utils.allVersionRegex);
		Matcher matcher;
		String actualModVersion;
		boolean modVersionValid;
		if(modVersion.equals(displayVersion)) {
			actualModVersion = modVersion;
			matcher = versionPattern.matcher(actualModVersion);
			modVersionValid = matcher.matches();
		} else {
			matcher = versionPattern.matcher(modVersion);
			modVersionValid = matcher.matches();
			matcher = versionPattern.matcher(displayVersion);
			boolean displayModVersionValid = matcher.matches();
			if(modVersionValid && displayModVersionValid) {
				// Take newer or more detailed version
				actualModVersion = (Utils.isNewer(modVersion, displayVersion) ? modVersion : displayVersion);
			} else if(modVersionValid) {
				actualModVersion = modVersion;
			} else if(displayModVersionValid) {
				actualModVersion = displayVersion;
				modVersionValid = true;
			} else {
				actualModVersion = modVersion + "/" + displayVersion;
			}
		}
		
		if(modVersionValid) {
			return isUpToDate(actualModVersion);
		}
		return false;
	}
	
	public boolean isUpToDate(String modVersion) {
		Pattern versionPattern = Pattern.compile(Utils.allVersionRegex);
		Matcher matcher;
		matcher = versionPattern.matcher(modVersion);
		boolean modVersionValid = matcher.matches();
		if(modVersionValid) {
			return !Utils.isNewer(version, modVersion);
		}
		return false;
	}
}
