package edivad.solargeneration.blocks;

import edivad.solargeneration.blockentity.BlockEntitySolarPanel;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelBattery;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Tooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SolarPanel extends Block implements EntityBlock, SimpleWaterloggedBlock {

    private final SolarPanelLevel levelSolarPanel;
    private static final VoxelShape BOX = createShape();
    private static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

    public SolarPanel(SolarPanelLevel levelSolarPanel) {
        super(Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(1.5F, 6.0F));
        this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
        this.levelSolarPanel = levelSolarPanel;
    }

    private static VoxelShape createShape() {
        ArrayList<VoxelShape> shapes = new ArrayList<>();
        shapes.add(box(0, 0, 0, 16, 1, 16));//bottom
        shapes.add(box(7, 1, 7, 9, 9, 9));//mainpillar
        shapes.add(box(6, 1, 9, 7, 9, 10));//pillar1
        shapes.add(box(9, 1, 9, 10, 9, 10));//pillar2
        shapes.add(box(9, 1, 6, 10, 9, 7));//pillar3
        shapes.add(box(6, 1, 6, 7, 9, 7));//pillar4
        shapes.add(box(0, 9, 0, 16, 12, 16));//top

        VoxelShape combinedShape = Shapes.empty();
        for(VoxelShape shape : shapes) {
            combinedShape = Shapes.joinUnoptimized(combinedShape, shape, BooleanOp.OR);
        }
        return combinedShape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter blockGetter , BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if(level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity tileEntity = level.getBlockEntity(pos);
        if(tileEntity instanceof MenuProvider menu) {
            NetworkHooks.openScreen((ServerPlayer) player, menu, tileEntity.getBlockPos());
        }
        else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return willHarvest || super.onDestroyedByPlayer(state, level, pos, player, false, fluid);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity te, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, te, stack);
        level.removeBlock(pos, false);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntitySolarPanel(levelSolarPanel, blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createSolarPanelTicker(level, blockEntityType, Registration.SOLAR_PANEL_TILE.get(levelSolarPanel).get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createSolarPanelTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends BlockEntitySolarPanel> tile) {
        BlockEntityTicker<BlockEntitySolarPanel> ticker = BlockEntitySolarPanel::serverTick;
        return level.isClientSide ? null : tile == blockEntityType ? (BlockEntityTicker<T>) ticker : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if(!level.isClientSide) {
            BlockEntitySolarPanel tile = ((BlockEntitySolarPanel) level.getBlockEntity(blockPos));
            if(itemStack.hasTag()) {
                tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(t -> {
                    SolarPanelBattery energyStorage = (SolarPanelBattery) t;
                    energyStorage.setEnergy(itemStack.getTag().getInt("energy"));
                });
            }
        }
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            int energy = stack.getTag().getInt("energy");
            tooltip.add(Tooltip.showInfoCtrl(energy));
        }
        tooltip.addAll(Tooltip.showInfoShift(this.levelSolarPanel));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return SimpleWaterloggedBlock.super.placeLiquid(levelAccessor, pos, state, fluidStateIn);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter blockGetter, BlockPos pos, BlockState state, Fluid fluidIn) {
        return SimpleWaterloggedBlock.super.canPlaceLiquid(blockGetter, pos, state, fluidIn);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }
}
