package novamachina.exnihilosequentia.common.crafting.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import novamachina.exnihilosequentia.api.crafting.ItemStackWithChance;
import novamachina.exnihilosequentia.api.crafting.RecipeSerializer;
import novamachina.exnihilosequentia.api.crafting.hammer.HammerRecipe;
import novamachina.exnihilosequentia.common.item.tools.hammer.EnumHammer;

public class HammerRecipeSerializer extends RecipeSerializer<HammerRecipe> {
    @Override
    public ItemStack getIcon() {
        return new ItemStack(EnumHammer.DIAMOND.getRegistryObject().get());
    }

    @Override
    public HammerRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient input = Ingredient.read(buffer);
        int outputCount = buffer.readInt();
        List<ItemStackWithChance> output = new ArrayList<>(outputCount);
        for (int i = 0; i < outputCount; i++) {
            output.add(ItemStackWithChance.read(buffer));
        }

        return new HammerRecipe(recipeId, input, output);
    }

    @Override
    public void write(PacketBuffer buffer, HammerRecipe recipe) {
        recipe.getInput().write(buffer);
        buffer.writeInt(recipe.getOutput().size());
        for (ItemStackWithChance stack : recipe.getOutput()) {
            stack.write(buffer);
        }
    }

    @Override
    protected HammerRecipe readFromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient input = Ingredient.deserialize(json.get("input"));
        JsonArray results = json.getAsJsonArray("results");
        List<ItemStackWithChance> output = new ArrayList<>(results.size());
        for (int i = 0; i < results.size(); i++) {
            output.add(ItemStackWithChance.deserialize(results.get(i)));
        }
        return new HammerRecipe(recipeId, input, output);
    }
}
