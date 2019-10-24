package edivad.solargeneration.blocks;

import java.util.List;
import java.util.Random;

import edivad.solargeneration.Main;
import edivad.solargeneration.tile.TileEntityAdvancedSolarPanel;
import edivad.solargeneration.tile.TileEntityHardenedSolarPanel;
import edivad.solargeneration.tile.TileEntityLeadstoneSolarPanel;
import edivad.solargeneration.tile.TileEntityRedstoneSolarPanel;
import edivad.solargeneration.tile.TileEntityResonantSolarPanel;
import edivad.solargeneration.tile.TileEntitySignalumSolarPanel;
import edivad.solargeneration.tile.TileEntitySolarPanel;
import edivad.solargeneration.tile.TileEntityUltimateSolarPanel;
import edivad.solargeneration.tools.Platform;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Tooltip;
import edivad.solargeneration.tools.inter.IGuiTile;
import edivad.solargeneration.tools.inter.IRestorableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SolarPanel extends Block implements ITileEntityProvider {

	private final SolarPanelLevel levelSolarPanel;
	private static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

	public SolarPanel(SolarPanelLevel levelSolarPanel)
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(5F);
		setResistance(30F);
		setHarvestLevel("pickaxe", 0);
		this.levelSolarPanel = levelSolarPanel;

		setRegistryName(levelSolarPanel.getBlockResourceLocation());
		setUnlocalizedName(Main.MODID + "." + levelSolarPanel.getBlockResourceLocation().getResourcePath());

		setCreativeTab(Main.solarGenerationTab);
	}

	public SolarPanelLevel getLevelSolarPanel()
	{
		return this.levelSolarPanel;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getByNameOrId(Main.MODID + ":solar_panel_" + levelSolarPanel.name().toLowerCase());
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this.getItemDropped(state, RANDOM, 0));
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.DOWN;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB_TOP_HALF;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return true;

		if(playerIn != null)
		{
			if(Platform.isWrench(playerIn, playerIn.getHeldItemMainhand(), pos) && playerIn.isSneaking())
			{
				dismantleBlock(worldIn, pos);
				return true;
			}
		}

		TileEntity te = worldIn.getTileEntity(pos);
		if(!(te instanceof IGuiTile))
			return false;

		playerIn.openGui(Main.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	private void dismantleBlock(World worldIn, BlockPos pos)
	{
		ItemStack itemStack = new ItemStack(this);

		TileEntitySolarPanel localTileEntity = (TileEntitySolarPanel) worldIn.getTileEntity(pos);
		int internalEnergy = localTileEntity.getEnergy();
		if(internalEnergy > 0)
		{
			if(itemStack.getTagCompound() == null)
			{
				itemStack.setTagCompound(new NBTTagCompound());
			}
			itemStack.getTagCompound().setInteger("energy", internalEnergy);
		}

		worldIn.setBlockToAir(pos);

		EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
		entityItem.motionX = 0;
		entityItem.motionZ = 0;
		worldIn.spawnEntity(entityItem);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> result, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{

		TileEntity tileEntity = world.getTileEntity(pos);

		// Always check this!!
		if(tileEntity instanceof IRestorableTileEntity)
		{
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			NBTTagCompound tagCompound = new NBTTagCompound();
			((IRestorableTileEntity) tileEntity).writeRestorableToNBT(tagCompound);

			stack.setTagCompound(tagCompound);
			result.add(stack);
		}
		else
		{
			super.getDrops(result, world, pos, state, fortune);
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if(willHarvest)
			return true;
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		worldIn.setBlockToAir(pos);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		// Always check this!!
		if(tileEntity instanceof IRestorableTileEntity)
		{
			NBTTagCompound tagCompound = stack.getTagCompound();
			if(tagCompound != null)
			{
				((IRestorableTileEntity) tileEntity).readRestorableFromNBT(tagCompound);
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		switch (this.levelSolarPanel)
		{
			case Leadstone:
				return new TileEntityLeadstoneSolarPanel();
			case Hardened:
				return new TileEntityHardenedSolarPanel();
			case Redstone:
				return new TileEntityRedstoneSolarPanel();
			case Signalum:
				return new TileEntitySignalumSolarPanel();
			case Resonant:
				return new TileEntityResonantSolarPanel();
			case Advanced:
				return new TileEntityAdvancedSolarPanel();
			case Ultimate:
				return new TileEntityUltimateSolarPanel();
			default:
				return null;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		NBTTagCompound tagCompound = stack.getTagCompound();
		int energy = 0;
		if(tagCompound != null)
			energy = tagCompound.getInteger("energy");
		Tooltip.showInfoCtrl(energy, tooltip);
		Tooltip.showInfoShift(this.levelSolarPanel, tooltip);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
