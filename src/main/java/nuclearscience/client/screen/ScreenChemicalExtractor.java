package nuclearscience.client.screen;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.ElectricUnit;
import electrodynamics.common.item.subtype.SubtypeProcessorUpgrade;
import electrodynamics.prefab.inventory.container.slot.SlotRestricted;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.ScreenComponentFluid;
import electrodynamics.prefab.screen.component.ScreenComponentInfo;
import electrodynamics.prefab.screen.component.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.ScreenComponentSlot;
import electrodynamics.prefab.screen.component.ScreenComponentSlot.EnumSlotType;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.utils.AbstractFluidHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import nuclearscience.common.inventory.container.ContainerChemicalExtractor;
import nuclearscience.common.tile.TileChemicalExtractor;

@OnlyIn(Dist.CLIENT)
public class ScreenChemicalExtractor extends GenericScreen<ContainerChemicalExtractor> {
    public ScreenChemicalExtractor(ContainerChemicalExtractor container, PlayerInventory playerInventory, ITextComponent title) {
	super(container, playerInventory, title);
	components.add(new ScreenComponentProgress(() -> {
	    GenericTile furnace = container.getHostFromIntArray();
	    if (furnace != null) {
		ComponentProcessor processor = furnace.getComponent(ComponentType.Processor);
		if (processor.operatingTicks > 0) {
		    return Math.min(1.0, processor.operatingTicks / (processor.requiredTicks / 2.0));
		}
	    }
	    return 0;
	}, this, 42, 30));
	components.add(new ScreenComponentProgress(() -> {
	    GenericTile furnace = container.getHostFromIntArray();
	    if (furnace != null) {
		ComponentProcessor processor = furnace.getComponent(ComponentType.Processor);
		if (processor.operatingTicks > processor.requiredTicks / 2.0) {
		    return Math.min(1.0, (processor.operatingTicks - processor.requiredTicks / 2.0) / (processor.requiredTicks / 2.0));
		}
	    }
	    return 0;
	}, this, 98, 30));
	components.add(new ScreenComponentProgress(() -> 0, this, 46, 50).left());
	components.add(new ScreenComponentFluid(() -> {
	    TileChemicalExtractor boiler = container.getHostFromIntArray();
	    if (boiler != null) {
		AbstractFluidHandler<?> handler = boiler.getComponent(ComponentType.FluidHandler);
		for (Fluid fluid : handler.getValidInputFluids()) {
		    FluidTank tank = handler.getTankFromFluid(fluid, true);
		    if (tank.getFluidAmount() > 0) {
			return tank;
		    }
		}
	    }
	    return null;
	}, this, 21, 18));
	components.add(new ScreenComponentElectricInfo(this::getEnergyInformation, this, -ScreenComponentInfo.SIZE + 1, 2));
    }

    @Override
    protected ScreenComponentSlot createScreenSlot(Slot slot) {
	return new ScreenComponentSlot(slot instanceof SlotRestricted && ((SlotRestricted) slot)
		.isItemValid(new ItemStack(electrodynamics.DeferredRegisters.SUBTYPEITEM_MAPPINGS.get(SubtypeProcessorUpgrade.basicspeed)))
			? EnumSlotType.SPEED
			: slot instanceof SlotRestricted ? EnumSlotType.LIQUID : EnumSlotType.NORMAL,
		this, slot.xPos - 1, slot.yPos - 1);
    }

    private List<? extends ITextProperties> getEnergyInformation() {
	ArrayList<ITextProperties> list = new ArrayList<>();
	GenericTile box = container.getHostFromIntArray();
	if (box != null) {
	    ComponentElectrodynamic electro = box.getComponent(ComponentType.Electrodynamic);
	    ComponentProcessor processor = box.getComponent(ComponentType.Processor);

	    list.add(new TranslationTextComponent("gui.chemicalextractor.usage",
		    new StringTextComponent(ChatFormatter.getElectricDisplayShort(processor.getUsage() * 20, ElectricUnit.WATT))
			    .mergeStyle(TextFormatting.GRAY)).mergeStyle(TextFormatting.DARK_GRAY));
	    list.add(new TranslationTextComponent("gui.chemicalextractor.voltage",
		    new StringTextComponent(ChatFormatter.getElectricDisplayShort(electro.getVoltage(), ElectricUnit.VOLTAGE))
			    .mergeStyle(TextFormatting.GRAY)).mergeStyle(TextFormatting.DARK_GRAY));
	}
	return list;
    }
}