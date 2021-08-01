package nuclearscience.common.tile;

import electrodynamics.api.electricity.CapabilityElectrodynamic;
import electrodynamics.common.recipe.categories.fluiditem2item.FluidItem2ItemRecipe;
import electrodynamics.prefab.tile.GenericTileTicking;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandler;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentProcessorType;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import nuclearscience.DeferredRegisters;
import nuclearscience.common.inventory.container.ContainerChemicalExtractor;
import nuclearscience.common.recipe.NuclearScienceRecipeInit;
import nuclearscience.common.settings.Constants;

public class TileChemicalExtractor extends GenericTileTicking {

    public static final int MAX_TANK_CAPACITY = 5000;

    public static Fluid[] SUPPORTED_INPUT_FLUIDS = new Fluid[] {

	    Fluids.WATER

    };

    public TileChemicalExtractor() {
	super(DeferredRegisters.TILE_CHEMICALEXTRACTOR.get());
	addComponent(new ComponentTickable().tickClient(this::tickClient));
	addComponent(new ComponentDirection());
	addComponent(new ComponentPacketHandler());
	addComponent(new ComponentElectrodynamic(this).enableUniversalInput().voltage(CapabilityElectrodynamic.DEFAULT_VOLTAGE * 2)
		.maxJoules(Constants.CHEMICALEXTRACTOR_USAGE_PER_TICK * 10));
	addComponent(new ComponentFluidHandler(this).addMultipleFluidTanks(SUPPORTED_INPUT_FLUIDS, MAX_TANK_CAPACITY, true).universalInput());
	addComponent(new ComponentInventory(this).size(6).faceSlots(Direction.UP, 0).faceSlots(Direction.DOWN, 1).slotFaces(2, Direction.SOUTH,
		Direction.NORTH, Direction.EAST, Direction.WEST));
	addComponent(new ComponentProcessor(this).upgradeSlots(3, 4, 5).type(ComponentProcessorType.ObjectToObject)
		.usage(Constants.CHEMICALEXTRACTOR_USAGE_PER_TICK).requiredTicks(Constants.CHEMICALEXTRACTOR_REQUIRED_TICKS)
		.canProcess(component -> component.consumeBucket(MAX_TANK_CAPACITY, SUPPORTED_INPUT_FLUIDS, 2)
			.canProcessFluidItem2ItemRecipe(component, FluidItem2ItemRecipe.class, NuclearScienceRecipeInit.CHEMICAL_EXTRACTOR_TYPE))
		.process(component -> component.processFluidItem2ItemRecipe(component, FluidItem2ItemRecipe.class)));
	addComponent(new ComponentContainerProvider("container.chemicalextractor")
		.createMenu((id, player) -> new ContainerChemicalExtractor(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
    }

    protected void tickClient(ComponentTickable tickable) {
	if (this.<ComponentProcessor>getComponent(ComponentType.Processor).operatingTicks > 0 && world.rand.nextDouble() < 0.15) {
	    world.addParticle(ParticleTypes.SMOKE, pos.getX() + world.rand.nextDouble(), pos.getY() + world.rand.nextDouble() * 0.8 + 0.5,
		    pos.getZ() + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
	}
    }

}
