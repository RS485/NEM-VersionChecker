package rs485.nem.versionchecker;

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
	
	public boolean isUpToDate(String currentVersion) {
		return currentVersion.equals(getVersion());
	}
}
