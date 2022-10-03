package edivad.solargeneration.datagen;

import edivad.solargeneration.Main;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraftforge.common.Tags;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        supportingItems(consumer);
        solarPanelReverse(consumer);
        solarHelmet(consumer);
        solarPanel(consumer);
        solarCore(consumer);
    }

    private void supportingItems(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(Items.LAPIS_LAZULI)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', Registration.LAPIS_SHARD.get())
                .unlockedBy(getHasName(Registration.LAPIS_SHARD.get()), has(Registration.LAPIS_SHARD.get()))
                .save(consumer, new ResourceLocation(Main.MODID, "lapis_lazuli_from_shard"));

        ShapedRecipeBuilder.shaped(Registration.LAPIS_SHARD.get(), 36)
                .pattern("aa")
                .pattern("aa")
                .define('a', Items.LAPIS_LAZULI)
                .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.PHOTOVOLTAIC_CELL.get())
                .pattern("aaa")
                .pattern("bbb")
                .pattern("ccc")
                .define('a', Items.GLASS_PANE)
                .define('b', Registration.LAPIS_SHARD.get())
                .define('c', Tags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Registration.LAPIS_SHARD.get()), has(Registration.LAPIS_SHARD.get()))
                .save(consumer);
    }

    private void solarPanelReverse(Consumer<FinishedRecipe> consumer) {
        for(var level : SolarPanelLevel.values()) {
            var solarPanel = Registration.SOLAR_PANEL_BLOCK.get(level).get();
            var helmet = Registration.HELMET.get(level).get();
            var resourceLocation = new ResourceLocation(Main.MODID, level.getSolarPanelName() + "_reverse");
            ShapelessRecipeBuilder.shapeless(solarPanel)
                    .requires(helmet)
                    .unlockedBy(getHasName(helmet), has(helmet))
                    .save(consumer, resourceLocation.toString());
        }
    }

    private void solarHelmet(Consumer<FinishedRecipe> consumer) {
        for(var level : SolarPanelLevel.values()) {
            var solarPanel = Registration.SOLAR_PANEL_BLOCK.get(level).get();
            var helmet = Registration.HELMET.get(level).get();
            ShapelessRecipeBuilder.shapeless(helmet)
                    .requires(solarPanel)
                    .requires(getVanillaHelmet(level))
                    .unlockedBy(getHasName(solarPanel), has(solarPanel))
                    .save(consumer);
        }
    }

    private void solarPanel(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(Registration.SOLAR_PANEL_BLOCK.get(SolarPanelLevel.LEADSTONE).get())
                .pattern("aaa")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', Registration.PHOTOVOLTAIC_CELL.get())
                .define('b', Items.REDSTONE)
                .define('c', Registration.CORE.get(SolarPanelLevel.LEADSTONE).get())
                .define('d', ItemTags.create(new ResourceLocation("forge", "nuggets/steel")))
                .unlockedBy(getHasName(Registration.CORE.get(SolarPanelLevel.LEADSTONE).get()), has(Registration.CORE.get(SolarPanelLevel.LEADSTONE).get()))
                .unlockedBy(getHasName(Registration.PHOTOVOLTAIC_CELL.get()), has(Registration.PHOTOVOLTAIC_CELL.get()))
                .save(consumer);

        for(int i = 1; i < SolarPanelLevel.values().length; i++) {
            var level = SolarPanelLevel.values()[i];
            var currentSolarPanel = Registration.SOLAR_PANEL_BLOCK.get(level).get();
            var prevSolarPanel = Registration.SOLAR_PANEL_BLOCK.get(SolarPanelLevel.values()[i - 1]).get();
            var core = Registration.CORE.get(level).get();
            ShapedRecipeBuilder.shaped(currentSolarPanel)
                    .pattern("aaa")
                    .pattern("aba")
                    .pattern("aaa")
                    .define('a', prevSolarPanel)
                    .define('b', core)
                    .unlockedBy(getHasName(prevSolarPanel), has(prevSolarPanel))
                    .unlockedBy(getHasName(core), has(core))
                    .save(consumer);
        }
    }

    private void solarCore(Consumer<FinishedRecipe> consumer) {
        Map<SolarPanelLevel, ResourceLocation> materials = new HashMap<>();
        materials.put(SolarPanelLevel.HARDENED, new ResourceLocation("forge", "nuggets/invar"));
        materials.put(SolarPanelLevel.REDSTONE, new ResourceLocation("forge", "nuggets/electrum"));
        materials.put(SolarPanelLevel.SIGNALUM, new ResourceLocation("forge", "nuggets/signalum"));
        materials.put(SolarPanelLevel.RESONANT, new ResourceLocation("forge", "nuggets/enderium"));
        materials.put(SolarPanelLevel.ADVANCED, new ResourceLocation("forge", "nuggets/lumium"));
        materials.put(SolarPanelLevel.ULTIMATE, new ResourceLocation("forge", "nuggets/platinum"));

        ShapedRecipeBuilder.shaped(Registration.CORE.get(SolarPanelLevel.LEADSTONE).get())
                .pattern(" a ")
                .pattern("aba")
                .pattern(" a ")
                .define('a', ItemTags.create(new ResourceLocation("forge", "nuggets/lead")))
                .define('b', ItemTags.create(new ResourceLocation("forge", "ingots/iron")))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(consumer);

        for(int i = 1; i < SolarPanelLevel.values().length; i++) {
            var level = SolarPanelLevel.values()[i];
            var core = Registration.CORE.get(level).get();
            var prevCore = Registration.CORE.get(SolarPanelLevel.values()[i - 1]).get();
            ShapedRecipeBuilder.shaped(core)
                    .pattern(" a ")
                    .pattern("aba")
                    .pattern(" a ")
                    .define('a', ItemTags.create(materials.get(level)))
                    .define('b', prevCore)
                    .unlockedBy(getHasName(prevCore), has(prevCore))
                    .save(consumer);
        }

    }

    private Item getVanillaHelmet(SolarPanelLevel level) {
        return switch(level) {
            case LEADSTONE, HARDENED -> Items.IRON_HELMET;
            case REDSTONE, SIGNALUM, RESONANT, ADVANCED -> Items.DIAMOND_HELMET;
            case ULTIMATE -> Items.NETHERITE_HELMET;
        };
    }
}
