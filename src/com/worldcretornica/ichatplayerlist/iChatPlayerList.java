package com.worldcretornica.ichatplayerlist;

import java.util.logging.Logger;

import net.TheDgtl.iChat.iChat;
import net.TheDgtl.iChat.iChatAPI;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class iChatPlayerList extends JavaPlugin {

	public String pdfdescription;
	private String pdfversion;
	
	public final PlayerLoginListener playerloginlistener = new PlayerLoginListener(this);
	
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public Plugin ichatplugin;
	
	public iChatAPI ichatapi;
	
	@Override
	public void onDisable() {
		this.logger.info(pdfdescription + " version " + pdfversion + " disabled");
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		pdfdescription = pdfFile.getName();
		pdfversion = pdfFile.getVersion();

		ichatplugin = this.getServer().getPluginManager().getPlugin("iChat");
		
		if(ichatplugin != null)
		{
			ichatapi = ((iChat) ichatplugin).API;
			
			PluginManager pm = getServer().getPluginManager();
			pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerloginlistener, Event.Priority.Normal, this);	
			
			this.logger.info(pdfdescription + " version " + pdfversion + " iChat " + ichatplugin.getDescription().getVersion() + " detected");
			this.logger.info(pdfdescription + " version " + pdfversion + " is enabled");
		}else{
			this.logger.severe(pdfdescription + " version " + pdfversion + " iChat not found!");
			this.setEnabled(false);
		}
	}

}
