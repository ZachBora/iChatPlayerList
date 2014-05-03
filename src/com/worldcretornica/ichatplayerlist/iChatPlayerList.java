package com.worldcretornica.ichatplayerlist;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.TheDgtl.iChat.iChat;
import me.javoris767.supachat.SupaChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.VanishPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

@SuppressWarnings("unused")
public class iChatPlayerList extends JavaPlugin {

	public String pdfdescription;
	private String pdfversion;
		
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public Plugin ichatplugin;
	public Plugin supachatplugin;
	public Plugin PEX;
	public Plugin VanishNoPacketplugin;
	
	//public iChatAPI ichatapi;
	//public SupaChatAPI supachatapi;
	public PermissionManager PEXManager;
	
	public boolean ColorNamesInChat;
	
	@Override
	public void onDisable() 
	{
		this.logger.info(pdfdescription + " version " + pdfversion + " disabled");
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		pdfdescription = pdfFile.getName();
		pdfversion = pdfFile.getVersion();

		PluginManager pm = getServer().getPluginManager();
		
		ichatplugin = pm.getPlugin("iChat");
		supachatplugin = pm.getPlugin("SupaChat");
		PEX = pm.getPlugin("PermissionsEx");
		VanishNoPacketplugin = pm.getPlugin("VanishNoPacket");
		
		if(PEX == null && ichatplugin == null && supachatplugin == null)
		{
			this.logger.severe(pdfdescription + " version " + pdfversion + " PEX, iChat and SupaChat not found!");
			this.setEnabled(false);
			return;
		}
		
		this.getConfig().options().copyDefaults(true);
        saveConfig();
		

		if(pm.getPlugin("TagAPI") != null)
		{
			pm.registerEvents(new TagAPIListener(this), this);
		}
		
		if(VanishNoPacketplugin != null)
		{
			pm.registerEvents(new VanishAPIListener(this), this);
		}
		
		pm.registerEvents(new PlayerLoginListener(this), this);
				
		if(ichatplugin != null)
		{
			//ichatapi = ((iChat) ichatplugin).API;
			
			this.logger.info(pdfdescription + " version " + pdfversion + " iChat " + ichatplugin.getDescription().getVersion() + " detected");
		}else if(supachatplugin != null)
		{
			//supachatapi = ((SupaChat) supachatplugin).API;
			
			this.logger.info(pdfdescription + " version " + pdfversion + " SupaChat " + supachatplugin.getDescription().getVersion() + " detected");
		}else if(PEX != null)
		{
			PEXManager = PermissionsEx.getPermissionManager();
			
			this.logger.info(pdfdescription + " version " + pdfversion + " PEX " + PEX.getDescription().getVersion() + " detected");
		}

		ColorNamesInChat = getConfig().getBoolean("ColorNamesInChat");
		
		this.logger.info(pdfdescription + " version " + pdfversion + " is enabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args)
	{
		if (label.equalsIgnoreCase("ichatplayerlist"))
		{
			if(args.length == 1 && args[0].toString().equalsIgnoreCase("reload") && 
					(!(sender instanceof Player) || checkPermissions((Player) sender, "ichatplayerlist")))
			{
				this.reloadConfig();
				ColorNamesInChat = getConfig().getBoolean("ColorNamesInChat");
				
				for(Player p : getServer().getOnlinePlayers())
				{
					addPlayerToList(p);
				}
				
				sender.sendMessage(ChatColor.BLUE + this.getName() + " - Configuration reload successfully");
				return true;
			}
			return false;
		}
		return false;
	}
	
	
	public String addColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
	
	
	public boolean checkPermissions(Player player, String node) {
		if (player.hasPermission(node) || player.hasPermission("ichatplayerlist.*") || player.hasPermission("*")) {
			return true;
		} else if (player.isOp()) {
			return true;
		}
		return false;
	}
	
	public void addPlayerToList(Player player)
	{
		String Prefix = "";
		String Suffix = "";
		String LastColorPrefix = "";
		String FirstColorPrefix = "";
		String LastColorSuffix = "";
		String FirstColorSuffix = "";
		FileConfiguration config = getConfig();
		String OP = config.getString("OPSymbol");
		String Creative = config.getString("CreativeSymbol");
		String Survival = config.getString("SurvivalSymbol");
		String Vanish = config.getString("VanishedSymbol");
		String TabFormat = config.getString("TabFormat");
		String DisplayNameFormat = config.getString("DisplayNameFormat");
		String TooLongTabNameSymbol = config.getString("TooLongTabNameSymbol");
		int lastcolor = 0;
		int firstcolor = 0;
		
		//Get prefix and suffix
		if (ichatplugin != null)
		{
			((iChat) ichatplugin).info.addPlayer(player);
			
			if(((iChat) ichatplugin).info.getKey(player, "prefix") == null)
			{
				logger.severe("Unable to get prefix");
				return;
			}else{
				Prefix = ((iChat) ichatplugin).info.getKey(player, "prefix");
				Suffix = ((iChat) ichatplugin).info.getKey(player, "suffix");
			}
		}else if (supachatplugin != null)
		{
			((SupaChat) supachatplugin).info.addPlayer(player);
			
			if(((SupaChat) supachatplugin).info.getKey(player, "prefix") == null)
			{
				logger.severe("Unable to get prefix");
				return;
			}else{
				Prefix = ((SupaChat) supachatplugin).info.getKey(player, "prefix");
				Suffix = ((SupaChat) supachatplugin).info.getKey(player, "suffix");
			}
		}else if (PEXManager != null)
		{
			Prefix = PEXManager.getUser(player).getPrefix();
			Suffix = PEXManager.getUser(player).getSuffix();
		}
		
		//Get last prefix color
		lastcolor = Prefix.lastIndexOf("&");
		if(lastcolor >= 0)
		{
			LastColorPrefix = "&" + Prefix.charAt(lastcolor + 1);
		}
		//Get first prefix color
		firstcolor = Prefix.indexOf("&");
		if(firstcolor >= 0)
		{
			FirstColorPrefix = "&" + Prefix.charAt(firstcolor + 1);
		}
		//Get last suffix color
		lastcolor = Suffix.lastIndexOf("&");
		if(lastcolor >= 0)
		{
			LastColorSuffix = "&" + Suffix.charAt(lastcolor + 1);
		}
		//Get first suffix color
		firstcolor = Suffix.indexOf("&");
		if(firstcolor >= 0)
		{
			FirstColorSuffix = "&" + Suffix.charAt(firstcolor + 1);
		}
		
		//Color displayname?
		if (config.getBoolean("ShowInDisplayName"))
		{
			if(player.isOp())
			{
				DisplayNameFormat = DisplayNameFormat.replace("$OP", OP);
				if(!config.getBoolean("ShowBothStatus"))
				{
					DisplayNameFormat = DisplayNameFormat.replace("$CREATIVE", "");
					DisplayNameFormat = DisplayNameFormat.replace("$SURVIVAL", "");
				}
			}else{
				DisplayNameFormat = DisplayNameFormat.replace("$OP", "");
			}
			if(player.getGameMode() == GameMode.CREATIVE)
			{
				DisplayNameFormat = DisplayNameFormat.replace("$CREATIVE", Creative);
			}else{
				DisplayNameFormat = DisplayNameFormat.replace("$CREATIVE", "");
			}
			if(player.getGameMode() == GameMode.SURVIVAL)
			{
				DisplayNameFormat = DisplayNameFormat.replace("$SURVIVAL", Survival);
			}else{
				DisplayNameFormat = DisplayNameFormat.replace("$SURVIVAL", "");
			}
			
			if(VanishNoPacketplugin != null && ((VanishPlugin) VanishNoPacketplugin).getManager().isVanished(player))
			{
				DisplayNameFormat = DisplayNameFormat.replace("$HIDDEN", Vanish);
			}
			else
			{
				DisplayNameFormat = DisplayNameFormat.replace("$HIDDEN", "");
			}
			
			DisplayNameFormat = DisplayNameFormat.replace("$LASTCOLORPREFIX", LastColorPrefix);
			DisplayNameFormat = DisplayNameFormat.replace("$LASTCOLORSUFFIX", LastColorSuffix);
			DisplayNameFormat = DisplayNameFormat.replace("$FIRSTCOLORPREFIX", FirstColorPrefix);
			DisplayNameFormat = DisplayNameFormat.replace("$FIRSTCOLORSUFFIX", FirstColorSuffix);
			DisplayNameFormat = DisplayNameFormat.replace("$PREFIX", Prefix);
			DisplayNameFormat = DisplayNameFormat.replace("$SUFFIX", Suffix);
			DisplayNameFormat = DisplayNameFormat.replace("$NAME", player.getName());
			
			DisplayNameFormat = addColor(DisplayNameFormat);
			player.setDisplayName(DisplayNameFormat);
		}
		
		//Color tab?
		if (config.getBoolean("ShowInTAB"))
		{
			if(player.isOp())
			{
				TabFormat = TabFormat.replace("$OP", OP);
				if(!config.getBoolean("ShowBothStatus"))
				{
					TabFormat = TabFormat.replace("$CREATIVE", "");
					TabFormat = TabFormat.replace("$SURVIVAL", "");
				}
			}else{
				TabFormat = TabFormat.replace("$OP", "");
			}
			if(player.getGameMode() == GameMode.CREATIVE)
			{
				TabFormat = TabFormat.replace("$CREATIVE", Creative);
			}else{
				TabFormat = TabFormat.replace("$CREATIVE", "");
			}
			if(player.getGameMode() == GameMode.SURVIVAL)
			{
				TabFormat = TabFormat.replace("$SURVIVAL", Survival);
			}else{
				TabFormat = TabFormat.replace("$SURVIVAL", "");
			}
			
			if(VanishNoPacketplugin != null && ((VanishPlugin) VanishNoPacketplugin).getManager().isVanished(player))
			{
				TabFormat = TabFormat.replace("$HIDDEN", Vanish);
			}
			else
			{
				TabFormat = TabFormat.replace("$HIDDEN", "");
			}
			
			TabFormat = TabFormat.replace("$LASTCOLORPREFIX", LastColorPrefix);
			TabFormat = TabFormat.replace("$LASTCOLORSUFFIX", LastColorSuffix);
			TabFormat = TabFormat.replace("$FIRSTCOLORPREFIX", FirstColorPrefix);
			TabFormat = TabFormat.replace("$FIRSTCOLORSUFFIX", FirstColorSuffix);
			TabFormat = TabFormat.replace("$PREFIX", Prefix);
			TabFormat = TabFormat.replace("$SUFFIX", Suffix);
			TabFormat = TabFormat.replace("$NAME", player.getName());
			TabFormat = TabFormat.replace("$DISPLAYNAME", player.getDisplayName());
			
			TabFormat = addColor(TabFormat);
			if (TabFormat.length()>16)
			{
				if (!TooLongTabNameSymbol.equals(""))
				{
					player.setPlayerListName(TabFormat.substring(0, 16 - TooLongTabNameSymbol.length()) + TooLongTabNameSymbol);
				}else{
					player.setPlayerListName(TabFormat.substring(0, 16));
				}
			}
			else{
				player.setPlayerListName(TabFormat);
			}
		}
	}
	
	public String getPlayerOverhead(Player player)
	{
		String Prefix = "";
		String Suffix = "";
		String LastColorPrefix = "";
		String FirstColorPrefix = "";
		String LastColorSuffix = "";
		String FirstColorSuffix = "";
		FileConfiguration config = getConfig();
		String OP = config.getString("OPSymbol");
		String Creative = config.getString("CreativeSymbol");
		String Survival = config.getString("SurvivalSymbol");
		String Vanish = config.getString("VanishedSymbol");
		//String TabFormat = getConfig().getString("TabFormat");
		//String DisplayNameFormat = getConfig().getString("DisplayNameFormat");
		//String TooLongTabNameSymbol = getConfig().getString("TooLongTabNameSymbol");
		String OverheadFormat = config.getString("OverheadFormat");
		int lastcolor = 0;
		int firstcolor = 0;
		
		//Get prefix and suffix
		if (ichatplugin != null)
		{
			((iChat) ichatplugin).info.addPlayer(player);
			
			if(((iChat) ichatplugin).info.getKey(player, "prefix") == null)
			{
				logger.severe("Unable to get prefix");
				return "";
			}else{
				Prefix = ((iChat) ichatplugin).info.getKey(player, "prefix");
				Suffix = ((iChat) ichatplugin).info.getKey(player, "suffix");
			}
		}else if (supachatplugin != null)
		{
			((SupaChat) supachatplugin).info.addPlayer(player);
			
			if(((SupaChat) supachatplugin).info.getKey(player, "prefix") == null)
			{
				logger.severe("Unable to get prefix");
				return "";
			}else{
				Prefix = ((SupaChat) supachatplugin).info.getKey(player, "prefix");
				Suffix = ((SupaChat) supachatplugin).info.getKey(player, "suffix");
			}
		}else if (PEXManager != null)
		{
			Prefix = PEXManager.getUser(player).getPrefix();
			Suffix = PEXManager.getUser(player).getSuffix();
		}
		
		//Get last prefix color
		lastcolor = Prefix.lastIndexOf("&");
		if(lastcolor >= 0)
		{
			LastColorPrefix = "&" + Prefix.charAt(lastcolor + 1);
		}
		//Get first prefix color
		firstcolor = Prefix.indexOf("&");
		if(firstcolor >= 0)
		{
			FirstColorPrefix = "&" + Prefix.charAt(firstcolor + 1);
		}
		//Get last suffix color
		lastcolor = Suffix.lastIndexOf("&");
		if(lastcolor >= 0)
		{
			LastColorSuffix = "&" + Suffix.charAt(lastcolor + 1);
		}
		//Get first suffix color
		firstcolor = Suffix.indexOf("&");
		if(firstcolor >= 0)
		{
			FirstColorSuffix = "&" + Suffix.charAt(firstcolor + 1);
		}
		
		//Color Overhead?
		if (config.getBoolean("ShowInOverhead"))
		{
			if(player.isOp())
			{
				OverheadFormat = OverheadFormat.replace("$OP", OP);
				if(!config.getBoolean("ShowBothStatus"))
				{
					OverheadFormat = OverheadFormat.replace("$CREATIVE", "");
					OverheadFormat = OverheadFormat.replace("$SURVIVAL", "");
				}
			}else{
				OverheadFormat = OverheadFormat.replace("$OP", "");
			}
			if(player.getGameMode() == GameMode.CREATIVE)
			{
				OverheadFormat = OverheadFormat.replace("$CREATIVE", Creative);
			}else{
				OverheadFormat = OverheadFormat.replace("$CREATIVE", "");
			}
			if(player.getGameMode() == GameMode.SURVIVAL)
			{
				OverheadFormat = OverheadFormat.replace("$SURVIVAL", Survival);
			}else{
				OverheadFormat = OverheadFormat.replace("$SURVIVAL", "");
			}
			
			if(VanishNoPacketplugin != null && ((VanishPlugin) VanishNoPacketplugin).getManager().isVanished(player))
			{
				OverheadFormat = OverheadFormat.replace("$HIDDEN", Vanish);
			}
			else
			{
				OverheadFormat = OverheadFormat.replace("$HIDDEN", "");
			}
			
			OverheadFormat = OverheadFormat.replace("$LASTCOLORPREFIX", LastColorPrefix);
			OverheadFormat = OverheadFormat.replace("$LASTCOLORSUFFIX", LastColorSuffix);
			OverheadFormat = OverheadFormat.replace("$FIRSTCOLORPREFIX", FirstColorPrefix);
			OverheadFormat = OverheadFormat.replace("$FIRSTCOLORSUFFIX", FirstColorSuffix);
			OverheadFormat = OverheadFormat.replace("$PREFIX", Prefix);
			OverheadFormat = OverheadFormat.replace("$SUFFIX", Suffix);
			OverheadFormat = OverheadFormat.replace("$NAME", player.getName());
			
			OverheadFormat = addColor(OverheadFormat);
			
			if (OverheadFormat.length()>16)
			{
				OverheadFormat = OverheadFormat.substring(0, 16);
			}
		}
				
		return OverheadFormat;
	}
	
	/*
	public void updateTabListByRank()
	{
	    SortedMap<Integer, SortedSet<String>> playerranks = new TreeMap<>();
	    boolean found = false;
	    
	    for(Player player : Bukkit.getOnlinePlayers())
	    {
	        found = false;
	        for(int i = 0;i<100 && !found;i++)
	        {
	            if(player.hasPermission("rank." + i))
	            {
	                if(!playerranks.containsKey(i))
	                {
	                    playerranks.put(i, new TreeSet<String>());
	                }
	                playerranks.get(i).add(player.getName());
	                found = true;
	            }
	        }
	        
	        if(!found)
	        {
	            if(!playerranks.containsKey(9999))
                {
                    playerranks.put(9999, new TreeSet<String>());
                }
	            playerranks.get(9999).add(player.getName());
	        }
	    }
	    
	    int maxcol = TabAPI.getHorizSize()-1;
	    int maxline = TabAPI.getVertSize()-1;
	    int currCol;
	    int currRow;
	    	 
	    logger.info("New tab list");
	    
	    for(Player player : Bukkit.getOnlinePlayers())
	    {
	        currCol = -1;
	        currRow = 0;
    	    for(Integer rank : playerranks.keySet())
    	    {
    	        logger.info("Rank: " + rank);
    	        
    	        for(String name : playerranks.get(rank))
    	        {
    	            Player cplayer = Bukkit.getPlayer(name);
    	            
    	            if(player.canSee(cplayer))
    	            {
        	            currCol++;
                        if(currCol>maxcol) 
                        {
                            currCol = 0;
                            currRow++;
                        }
                        
        	            if(currRow<=maxline)
        	            {
        	                TabAPI.setTabString(this, player, currRow, currCol, cplayer.getPlayerListName());
        	                logger.info("Player : " + cplayer.getName() + " " + currRow + ":" + currCol);
        	            }
    	            }
    	        }
    	    }
    	    
	    }
	    TabAPI.updateAll();
	}*/

}
