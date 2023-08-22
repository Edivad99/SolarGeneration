package edivad.solargeneration.tags;

import edivad.solargeneration.SolarGeneration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;


public class SolarGenerationTags {

  public static class Items {

    public static final TagKey<Item> SOLAR_PANEL = tag("solar_panel");
    public static final TagKey<Item> SOLAR_HELMET = tag("solar_helmet");

    private static TagKey<Item> tag(String name) {
      return ItemTags.create(new ResourceLocation(SolarGeneration.ID, name));
    }
  }
}
