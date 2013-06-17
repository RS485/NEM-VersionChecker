package rs485.nem.versionchecker;

import java.util.logging.Logger;

import net.minecraft.crash.CallableMinecraftVersion;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(
		modid = "NEM-VersionChecker",
		name = "NEM Version Checker",
		/* %------------CERTIFICATE-SUM-----------% */
		version = "%VERSION%")
public class NEMVersionChecker {
	
	@Getter
	@Instance("NEM-VersionChecker")
	private static NEMVersionChecker instance;
	
	@Getter
	private Logger log;
	
	@Getter
	@Setter(value=AccessLevel.PROTECTED)
	private NEMModInfo[] modInformation = null;
	
	@Getter
	private boolean disabled = false;
	
	private NEMEventListener listener;
	
	public String getMCVersion() {
		return new CallableMinecraftVersion(null).minecraftVersion();
	}
	
	public void disable() {
		disabled = true;
	}
	
	@PreInit
	public void LoadConfig(FMLPreInitializationEvent evt) throws NoSuchFieldException, SecurityException {
		log = evt.getModLog();
		listener = new NEMEventListener();
		TickRegistry.registerTickHandler(listener, Side.CLIENT);
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		new NEMVersionDownloader(getMCVersion());
	}
}
