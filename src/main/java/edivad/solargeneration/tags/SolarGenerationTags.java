package edivad.solargeneration.tags;

import edivad.solargeneration.SolarGeneration;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;


public class SolarGenerationTags {

  public static class Items {

    public static final TagKey<Item> SOLAR_PANEL = tag("solar_panel");
    public static final TagKey<Item> SOLAR_HELMET = tag("solar_helmet");

    public static final TagKey<Item> NUGGETS_LEAD = commonTag("nuggets/lead");
    public static final TagKey<Item> NUGGETS_STEEL = commonTag("nuggets/steel");
    public static final TagKey<Item> NUGGETS_INVAR = commonTag("nuggets/invar");
    public static final TagKey<Item> NUGGETS_ELECTRUM = commonTag("nuggets/electrum");
    public static final TagKey<Item> NUGGETS_SIGNALUM = commonTag("nuggets/signalum");
    public static final TagKey<Item> NUGGETS_ENDERIUM = commonTag("nuggets/enderium");
    public static final TagKey<Item> NUGGETS_LUMIUM = commonTag("nuggets/lumium");
    public static final TagKey<Item> NUGGETS_PLATINUM = commonTag("nuggets/platinum");

    private static TagKey<Item> tag(String name) {
      return ItemTags.create(SolarGeneration.id(name));
    }

    private static TagKey<Item> commonTag(String name) {
      return ItemTags.create(Identifier.fromNamespaceAndPath("c", name));
    }
  }
}
