package nuclearscience.common.tile;

import electrodynamics.common.block.VoxelShapes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuclearscience.common.inventory.container.ContainerMoltenSaltSupplier;
import nuclearscience.common.settings.Constants;
import nuclearscience.registers.NuclearScienceBlockTypes;
import nuclearscience.registers.NuclearScienceBlocks;
import nuclearscience.registers.NuclearScienceItems;

public class TileMoltenSaltSupplier extends GenericTile {

	protected CachedTileOutput output;

	public TileMoltenSaltSupplier(BlockPos pos, BlockState state) {
		super(NuclearScienceBlockTypes.TILE_MOLTENSALTSUPPLIER.get(), pos, state);
		addComponent(new ComponentDirection());
		addComponent(new ComponentTickable().tickServer(this::tickServer));
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentElectrodynamic(this).voltage(Constants.MOLTENSALTSUPPLIER_VOLTAGE).extractPower((x, y) -> TransferPack.EMPTY).input(Direction.UP).input(Direction.DOWN).maxJoules(Constants.MOLTENSALTSUPPLIER_USAGE_PER_TICK * 20));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().inputs(1)).slotFaces(0, Direction.values()).valid((slot, stack, i) -> stack.getItem() == NuclearScienceItems.ITEM_LIFHT4PUF3.get()));
		addComponent(new ComponentContainerProvider("container.moltensaltsupplier").createMenu((id, player) -> new ContainerMoltenSaltSupplier(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
	}

	public void tickServer(ComponentTickable tickable) {
		Direction dir = this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
		if (output == null) {
			output = new CachedTileOutput(level, worldPosition.relative(dir.getOpposite()));
		}
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		boolean enoughPower = electro.getJoulesStored() >= Constants.MOLTENSALTSUPPLIER_USAGE_PER_TICK;
		if (!enoughPower) {
			return;
		}

		if (BlockEntityUtils.isLit(this) ^ enoughPower) {
			BlockEntityUtils.updateLit(this, enoughPower);
		}

		electro.joules(electro.getJoulesStored() - Constants.MOLTENSALTSUPPLIER_USAGE_PER_TICK);

		if (tickable.getTicks() % 40 != 0) {
			return;
		}
		output.update(worldPosition.relative(dir.getOpposite()));
		ItemStack in = this.<ComponentInventory>getComponent(ComponentType.Inventory).getItem(0);
		if (in.getCount() > 0 && output.valid() && output.getSafe() instanceof TileMSRReactorCore core) {
			if (core.<ComponentDirection>getComponent(ComponentType.Direction).getDirection() == dir) {
				if (TileMSRReactorCore.FUEL_CAPACITY - core.currentFuel.get() >= 250) {
					in.shrink(1);
					core.currentFuel.set(core.currentFuel.get() + 250);
				}
			}
		}

	}

	static {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.join(shape, Shapes.box(0.0625, 0.125, 0.25, 0.9375, 0.75, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.1875, 0.9375, 0.6875, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.75, 0.9375, 0.6875, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0, 0.1875, 0.25, 0.0625, 0.6875, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.9375, 0.1875, 0.25, 1, 0.6875, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.25, 0.0625, 0.25, 0.75, 0.125, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.0625, 0.6875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.8125, 0.125, 0.1875, 0.875, 0.1875, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.8125, 0.125, 0.75, 0.875, 0.1875, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.8125, 0.1875, 0.125, 0.875, 0.6875, 0.1875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.8125, 0.1875, 0.8125, 0.875, 0.6875, 0.875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.8125, 0.0625, 0.25, 0.875, 0.125, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.75, 0.0625, 0.3125, 0.8125, 0.125, 0.375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.75, 0.0625, 0.625, 0.8125, 0.125, 0.6875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.8125, 0.75, 0.25, 0.875, 0.8125, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.8125, 0.6875, 0.75, 0.875, 0.75, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.8125, 0.6875, 0.1875, 0.875, 0.75, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.625, 0.125, 0.1875, 0.6875, 0.1875, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.625, 0.125, 0.75, 0.6875, 0.1875, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.625, 0.1875, 0.125, 0.6875, 0.6875, 0.1875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.625, 0.1875, 0.8125, 0.6875, 0.6875, 0.875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.625, 0.75, 0.25, 0.6875, 0.8125, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.625, 0.6875, 0.75, 0.6875, 0.75, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.625, 0.6875, 0.1875, 0.6875, 0.75, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.1875, 0.375, 0.1875, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.75, 0.375, 0.1875, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3125, 0.1875, 0.125, 0.375, 0.6875, 0.1875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3125, 0.1875, 0.8125, 0.375, 0.6875, 0.875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3125, 0.75, 0.25, 0.375, 0.8125, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3125, 0.6875, 0.75, 0.375, 0.75, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3125, 0.6875, 0.1875, 0.375, 0.75, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.125, 0.1875, 0.1875, 0.1875, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.125, 0.75, 0.1875, 0.1875, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.1875, 0.125, 0.1875, 0.6875, 0.1875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.1875, 0.8125, 0.1875, 0.6875, 0.875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.0625, 0.25, 0.1875, 0.125, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.75, 0.25, 0.1875, 0.8125, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.6875, 0.75, 0.1875, 0.75, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.6875, 0.1875, 0.1875, 0.75, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.1875, 0.0625, 0.3125, 0.25, 0.125, 0.375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.1875, 0.0625, 0.625, 0.25, 0.125, 0.6875), BooleanOp.OR);
		VoxelShapes.registerShape(NuclearScienceBlocks.blockMoltenSaltSupplier, shape, Direction.EAST);
	}
}
