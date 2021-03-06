package novamachina.exnihilosequentia.common.compat.jei.sieve.dry;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import novamachina.exnihilosequentia.api.ExNihiloRegistries;
import novamachina.exnihilosequentia.api.compat.jei.JEISieveRecipe;
import novamachina.exnihilosequentia.api.crafting.sieve.MeshWithChance;
import novamachina.exnihilosequentia.api.crafting.sieve.SieveRecipe;
import novamachina.exnihilosequentia.common.item.mesh.MeshItem;
import novamachina.exnihilosequentia.common.utility.ExNihiloConstants;
import novamachina.exnihilosequentia.common.utility.StringUtils;

public class DrySieveRecipeCategory implements IRecipeCategory<JEISieveRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA, "dry_sieve");
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA, "textures/gui/jei_mid.png");

    private final IDrawableStatic background;
    private final IDrawableStatic slotHighlight;

    public DrySieveRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 58);
        this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public Class<? extends JEISieveRecipe> getRecipeClass() {
        return JEISieveRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Sieve";
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void setIngredients(JEISieveRecipe recipe, IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, recipe.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getResults());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, JEISieveRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 10, 38);
        recipeLayout.getItemStacks().set(0, recipe.getMesh());

        recipeLayout.getItemStacks().init(1, true, 10, 2);
        recipeLayout.getItemStacks().set(1, recipe.getSieveables());

        IFocus<?> focus = recipeLayout.getFocus();

        int slotIndex = 2;
        for (int i = 0; i < recipe.getResults().size(); i++) {
            final int slotX = 38 + (i % 7 * 18);
            final int slotY = 2 + i / 7 * 18;

            ItemStack outputStack = recipe.getResults().get(i);
            recipeLayout.getItemStacks().init(slotIndex + i, false, slotX, slotY);
            recipeLayout.getItemStacks().set(slotIndex + i, outputStack);

            if (focus != null) {
                ItemStack focusStack = (ItemStack) focus.getValue();
                if (focus.getMode() == IFocus.Mode.OUTPUT
                        && !focusStack.isEmpty()
                        && focusStack.getItem() == outputStack.getItem()
                        && focusStack.getDamage() == outputStack.getDamage()) {
                    recipeLayout.getItemStacks().setBackground(slotIndex + i, slotHighlight);
                }
            }
        }

        recipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<ITextComponent> tooltip) {
                if (!input) {
                    Multiset<String> condensedTooltips = HashMultiset.create();
                    List<SieveRecipe> drops = ExNihiloRegistries.SIEVE_REGISTRY
                            .getDrops(recipe.getInputs().get(1).get(0).getItem(), ((MeshItem) recipe
                                    .getInputs().get(0).get(0).getItem()).getMesh(), false);
                    for (SieveRecipe entry : drops) {
                        ItemStack drop = entry.getDrop();
                        if (!drop.isItemEqual(ingredient)) {
                            continue;
                        }
                        for (MeshWithChance meshWithChance : entry.getRolls()) {
                            condensedTooltips.add(StringUtils.formatPercent(meshWithChance.getChance()));
                        }
                    }
                    tooltip.add(new TranslationTextComponent("jei.sieve.dropChance"));
                    for (String line : condensedTooltips.elementSet()) {
                        tooltip.add(new StringTextComponent(" * " + condensedTooltips.count(line) + "x " + line));
                    }
                }
            }
        });
    }
}
