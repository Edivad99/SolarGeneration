package com.edivad.solargeneration.proxy;

import com.edivad.solargeneration.Main;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHandler {

	public static final EventHandler INSTANCE = new EventHandler();

	@SubscribeEvent
	public void handlePlayerLoggedInEvent(PlayerLoggedInEvent event)
	{

		CheckResult result = ForgeVersion.getResult(Loader.instance().activeModContainer());
		if(!(result.status.equals(Status.UP_TO_DATE) || result.status.equals(Status.PENDING)))
		{
			event.player.sendMessage(new TextComponentString(TextFormatting.GREEN + "[" + Main.MODNAME + "] " + TextFormatting.WHITE + "A new version is available (" + result.target + "), please update!"));
			event.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Changelog:"));
			
			String changes = result.changes.get(result.target);
			String changesFormat[] = changes.split("\n");
			
			for (String change : changesFormat)
			{
				event.player.sendMessage(new TextComponentString(TextFormatting.WHITE + "- " + change));
			}
		}
	}

}
