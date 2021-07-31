package edivad.solargeneration.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tile.TileEntitySolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Tooltip;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class SolarPanel extends Block implements EntityBlock, SimpleWaterloggedBlock {

    private final SolarPanelLevel levelSolarPanel;
    private static final VoxelShape BOX = createShape();
    private static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

    public SolarPanel(SolarPanelLevel levelSolarPanel)
    {
        super(Properties.of(Material.METAL).sound(SoundType.METAL).strength(1.5F, 6.0F));
        this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
        this.levelSolarPanel = levelSolarPanel;
    }

    private static VoxelShape createShape()
    {
        ArrayList<VoxelShape> shapes = new ArrayList<>();
        shapes.add(box(0, 0, 0, 16, 1, 16));//bottom
        shapes.add(box(7, 1, 7, 9, 9, 9));//mainpillar
        shapes.add(box(6, 1, 9, 7, 9, 10));//pillar1
        shapes.add(box(9, 1, 9, 10, 9, 10));//pillar2
        shapes.add(box(9, 1, 6, 10, 9, 7));//pillar3
        shapes.add(box(6, 1, 6, 7, 9, 7));//pillar4
        shapes.add(box(0, 9, 0, 16, 12, 16));//top

        VoxelShape combinedShape = Shapes.empty();
        for(VoxelShape shape : shapes)
        {
            combinedShape = Shapes.joinUnoptimized(combinedShape, shape, BooleanOp.OR);
        }
        return combinedShape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return BOX;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return BOX;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
    {
        if(level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity tileEntity = level.getBlockEntity(pos);
        if(tileEntity instanceof MenuProvider menu)
        {
            NetworkHooks.openGui((ServerPlayer) player, menu, tileEntity.getBlockPos());
        }
        else
        {
            throw new IllegalStateException("Our named container provider is missing!");
        }
        return InteractionResult.SUCCESS;
    }

    private void dismantleBlock(Level worldIn, BlockPos pos)
    {
        ItemStack itemStack = new ItemStack(this);

        TileEntitySolarPanel localTileEntity = (TileEntitySolarPanel) worldIn.getBlockEntity(pos);
        int internalEnergy = localTileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
        if(internalEnergy > 0)
        {
            CompoundTag energyValue = new CompoundTag();
            energyValue.putInt("value", internalEnergy);

            CompoundTag energy = new CompoundTag();
            energy.put("energy", energyValue);

            CompoundTag root = new CompoundTag();
            root.put("BlockEntityTag", energy);
            itemStack.setTag(root);
        }

        worldIn.removeBlock(pos, false);

        ItemEntity entityItem = new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);

        entityItem.setDeltaMovement(0, entityItem.getMyRidingOffset(), 0);
        worldIn.addFreshEntity(entityItem);
    }

    @Override
    public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid)
    {
        return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, BlockEntity te, ItemStack stack)
    {
        super.playerDestroy(worldIn, player, pos, state, te, stack);
        worldIn.removeBlock(pos, false);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return new TileEntitySolarPanel(levelSolarPanel, blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType)
    {
        return createSolarPanelTicker(level, blockEntityType, Registration.SOLAR_PANEL_TILE.get(levelSolarPanel).get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createSolarPanelTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends TileEntitySolarPanel> tile) {
        BlockEntityTicker<TileEntitySolarPanel> ticker = TileEntitySolarPanel::serverTick;
        return level.isClientSide ? null : tile == blockEntityType ? (BlockEntityTicker<T>) ticker : null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        CompoundTag compoundnbt = stack.getTagElement("BlockEntityTag");
        int energy = 0;
        if(compoundnbt != null)
            if(compoundnbt.contains("energy"))
                energy = compoundnbt.getCompound("energy").getInt("value");

        Tooltip.showInfoCtrl(energy, tooltip);
        Tooltip.showInfoShift(this.levelSolarPanel, tooltip);
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn)
    {
        return SimpleWaterloggedBlock.super.placeLiquid(worldIn, pos, state, fluidStateIn);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn)
    {
        return SimpleWaterloggedBlock.super.canPlaceLiquid(worldIn, pos, state, fluidIn);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }
}
