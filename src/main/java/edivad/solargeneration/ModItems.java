package edivad.solargeneration;

import edivad.solargeneration.items.LapisShard;
import edivad.solargeneration.items.PhotovoltaicCell;
import edivad.solargeneration.items.SolarCore;
import edivad.solargeneration.items.SolarHelmet;
import edivad.solargeneration.items.Wrench;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModItems {

	// solarCore
	@ObjectHolder(Main.MODID + ":solar_core_advanced")
	public static SolarCore solarCoreAdvanced;
	@ObjectHolder(Main.MODID + ":solar_core_hardened")
	public static SolarCore solarCoreHardened;
	@ObjectHolder(Main.MODID + ":solar_core_leadstone")
	public static SolarCore solarCoreLeadstone;
	@ObjectHolder(Main.MODID + ":solar_core_redstone")
	public static SolarCore solarCoreRedstone;
	@ObjectHolder(Main.MODID + ":solar_core_resonant")
	public static SolarCore solarCoreResonant;
	@ObjectHolder(Main.MODID + ":solar_core_signalum")
	public static SolarCore solarCoreSignalum;
	@ObjectHolder(Main.MODID + ":solar_core_ultimate")
	public static SolarCore solarCoreUltimate;

	// solarHelmet
	@ObjectHolder(Main.MODID + ":solar_helmet_advanced")
	public static SolarHelmet solarHelmetAdvanced;
	@ObjectHolder(Main.MODID + ":solar_helmet_hardened")
	public static SolarHelmet solarHelmetHardened;
	@ObjectHolder(Main.MODID + ":solar_helmet_leadstone")
	public static SolarHelmet solarHelmetLeadstone;
	@ObjectHolder(Main.MODID + ":solar_helmet_redstone")
	public static SolarHelmet solarHelmetRedstone;
	@ObjectHolder(Main.MODID + ":solar_helmet_resonant")
	public static SolarHelmet solarHelmetResonant;
	@ObjectHolder(Main.MODID + ":solar_helmet_signalum")
	public static SolarHelmet solarHelmetSignalum;
	@ObjectHolder(Main.MODID + ":solar_helmet_ultimate")
	public static SolarHelmet solarHelmetUltimate;

	// Other Items
	@ObjectHolder(Main.MODID + ":lapis_shard")
	public static LapisShard lapisShard;
	@ObjectHolder(Main.MODID + ":photovoltaic_cell")
	public static PhotovoltaicCell photovoltaicCell;
	@ObjectHolder(Main.MODID + ":wrench")
	public static Wrench wrench;

	public static void register(IForgeRegistry<Item> registry)
	{
		// solarCore
		for(SolarPanelLevel level : SolarPanelLevel.values())
			registry.register(new SolarCore(level));

		// solarHelmet
		for(SolarPanelLevel level : SolarPanelLevel.values())
			registry.register(new SolarHelmet(level));

		// Other Items
		registry.register(new LapisShard());
		registry.register(new PhotovoltaicCell());
		registry.register(new Wrench());

		// Blocks
		Item.Properties property = new Item.Properties().group(Main.solarGenerationTab);
		registry.register(new BlockItem(ModBlocks.solarPanelLeadstone, property).setRegistryName(SolarPanelLevel.Leadstone.getBlockResourceLocation()));
		registry.register(new BlockItem(ModBlocks.solarPanelHardened, property).setRegistryName(SolarPanelLevel.Hardened.getBlockResourceLocation()));
		registry.register(new BlockItem(ModBlocks.solarPanelRedstone, property).setRegistryName(SolarPanelLevel.Redstone.getBlockResourceLocation()));
		registry.register(new BlockItem(ModBlocks.solarPanelResonant, property).setRegistryName(SolarPanelLevel.Resonant.getBlockResourceLocation()));
		registry.register(new BlockItem(ModBlocks.solarPanelSignalum, property).setRegistryName(SolarPanelLevel.Signalum.getBlockResourceLocation()));
		registry.register(new BlockItem(ModBlocks.solarPanelAdvanced, property).setRegistryName(SolarPanelLevel.Advanced.getBlockResourceLocation()));
		registry.register(new BlockItem(ModBlocks.solarPanelUltimate, property).setRegistryName(SolarPanelLevel.Ultimate.getBlockResourceLocation()));
	}
}
