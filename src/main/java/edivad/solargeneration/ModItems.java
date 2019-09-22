package edivad.solargeneration;

import edivad.solargeneration.blocks.SolarPanel;
import edivad.solargeneration.items.LapisShard;
import edivad.solargeneration.items.PhotovoltaicCell;
import edivad.solargeneration.items.SolarCore;
import edivad.solargeneration.items.SolarHelmet;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

	// solarCore
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_core_advanced")
	public static SolarCore solarCoreAdvanced;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_core_hardened")
	public static SolarCore solarCoreHardened;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_core_leadstone")
	public static SolarCore solarCoreLeadstone;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_core_redstone")
	public static SolarCore solarCoreRedstone;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_core_resonant")
	public static SolarCore solarCoreResonant;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_core_signalum")
	public static SolarCore solarCoreSignalum;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_core_ultimate")
	public static SolarCore solarCoreUltimate;

	// solarHelmet
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_helmet_advanced")
	public static SolarHelmet solarHelmetAdvanced;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_helmet_hardened")
	public static SolarHelmet solarHelmetHardened;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_helmet_leadstone")
	public static SolarHelmet solarHelmetLeadstone;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_helmet_redstone")
	public static SolarHelmet solarHelmetRedstone;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_helmet_resonant")
	public static SolarHelmet solarHelmetResonant;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_helmet_signalum")
	public static SolarHelmet solarHelmetSignalum;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_helmet_ultimate")
	public static SolarHelmet solarHelmetUltimate;

	// Other Items
	@GameRegistry.ObjectHolder(Main.MODID + ":lapis_shard")
	public static LapisShard lapisShard;
	@GameRegistry.ObjectHolder(Main.MODID + ":photovoltaic_cell")
	public static PhotovoltaicCell photovoltaicCell;

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		// solarCore
		solarCoreAdvanced.initModel();
		solarCoreHardened.initModel();
		solarCoreLeadstone.initModel();
		solarCoreRedstone.initModel();
		solarCoreResonant.initModel();
		solarCoreSignalum.initModel();
		solarCoreUltimate.initModel();

		// solarHelmet
		solarHelmetAdvanced.initModel();
		solarHelmetHardened.initModel();
		solarHelmetLeadstone.initModel();
		solarHelmetRedstone.initModel();
		solarHelmetResonant.initModel();
		solarHelmetSignalum.initModel();
		solarHelmetUltimate.initModel();

		// Other Items
		lapisShard.initModel();
		photovoltaicCell.initModel();
	}

	public static void register(IForgeRegistry<Item> registry)
	{
		// solarCore
		registry.register(new SolarCore("solar_core_advanced"));
		registry.register(new SolarCore("solar_core_hardened"));
		registry.register(new SolarCore("solar_core_leadstone"));
		registry.register(new SolarCore("solar_core_redstone"));
		registry.register(new SolarCore("solar_core_resonant"));
		registry.register(new SolarCore("solar_core_signalum"));
		registry.register(new SolarCore("solar_core_ultimate"));

		// solarHelmet
		registry.register(new SolarHelmet(SolarPanelLevel.Advanced));
		registry.register(new SolarHelmet(SolarPanelLevel.Hardened));
		registry.register(new SolarHelmet(SolarPanelLevel.Leadstone));
		registry.register(new SolarHelmet(SolarPanelLevel.Redstone));
		registry.register(new SolarHelmet(SolarPanelLevel.Resonant));
		registry.register(new SolarHelmet(SolarPanelLevel.Signalum));
		registry.register(new SolarHelmet(SolarPanelLevel.Ultimate));

		// Other Items
		registry.register(new LapisShard());
		registry.register(new PhotovoltaicCell());

		// Blocks
		registry.register(new ItemBlock(ModBlocks.solarPanelAdvanced).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Advanced)));
		registry.register(new ItemBlock(ModBlocks.solarPanelHardened).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Hardened)));
		registry.register(new ItemBlock(ModBlocks.solarPanelLeadstone).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Leadstone)));
		registry.register(new ItemBlock(ModBlocks.solarPanelRedstone).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Redstone)));
		registry.register(new ItemBlock(ModBlocks.solarPanelResonant).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Resonant)));
		registry.register(new ItemBlock(ModBlocks.solarPanelSignalum).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Signalum)));
		registry.register(new ItemBlock(ModBlocks.solarPanelUltimate).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Ultimate)));
	}
}
