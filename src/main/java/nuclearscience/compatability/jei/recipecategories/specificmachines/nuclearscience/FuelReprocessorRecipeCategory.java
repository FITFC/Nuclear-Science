package nuclearscience.compatability.jei.recipecategories.specificmachines.nuclearscience;

import java.util.ArrayList;
import java.util.Arrays;

import electrodynamics.api.References;
import electrodynamics.compatability.jei.recipecategories.O2ORecipeCategory;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class FuelReprocessorRecipeCategory extends O2ORecipeCategory {

    // JEI Window Parameters
    private static int[] GUI_BACKGROUND = { 0, 0, 132, 58 };
    private static int[] PROCESSING_ARROW_LOCATION = { 132, 0, 24, 17 };

    private static int[] INPUT_ITEM_OFFSET = { 22, 21 };
    private static int[] OUTPUT_ITEM_OFFSET = { 82, 22 };
    private static int[] PROCESSING_ARROW_OFFSET = { 46, 23 };

    private static int SMELT_TIME = 50;
    private static int TEXT_Y_HEIGHT = 48;

    private static String MOD_ID = References.ID;
    private static String RECIPE_GROUP = "fuel_reprocessor";
    private static String GUI_TEXTURE = "textures/gui/jei/o2o_recipe_gui.png";

    public static ItemStack INPUT_MACHINE = new ItemStack(nuclearscience.DeferredRegisters.blockFuelReprocessor);

    private static IDrawableAnimated.StartDirection ARROW_START_DIRECTION = IDrawableAnimated.StartDirection.LEFT;

    public static ResourceLocation UID = new ResourceLocation(MOD_ID, RECIPE_GROUP);

    private static ArrayList<int[]> INPUT_COORDINATES = new ArrayList<>(
	    Arrays.asList(GUI_BACKGROUND, PROCESSING_ARROW_LOCATION, INPUT_ITEM_OFFSET, OUTPUT_ITEM_OFFSET, PROCESSING_ARROW_OFFSET));

    public FuelReprocessorRecipeCategory(IGuiHelper guiHelper) {
	super(guiHelper, MOD_ID, RECIPE_GROUP, GUI_TEXTURE, INPUT_MACHINE, INPUT_COORDINATES, SMELT_TIME, TEXT_Y_HEIGHT, ARROW_START_DIRECTION);
    }

    @Override
    public ResourceLocation getUid() {
	return UID;
    }
}
