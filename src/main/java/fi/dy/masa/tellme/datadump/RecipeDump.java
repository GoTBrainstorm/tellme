package fi.dy.masa.tellme.datadump;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RecipeDump
{
    public static String getJsonRecipeDump()
    {
        var recipeManager = Minecraft.getInstance().level.getRecipeManager();

        // Grab all recipes, grouped by serializer
        var groupedRecipes = recipeManager
                .getRecipes()
                .stream()
                .collect(Collectors
                        .groupingBy(r -> r.getSerializer().getRegistryName().toString(), Collectors.toSet()));

        JsonObject jsonRoot = new JsonObject();
        for (String serializerName : new TreeSet<>(groupedRecipes.keySet())) {
            var recipes = groupedRecipes.get(serializerName);
            var jsonRecipeArray = new JsonArray();
            recipes.stream().sorted(Comparator.comparing(Recipe::getId)).forEachOrdered(r -> {
                jsonRecipeArray.add(dumpRecipe(r));
            });
            jsonRoot.add(serializerName, jsonRecipeArray);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonRoot);
    }

    public static JsonObject dumpRecipe(Recipe<?> r)
    {
        // Basic properties so it'll always appear
        JsonObject jsonRecipe = new JsonObject();
        jsonRecipe.addProperty("name", r.getId().toString());
        jsonRecipe.addProperty("type", r.getType().toString());

        try
        {
            var recipeClass = r.getClass();

            jsonRecipe.add("_internals", dumpRecipeInternals(recipeClass));

            if (recipeClass.getName() == "net.minecraft.world.item.crafting.ShapedRecipe")
            {
                EnrichForShapedRecipe(r, recipeClass, jsonRecipe);
            }
            else if (recipeClass.getName() == "net.minecraft.world.item.crafting.ShapelessRecipe")
            {
                EnrichForShapelessRecipe(r, recipeClass, jsonRecipe);
            }
        }
        catch (Exception ex)
        {
            jsonRecipe.addProperty("_error", ex.toString());
        }

        return jsonRecipe;
    }

    public static JsonObject dumpRecipeInternals(Class<? extends Recipe> c) throws Exception
    {
        var desc = new JsonObject();
        desc.addProperty("className", c.getName());
        return desc;
    }

    public static void EnrichForShapelessRecipe(Recipe<?> r, Class<? extends Recipe> c, JsonObject jsonRecipe) throws Exception {
        jsonRecipe.add("recipeItems", ParseIngredientList((List<Ingredient>) c.getMethod("getIngredients").invoke(r)));
        jsonRecipe.add("result", ParseItemStack((ItemStack) c.getMethod("getResultItem").invoke(r)));
        jsonRecipe.addProperty("group", (String) c.getMethod("getGroup").invoke(r));
    }

    public static void EnrichForShapedRecipe(Recipe<?> r, Class<? extends Recipe> c, JsonObject jsonRecipe) throws Exception {
        jsonRecipe.addProperty("width", (int) c.getMethod("getRecipeWidth").invoke(r));
        jsonRecipe.addProperty("height", (int) c.getMethod("getRecipeHeight").invoke(r));
        jsonRecipe.add("recipeItems", ParseIngredientList((List<Ingredient>) c.getMethod("getIngredients").invoke(r)));
        jsonRecipe.add("result", ParseItemStack((ItemStack) c.getMethod("getResultItem").invoke(r)));
        jsonRecipe.addProperty("group", (String) c.getMethod("getGroup").invoke(r));
    }

    public static JsonElement ParseIngredientList(List<Ingredient> list) throws Exception
    {
        var jsonIngredients = new JsonArray();
        for (int i = 0; i < list.size(); i++) {
            jsonIngredients.add(list.get(i).toJson());
        }
        return jsonIngredients;
    }

    public static JsonElement ParseItemStack(ItemStack stack) throws Exception
    {
        JsonObject json = new JsonObject();
        json.addProperty("count", stack.getCount());
        json.addProperty("item", stack.getItem().getRegistryName().toString());
        return json;
    }

    public class RecipeDumpDescriptor
    {

    }
}