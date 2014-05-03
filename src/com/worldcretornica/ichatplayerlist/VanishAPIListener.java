package com.worldcretornica.ichatplayerlist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

public class VanishAPIListener implements Listener 
{
	public static iChatPlayerList plugin;
	
	VanishAPIListener(iChatPlayerList instance)
	{
		plugin = instance;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onVanishChange(final VanishStatusChangeEvent event)
	{
		plugin.addPlayerToList(event.getPlayer());
	}
}
