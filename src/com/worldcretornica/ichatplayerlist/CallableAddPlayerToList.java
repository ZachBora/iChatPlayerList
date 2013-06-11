package com.worldcretornica.ichatplayerlist;

import java.util.concurrent.Callable;

import org.bukkit.entity.Player;

public class CallableAddPlayerToList implements Callable<Object>
{

	private Player player;
	private iChatPlayerList plugin;
	
	CallableAddPlayerToList(Player p, iChatPlayerList pl)
	{
		player = p;
		plugin = pl;
	}
	
	@Override
	public Object call() throws Exception {
		plugin.addPlayerToList(player);
		return null;
	}

}
