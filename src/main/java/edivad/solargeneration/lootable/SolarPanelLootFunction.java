package edivad.solargeneration.lootable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import edivad.solargeneration.setup.SGLootFunctions;
import edivad.solargeneration.tile.TileEntitySolarPanel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SolarPanelLootFunction extends LootItemConditionalFunction {

    public SolarPanelLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext lootContext) {
        BlockEntity blockEntity = lootContext.getParam(LootContextParams.BLOCK_ENTITY);
        if(blockEntity instanceof TileEntitySolarPanel tile) {
            stack.getOrCreateTag().putInt("energy", tile.getEnergy());
        }
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return SGLootFunctions.getSolarPanel();
    }

    public static LootItemConditionalFunction.Builder<?> builder() {
        return simpleBuilder(SolarPanelLootFunction::new);
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SolarPanelLootFunction> {

        @Override
        public SolarPanelLootFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            return new SolarPanelLootFunction(conditions);
        }
    }
}
