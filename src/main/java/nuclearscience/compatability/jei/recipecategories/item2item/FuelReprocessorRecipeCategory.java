package nuclearscience.compatability.jei.recipecategories.item2item;

import electrodynamics.api.References;
import electrodynamics.compatability.jei.ElectrodynamicsJEIPlugin;
import electrodynamics.compatability.jei.recipecategories.item2item.Item2ItemRecipeCategory;
import electrodynamics.compatability.jei.utils.gui.arrows.animated.ArrowRightAnimatedWrapper;
import electrodynamics.compatability.jei.utils.gui.backgroud.BackgroundWrapper;
import electrodynamics.compatability.jei.utils.gui.item.BigItemSlotWrapper;
import electrodynamics.compatability.jei.utils.gui.item.DefaultItemSlotWrapper;
import electrodynamics.compatability.jei.utils.label.PowerLabelWrapper;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FuelReprocessorRecipeCategory extends Item2ItemRecipeCategory {

	// JEI Window Parameters
	private static BackgroundWrapper BACK_WRAP = new BackgroundWrapper(132,58);
	
	private static DefaultItemSlotWrapper INPUT_SLOT = new DefaultItemSlotWrapper(22, 20);
	private static BigItemSlotWrapper OUTPUT_SLOT = new BigItemSlotWrapper(83, 16);
	
	private static ArrowRightAnimatedWrapper ANIM_ARROW = new ArrowRightAnimatedWrapper(50, 23);
	
	private static PowerLabelWrapper POWER_LABEL = new PowerLabelWrapper(48, BACK_WRAP);

    private static int ANIM_TIME = 50;


    private static String MOD_ID = References.ID;
    private static String RECIPE_GROUP = "fuel_reprocessor";

    public static ItemStack INPUT_MACHINE = new ItemStack(nuclearscience.DeferredRegisters.blockFuelReprocessor);

    public static ResourceLocation UID = new ResourceLocation(MOD_ID, RECIPE_GROUP);

    public FuelReprocessorRecipeCategory(IGuiHelper guiHelper) {
    	super(guiHelper, MOD_ID, RECIPE_GROUP, INPUT_MACHINE, BACK_WRAP, ANIM_TIME);
    	ElectrodynamicsJEIPlugin.addO2OClickArea(UID);
    	setInputSlots(guiHelper, INPUT_SLOT);
    	setOutputSlots(guiHelper, OUTPUT_SLOT);
    	setAnimatedArrows(guiHelper, ANIM_ARROW);
    	setLabels(POWER_LABEL);
    }

    @Override
    public ResourceLocation getUid() {
	return UID;
    }
}
