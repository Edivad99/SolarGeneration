package edivad.solargeneration.proxy;

import edivad.solargeneration.Main;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.VersionChecker.CheckResult;
import net.minecraftforge.fml.VersionChecker.Status;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHandler {

	public static final EventHandler INSTANCE = new EventHandler();

	@SubscribeEvent
	public void handlePlayerLoggedInEvent(PlayerLoggedInEvent event)
	{
		CheckResult versionRAW = VersionChecker.getResult(ModList.get().getModFileById(Main.MODID).getMods().get(0));
		Status result = versionRAW.status;

		if(!(result.equals(Status.UP_TO_DATE) || result.equals(Status.PENDING)))
		{
			event.getPlayer().sendMessage(new TextComponentString(TextFormatting.GREEN + "[" + Main.MODNAME + "] " + TextFormatting.WHITE + "A new version is available (" + versionRAW.target + "), please update!"));
			event.getPlayer().sendMessage(new TextComponentString(TextFormatting.YELLOW + "Changelog:"));

			String changes = versionRAW.changes.get(versionRAW.target);
			if(changes != null)
			{
				String changesFormat[] = changes.split("\n");

				for(String change : changesFormat)
				{
					event.getPlayer().sendMessage(new TextComponentString(TextFormatting.WHITE + "- " + change));
				}
			}
		}
	}

}
