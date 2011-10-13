package com.worldcretornica.ichatplayerlist;

import net.TheDgtl.iChat.iChat;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerLoginListener extends PlayerListener {

	public static iChatPlayerList plugin;
	
	public PlayerLoginListener(iChatPlayerList instance)
	{
		plugin = instance;
	}
	
	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		
		Player player = event.getPlayer();
		addPlayerToList(player);				
		
		super.onPlayerChat(event);
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		addPlayerToList(player);
		
		super.onPlayerJoin(event);
	}
	
	
	public void addPlayerToList(Player player)
	{
		((iChat) plugin.ichatplugin).info.addPlayer(player);
		
		if(((iChat) plugin.ichatplugin).info.getKey(player, "prefix") == null)
		{
			plugin.logger.severe("Unable to get prefix");
		}else{
			String prefix = ((iChat) plugin.ichatplugin).info.getKey(player, "prefix");
			
			if(prefix.lastIndexOf("&") != -1)
			{
				int lastcolor = prefix.lastIndexOf("&");
				
				String coloredname = "&" + prefix.charAt(lastcolor + 1) + player.getName();
				String tabname = coloredname;
				
				if(plugin.mainconfig.getBoolean("ShowInTAB"))
				{
					tabname = checkOP(tabname, player);
					tabname = ((iChat) plugin.ichatplugin).API.addColor(tabname);
					if (tabname.length()>16)
						player.setPlayerListName(tabname.substring(0, 14) + "..");
					else
						player.setPlayerListName(tabname);
				}
				if(plugin.mainconfig.getBoolean("ShowInDisplayName"))
				{
					coloredname = checkOP(coloredname, player);
					coloredname = ((iChat) plugin.ichatplugin).API.addColor(coloredname);
					player.setDisplayName(coloredname);
				}				
			}
		}
	}
	
	public String checkOP(String name, Player player)
	{
		if(plugin.mainconfig.getBoolean("ShowStatusOP"))
		{
			if(player.isOp())
			{
				name = plugin.mainconfig.getString("OPSymbol") + name;
				
				if(plugin.mainconfig.getBoolean("ShowBothStatus"))
				{
					name = checkCreative(name, player);
				}
			}else{
				name = checkCreative(name, player);
			}
			
			return name;
		}else{
			return checkCreative(name, player);
		}
	}
	
	public String checkCreative(String name, Player player)
	{
		if(plugin.mainconfig.getBoolean("ShowStatusCreative"))
		{
			if(player.getGameMode() == GameMode.CREATIVE)
			{
				name = plugin.mainconfig.getString("CreativeSymbol") + name;
			}
			
			return name;
		}else{
			return name;
		}
	}
	
}
