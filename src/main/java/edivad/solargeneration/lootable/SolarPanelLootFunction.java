package edivad.solargeneration.lootable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import edivad.solargeneration.blockentity.BlockEntitySolarPanel;
import edivad.solargeneration.setup.Registration;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.concurrent.atomic.AtomicInteger;

public class SolarPanelLootFunction extends LootItemConditionalFunction {

    public SolarPanelLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext lootContext) {
        BlockEntity blockEntity = lootContext.getParam(LootContextParams.BLOCK_ENTITY);
        if(blockEntity instanceof BlockEntitySolarPanel tile) {
            AtomicInteger energy = new AtomicInteger();
            tile.getCapability(CapabilityEnergy.ENERGY, Direction.DOWN).ifPresent(handler -> energy.set(handler.getEnergyStored()));
            stack.getOrCreateTag().putInt("energy", energy.get());
        }
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return Registration.REGISTERED_LOOT_ITEM_FUNCTIONS.get("solar_panel").get();
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
