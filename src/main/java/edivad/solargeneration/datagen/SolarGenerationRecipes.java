package edivad.solargeneration.datagen;

import java.util.HashMap;
import java.util.Map;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.setup.ModRegistration;
import edivad.solargeneration.tags.SolarGenerationTags;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.NeoForgeConditions;

public class SolarGenerationRecipes extends RecipeProvider {

  protected SolarGenerationRecipes(BootstrapContext<Recipe<?>> recipeOutput,
      BootstrapContext<Advancement> advancementOutput) {
    super(recipeOutput, advancementOutput);
  }

  public static MultiRegistryBootstrap create() {
    return RecipeProvider.asBootstrap(SolarGenerationRecipes::new);
  }

  @Override
  protected void buildRecipes() {
    supportingItems();
    solarPanelReverse();
    solarHelmet();
    solarPanel();
    solarCore();
  }

  private void supportingItems() {
    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, Items.LAPIS_LAZULI)
        .pattern("aaa")
        .pattern("aaa")
        .pattern("aaa")
        .define('a', ModRegistration.LAPIS_SHARD.get())
        .unlockedBy(getHasName(ModRegistration.LAPIS_SHARD.get()), has(ModRegistration.LAPIS_SHARD.get()))
        .save(this.output, SolarGeneration.id("lapis_lazuli_from_shard").toString());

    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, ModRegistration.LAPIS_SHARD.get(), 36)
        .pattern("aa")
        .pattern("aa")
        .define('a', Items.LAPIS_LAZULI)
        .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI))
        .save(this.output);

    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, ModRegistration.PHOTOVOLTAIC_CELL.get())
        .pattern("aaa")
        .pattern("bbb")
        .pattern("ccc")
        .define('a', Items.GLASS_PANE)
        .define('b', ModRegistration.LAPIS_SHARD.get())
        .define('c', Tags.Items.NUGGETS_IRON)
        .unlockedBy(getHasName(ModRegistration.LAPIS_SHARD.get()), has(ModRegistration.LAPIS_SHARD.get()))
        .save(this.output);
  }

  private void solarPanelReverse() {
    for (var level : SolarPanelLevel.values()) {
      var solarPanel = ModRegistration.SOLAR_PANEL_BLOCK.get(level).get();
      var helmet = ModRegistration.HELMET.get(level).get();
      var resourceLocation = SolarGeneration.id(level.getSolarPanelName() + "_reverse");
      ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, solarPanel)
          .requires(helmet)
          .unlockedBy(getHasName(helmet), has(helmet))
          .save(this.output, resourceLocation.toString());
    }
  }

  private void solarHelmet() {
    for (var level : SolarPanelLevel.values()) {
      var solarPanel = ModRegistration.SOLAR_PANEL_BLOCK.get(level).get();
      var helmet = ModRegistration.HELMET.get(level).get();
      ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, helmet)
          .requires(solarPanel)
          .requires(getVanillaHelmet(level))
          .unlockedBy(getHasName(solarPanel), has(solarPanel))
          .save(this.output);
    }
  }

  private void solarPanel() {
    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC,
            ModRegistration.SOLAR_PANEL_BLOCK.get(SolarPanelLevel.LEADSTONE).get())
        .pattern("aaa")
        .pattern("bcb")
        .pattern("ddd")
        .define('a', ModRegistration.PHOTOVOLTAIC_CELL.get())
        .define('b', Items.REDSTONE)
        .define('c', ModRegistration.CORE.get(SolarPanelLevel.LEADSTONE).get())
        .define('d', SolarGenerationTags.Items.NUGGETS_STEEL)
        .unlockedBy(getHasName(ModRegistration.CORE.get(SolarPanelLevel.LEADSTONE).get()),
            has(ModRegistration.CORE.get(SolarPanelLevel.LEADSTONE).get()))
        .unlockedBy(getHasName(ModRegistration.PHOTOVOLTAIC_CELL.get()),
            has(ModRegistration.PHOTOVOLTAIC_CELL.get()))
        .save(ifTagNotEmpty(SolarGenerationTags.Items.NUGGETS_STEEL));

    for (int i = 1; i < SolarPanelLevel.values().length; i++) {
      var level = SolarPanelLevel.values()[i];
      var currentSolarPanel = ModRegistration.SOLAR_PANEL_BLOCK.get(level).get();
      var prevSolarPanel = ModRegistration.SOLAR_PANEL_BLOCK.get(SolarPanelLevel.values()[i - 1])
          .get();
      var core = ModRegistration.CORE.get(level).get();
      ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, currentSolarPanel)
          .pattern("aaa")
          .pattern("aba")
          .pattern("aaa")
          .define('a', prevSolarPanel)
          .define('b', core)
          .unlockedBy(getHasName(prevSolarPanel), has(prevSolarPanel))
          .unlockedBy(getHasName(core), has(core))
          .save(this.output);
    }
  }

  private void solarCore() {
    Map<SolarPanelLevel, TagKey<Item>> materials = new HashMap<>();
    materials.put(SolarPanelLevel.HARDENED, SolarGenerationTags.Items.NUGGETS_INVAR);
    materials.put(SolarPanelLevel.REDSTONE, SolarGenerationTags.Items.NUGGETS_ELECTRUM);
    materials.put(SolarPanelLevel.SIGNALUM, SolarGenerationTags.Items.NUGGETS_SIGNALUM);
    materials.put(SolarPanelLevel.RESONANT, SolarGenerationTags.Items.NUGGETS_ENDERIUM);
    materials.put(SolarPanelLevel.ADVANCED, SolarGenerationTags.Items.NUGGETS_LUMIUM);
    materials.put(SolarPanelLevel.ULTIMATE, SolarGenerationTags.Items.NUGGETS_PLATINUM);

    ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC,
            ModRegistration.CORE.get(SolarPanelLevel.LEADSTONE).get())
        .pattern(" a ")
        .pattern("aba")
        .pattern(" a ")
        .define('a', SolarGenerationTags.Items.NUGGETS_LEAD)
        .define('b', Tags.Items.INGOTS_IRON)
        .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
        .save(ifTagNotEmpty(SolarGenerationTags.Items.NUGGETS_LEAD));

    for (int i = 1; i < SolarPanelLevel.values().length; i++) {
      var level = SolarPanelLevel.values()[i];
      var core = ModRegistration.CORE.get(level).get();
      var prevCore = ModRegistration.CORE.get(SolarPanelLevel.values()[i - 1]).get();
      var material = materials.get(level);
      ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, core)
          .pattern(" a ")
          .pattern("aba")
          .pattern(" a ")
          .define('a', material)
          .define('b', prevCore)
          .unlockedBy(getHasName(prevCore), has(prevCore))
          .save(ifTagNotEmpty(material));
    }
  }

  private RecipeOutput ifTagNotEmpty(TagKey<Item> tag) {
    return this.output.withConditions(NeoForgeConditions.not(NeoForgeConditions.tagEmpty(tag)));
  }

  private Item getVanillaHelmet(SolarPanelLevel level) {
    return switch (level) {
      case LEADSTONE, HARDENED -> Items.IRON_HELMET;
      case REDSTONE, SIGNALUM, RESONANT, ADVANCED -> Items.DIAMOND_HELMET;
      case ULTIMATE -> Items.NETHERITE_HELMET;
    };
  }
}
