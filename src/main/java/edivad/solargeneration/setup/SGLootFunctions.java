package edivad.solargeneration.setup;

import edivad.solargeneration.Main;
import edivad.solargeneration.lootable.SolarPanelLootFunction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class SGLootFunctions {

    private static LootItemFunctionType solarPanel;

    public static void register() {
        solarPanel = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Main.MODID, "solar_panel"), new LootItemFunctionType(new SolarPanelLootFunction.Serializer()));
    }

    public static LootItemFunctionType getSolarPanel() {
        return solarPanel;
    }
}
