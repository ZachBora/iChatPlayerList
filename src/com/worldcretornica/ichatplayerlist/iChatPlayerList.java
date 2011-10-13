package com.worldcretornica.ichatplayerlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import net.TheDgtl.iChat.iChat;
import net.TheDgtl.iChat.iChatAPI;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
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
	
	public YamlConfiguration mainconfig;
	
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
			
			loadConfig();
			
			PluginManager pm = getServer().getPluginManager();
			pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerloginlistener, Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_CHAT, this.playerloginlistener, Event.Priority.High, this);
			
			this.logger.info(pdfdescription + " version " + pdfversion + " iChat " + ichatplugin.getDescription().getVersion() + " detected");
			this.logger.info(pdfdescription + " version " + pdfversion + " is enabled");
		}else{
			this.logger.severe(pdfdescription + " version " + pdfversion + " iChat not found!");
			this.setEnabled(false);
		}
	}
	
	
	private void loadConfig()
	{
		mainconfig = new YamlConfiguration();
		
		File file = new File(this.getDataFolder(), "config.yml");
		
		if(file.exists())
		{
			try {
				mainconfig.load(file);
			} catch (FileNotFoundException e) {
				logger.severe("[" + pdfdescription + "] File not found: " + e.getMessage());
			} catch (IOException e) {
				logger.severe("[" + pdfdescription + "] IO Error: " + e.getMessage());
			} catch (InvalidConfigurationException e) {
				logger.severe("[" + pdfdescription + "] Invalid configuration: " + e.getMessage());
			} catch (NullPointerException e){
				logger.severe("[" + pdfdescription + "] Null pointer: ");
			}
			
			if (!mainconfig.isBoolean("ShowStatusOP"))
				mainconfig.set("ShowStatusOP", true);
			if (!mainconfig.isBoolean("ShowStatusCreative"))
				mainconfig.set("ShowStatusCreative", true);
			if (!mainconfig.isBoolean("ShowInTAB"))
				mainconfig.set("ShowInTAB", true);
			if (!mainconfig.isBoolean("ShowInDisplayName"))
				mainconfig.set("ShowInDisplayName", true);
			if (!mainconfig.isBoolean("ShowBothStatus"))
				mainconfig.set("ShowBothStatus", false);
			if (!mainconfig.isString("OPSymbol"))
				mainconfig.set("OPSymbol", "*");
			if (!mainconfig.isString("CreativeSymbol"))
				mainconfig.set("CreativeSymbol", "~");
		}else{
			mainconfig.set("ShowStatusOP", true);
			mainconfig.set("ShowStatusCreative", true);
			mainconfig.set("ShowInTAB", true);
			mainconfig.set("ShowInDisplayName", true);
			mainconfig.set("ShowBothStatus", false);
			mainconfig.set("OPSymbol", "*");
			mainconfig.set("CreativeSymbol", "~");
		}

		try {
			mainconfig.save(file);
		} catch (IOException e) {
			logger.severe("[" + pdfdescription + "] IO Error: " + e.getMessage());
		}
	}

}
