package com.worldcretornica.ichatplayerlist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

public class TagAPIListener implements Listener
{
	public static iChatPlayerList plugin;
	
	TagAPIListener(iChatPlayerList instance)
	{
		plugin = instance;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onNameTag(final AsyncPlayerReceiveNameTagEvent event)
	{
		String overhead = plugin.getPlayerOverhead(event.getNamedPlayer());
		
		if(overhead != "")
		{
			event.setTag(overhead);
		}
	}
}
