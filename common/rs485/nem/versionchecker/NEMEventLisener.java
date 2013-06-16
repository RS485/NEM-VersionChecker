package rs485.nem.versionchecker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.client.GuiSlotModList;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.TickType;

public class NEMEventLisener implements ITickHandler {

	private Field modList;
	private Field parent;
	private Field mods;
	private Field listWidth;
	private Field buttonList;
	
	private boolean modified = false;
	
	public NEMEventLisener() throws NoSuchFieldException, SecurityException {
		modList = GuiModList.class.getDeclaredField("modList");
		parent = GuiSlotModList.class.getDeclaredField("parent");
		mods = GuiSlotModList.class.getDeclaredField("mods");
		listWidth = GuiScrollingList.class.getDeclaredField("listWidth");
		buttonList = GuiScreen.class.getDeclaredField("buttonList");
		modList.setAccessible(true);
		parent.setAccessible(true);
		mods.setAccessible(true);
		listWidth.setAccessible(true);
		buttonList.setAccessible(true);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.RENDER)) {
			if(FMLClientHandler.instance().getClient().currentScreen.getClass() == GuiModList.class) {
				if(!modified) {
					try {
						GuiSlotModList old = (GuiSlotModList) modList.get(FMLClientHandler.instance().getClient().currentScreen);
						GuiModList one = (GuiModList) parent.get(old);
						ArrayList<ModContainer> two = (ArrayList<ModContainer>) mods.get(old);
						Integer three = (Integer) listWidth.get(old);
						NEMGuiSlotModList newGui = new NEMGuiSlotModList(one, two, three);
						newGui.registerScrollButtons((List) buttonList.get(FMLClientHandler.instance().getClient().currentScreen), 7, 8);
						modList.set(FMLClientHandler.instance().getClient().currentScreen, newGui);
					} catch(IllegalArgumentException e) {
						e.printStackTrace();
					} catch(IllegalAccessException e) {
						e.printStackTrace();
					}
					modified = true;
				}
			} else if(modified) {
				modified = false;
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "NEMTickLisener";
	}
}
