package rs485.nem.versionchecker;

import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.GuiSlotModList;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.LoaderState.ModState;

public class NEMGuiSlotModList extends GuiSlotModList {
	
	private FontRenderer fontRenderer;
	private ArrayList<ModContainer> mods;
    
	public NEMGuiSlotModList(GuiModList parent, ArrayList<ModContainer> mods, int listWidth) {
		super(parent, mods, listWidth);
		this.fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;
		this.mods = mods;
	}
	
	@Override
	protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator var5) {
        ModContainer mc=mods.get(listIndex);
        NEMModInfo[] infos = NEMVersionChecker.getInstance().getModInformation();
        NEMModInfo match = null;
        if(infos != null) {
        	for(NEMModInfo info:infos) {
        		if(info.getModid().equals(mc.getModId())) {
        			match = info;
        			break;
        		}
        	}
        }
        if (Loader.instance().getModState(mc)==ModState.DISABLED) {
            fontRenderer.drawString(fontRenderer.trimStringToWidth(mc.getName(), listWidth - 10), this.left + 3 , var3 + 2, 0xFF2222);
            fontRenderer.drawString(fontRenderer.trimStringToWidth(mc.getDisplayVersion(), listWidth - 10), this.left + 3 , var3 + 12, 0xFF2222);
            fontRenderer.drawString(fontRenderer.trimStringToWidth("DISABLED", listWidth - 10), this.left + 3 , var3 + 22, 0xFF2222);
        } else {
        	String addition = "";
        	String addition2 = "";
        	int colorFirst = 0xFFFFFF;
        	int colorSecond = 0xCCCCCC;
        	if(!mc.getModId().equals("mcp") && !mc.getModId().equals("FML")) {
	        	if(match == null) {
	        		colorFirst = 0xCCCC00;
	        	} else {
	        		if(match.isUpToDate(mc.getVersion()) || match.isUpToDate(mc.getDisplayVersion())) {
	        			colorFirst = 0x00CC00;
	        		} else {
	        			colorFirst = 0xCC0000;
	        			addition = " : " + match.getVersion();
	        			addition2 = " : ";
	        			colorSecond = 0x00CC00;
	        		}
	        	}
        	}
        	fontRenderer.drawString(fontRenderer.trimStringToWidth(mc.getName(), listWidth - 10), this.left + 3 , var3 + 2, colorFirst);
        	fontRenderer.drawString(fontRenderer.trimStringToWidth(mc.getDisplayVersion() + addition , listWidth - 10), this.left + 3 , var3 + 12, colorSecond);
            fontRenderer.drawString(fontRenderer.trimStringToWidth(mc.getDisplayVersion() + addition2, listWidth - 10), this.left + 3 , var3 + 12, 0xCCCCCC);
            fontRenderer.drawString(fontRenderer.trimStringToWidth(mc.getMetadata() !=null ? mc.getMetadata().getChildModCountString() : "Metadata not found", listWidth - 10), this.left + 3 , var3 + 22, 0xCCCCCC);
        }
    }
}
