package com.worldcretornica.ichatplayerlist;

import me.javoris767.supachat.SupaChat;
import net.TheDgtl.iChat.iChat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLoginListener implements Listener
{

	public static iChatPlayerList plugin;
	
	public PlayerLoginListener(iChatPlayerList instance)
	{
		plugin = instance;
	}
	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChatSetTab(final AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		
		if(event.isAsynchronous())
			plugin.getServer().getScheduler().callSyncMethod(plugin, new CallableAddPlayerToList(player, plugin));
		else
			plugin.addPlayerToList(player);
	}
	
	
	@EventHandler
	public void onPlayerChangedWorld(final PlayerChangedWorldEvent event)
	{
		plugin.addPlayerToList(event.getPlayer());
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		if(plugin.ColorNamesInChat)
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{
				if(event.getPlayer().canSee(p))
				{
					String Prefix = "";
					String Suffix = "";
					int lastcolor = 0;
					String LastColorPrefix = "";
					String LastColorSuffix = "";
					
					if (plugin.ichatplugin != null)
					{
						((iChat) plugin.ichatplugin).info.addPlayer(p);
						
						if(((iChat) plugin.ichatplugin).info.getKey(p, "prefix") == null)
						{
							return;
						}else{
							Prefix = ((iChat) plugin.ichatplugin).info.getKey(p, "prefix");
							Suffix = ((iChat) plugin.ichatplugin).info.getKey(event.getPlayer(), "suffix");
						}
					}else if (plugin.supachatplugin != null)
					{
						((SupaChat) plugin.supachatplugin).info.addPlayer(p);
						
						if(((SupaChat) plugin.supachatplugin).info.getKey(p, "prefix") == null)
						{
							return;
						}else{
							Prefix = ((SupaChat) plugin.supachatplugin).info.getKey(p, "prefix");
							Suffix = ((SupaChat) plugin.supachatplugin).info.getKey(event.getPlayer(), "suffix");
						}
					}else if (plugin.PEXManager != null)
					{
						Prefix = plugin.PEXManager.getUser(p).getPrefix();
						Suffix = plugin.PEXManager.getUser(event.getPlayer()).getSuffix();
					}

					
					lastcolor = Prefix.lastIndexOf("&");
					if(lastcolor >= 0)
					{
						LastColorPrefix = "&" + Prefix.charAt(lastcolor + 1);
					}
					
					lastcolor = Suffix.lastIndexOf("&");
					if(lastcolor >= 0)
					{
						LastColorSuffix = "&" + Suffix.charAt(lastcolor + 1);
					}
					else
					{
						LastColorSuffix = "&f";
					}
					
					event.setMessage(event.getMessage().replaceAll("(?i)" + p.getName(), plugin.addColor(LastColorPrefix + p.getName() + LastColorSuffix)));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		plugin.addPlayerToList(player);
	}	
}
