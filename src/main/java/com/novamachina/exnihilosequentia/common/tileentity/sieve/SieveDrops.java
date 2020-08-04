package com.novamachina.exnihilosequentia.common.tileentity.sieve;

import com.novamachina.exnihilosequentia.common.item.mesh.EnumMesh;
import com.novamachina.exnihilosequentia.common.item.ore.EnumOre;
import com.novamachina.exnihilosequentia.common.item.resources.EnumResource;
import com.novamachina.exnihilosequentia.common.item.seeds.EnumSeed;
import com.novamachina.exnihilosequentia.common.setup.ModBlocks;
import com.novamachina.exnihilosequentia.common.setup.ModItems;
import com.novamachina.exnihilosequentia.common.utility.Constants;
import com.novamachina.exnihilosequentia.common.utility.LogUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class SieveDrops {

    private static final Map<ResourceLocation, List<SieveDropEntry>> stringMeshMap = new HashMap<>();
    private static final Map<ResourceLocation, List<SieveDropEntry>> flintMeshMap = new HashMap<>();
    private static final Map<ResourceLocation, List<SieveDropEntry>> ironMeshMap = new HashMap<>();
    private static final Map<ResourceLocation, List<SieveDropEntry>> diamondMeshMap = new HashMap<>();

    public static void addDrop(Block input, Item result, float rarity, EnumMesh meshType) {
        addDrop(input.getRegistryName(), result.getRegistryName(), rarity, meshType);
    }

    public static void addDrop(ResourceLocation input, ResourceLocation result, float rarity, EnumMesh meshType) {
        switch (meshType) {
            case STRING:
                insertIntoMap(stringMeshMap, input, result, rarity);
                break;
            case FLINT:
                insertIntoMap(flintMeshMap, input, result, rarity);
                break;
            case IRON:
                insertIntoMap(ironMeshMap, input, result, rarity);
                break;
            case DIAMOND:
                insertIntoMap(diamondMeshMap, input, result, rarity);
                break;
            default:
                LogUtil.warn(String.format("Mesh type \"%s\" does not exist.", meshType.getName()));
                break;
        }
    }

    private static void insertIntoMap(Map<ResourceLocation, List<SieveDropEntry>> map, ResourceLocation input, ResourceLocation result, float rarity) {
        if (map.containsKey(input)) {
            map.get(input).add(new SieveDropEntry(result, rarity));
        } else {
            List<SieveDropEntry> list = new ArrayList<>();
            list.add(new SieveDropEntry(result, rarity));
            map.put(input, list);
        }
    }

    public static List<Item> getDrops(Block input, EnumMesh meshType) {
        List<Item> returnList = new ArrayList<>();
        switch (meshType) {
            case STRING:
                returnList.addAll(retrieveFromMap(stringMeshMap, input.getRegistryName()));
                break;
            case FLINT:
                returnList.addAll(retrieveFromMap(flintMeshMap, input.getRegistryName()));
                break;
            case IRON:
                returnList.addAll(retrieveFromMap(ironMeshMap, input.getRegistryName()));
                break;
            case DIAMOND:
                returnList.addAll(retrieveFromMap(diamondMeshMap, input.getRegistryName()));
                break;
            default:
                LogUtil.warn(String.format("Mesh type \"%s\" does not exist.", meshType.getName()));
                break;
        }
        return returnList;
    }

    //TODO: Add lapis, cocoa, bonemeal (these are the dyes that are missing)
    public static void initialize() {
        // Stone Pebble
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_STONE).get(), 1.0F, EnumMesh.STRING);
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_STONE).get(), 1.0F, EnumMesh.STRING);
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_STONE).get(), 0.5F, EnumMesh.STRING);
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_STONE).get(), 0.5F, EnumMesh.STRING);
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_STONE).get(), 0.1F, EnumMesh.STRING);
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_STONE).get(), 0.1F, EnumMesh.STRING);

        // Andesite Pebble
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_ANDESITE).get(), 0.5F, EnumMesh.STRING);
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_ANDESITE).get(), 0.1F, EnumMesh.STRING);

        // Diorite Pebble
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_DIORITE).get(), 0.5F, EnumMesh.STRING);
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_DIORITE).get(), 0.1F, EnumMesh.STRING);

        // Granite Pebble
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_GRANITE).get(), 0.5F, EnumMesh.STRING);
        addDrop(Blocks.DIRT,
            ModItems.pebbleMap.get(Constants.Items.PEBBLE_GRANITE).get(), 0.1F, EnumMesh.STRING);

        // Vanilla Seeds
        addDrop(Blocks.DIRT, Items.WHEAT_SEEDS, 0.7F,
            EnumMesh.STRING);
        addDrop(Blocks.DIRT, Items.MELON_SEEDS, 0.35F,
            EnumMesh.STRING);
        addDrop(Blocks.DIRT, Items.PUMPKIN_SEEDS, 0.35F,
            EnumMesh.STRING);

        // Ancient Spores
        addDrop(Blocks.DIRT,
            ModItems.resourceMap.get(EnumResource.ANCIENT_SPORE.getResourceName()).get(), 0.05F,
            EnumMesh.STRING);

        // Grass Seeds
        addDrop(Blocks.DIRT,
            ModItems.resourceMap.get(EnumResource.GRASS_SEED.getResourceName()).get(), 0.05F,
            EnumMesh.STRING);

        // Misc Vanilla Drops
        addDrop(Blocks.SAND, Items.COCOA_BEANS, 0.03F, EnumMesh.STRING);
        addDrop(Blocks.SAND, Items.PRISMARINE_SHARD, 0.02F,
            EnumMesh.DIAMOND);

        // Flint
        addDrop(Blocks.GRAVEL, Items.FLINT, 0.25F, EnumMesh.STRING);
        addDrop(Blocks.GRAVEL, Items.FLINT, 0.25F, EnumMesh.FLINT);

        // Coal
        addDrop(Blocks.GRAVEL, Items.COAL, 0.125F, EnumMesh.FLINT);

        addDrop(Blocks.GRAVEL, Items.LAPIS_LAZULI, 0.05F, EnumMesh.FLINT);

        // Diamond
        addDrop(Blocks.GRAVEL, Items.DIAMOND, 0.008F,
            EnumMesh.IRON);
        addDrop(Blocks.GRAVEL, Items.DIAMOND, 0.016F,
            EnumMesh.DIAMOND);

        // Emerald
        addDrop(Blocks.GRAVEL, Items.EMERALD, 0.008F,
            EnumMesh.IRON);
        addDrop(Blocks.GRAVEL, Items.EMERALD, 0.016F,
            EnumMesh.DIAMOND);

        // Quartz
        addDrop(Blocks.SOUL_SAND, Items.QUARTZ, 1.0F,
            EnumMesh.FLINT);
        addDrop(Blocks.SOUL_SAND, Items.QUARTZ, 0.33F,
            EnumMesh.FLINT);
        addDrop(Blocks.SOUL_SAND, Items.QUARTZ, 1.0F,
            EnumMesh.DIAMOND);
        addDrop(Blocks.SOUL_SAND, Items.QUARTZ, 0.8F,
            EnumMesh.DIAMOND);

        // Nether Wart
        addDrop(Blocks.SOUL_SAND, Items.NETHER_WART, 0.1F,
            EnumMesh.STRING);

        // Ghast Tear
        addDrop(Blocks.SOUL_SAND, Items.GHAST_TEAR, 0.02F,
            EnumMesh.DIAMOND);

        addDrop(ModBlocks.DUST.get(), Items.BONE_MEAL, 0.2F, EnumMesh.STRING);

        // Gunpowder
        addDrop(ModBlocks.DUST.get(), Items.GUNPOWDER, 0.07F,
            EnumMesh.STRING);

        // Redstone
        addDrop(ModBlocks.DUST.get(), Items.REDSTONE, 0.125F,
            EnumMesh.IRON);
        addDrop(ModBlocks.DUST.get(), Items.REDSTONE, 0.25F,
            EnumMesh.DIAMOND);

        // Glowstone
        addDrop(ModBlocks.DUST.get(), Items.GLOWSTONE_DUST,
            0.0625F, EnumMesh.IRON);

        // Blaze Powder
        addDrop(ModBlocks.DUST.get(), Items.BLAZE_POWDER,
            0.05F, EnumMesh.IRON);

        // Ores
        for (EnumOre ore : EnumOre.values()) {
            switch (ore) {
                case IRON: {
                    addMultiMeshDrop(Blocks.GRAVEL,
                        ModItems.pieceMap.get(ore.getName()).get(), null, 0.1F, 0.15F, 0.25F);
                    addDrop(Blocks.SAND, ModItems.pieceMap.get(ore.getName()).get(),
                        0.5F, EnumMesh.DIAMOND);
                    break;
                }
                case GOLD: {
                    addMultiMeshDrop(
                        ModBlocks.CRUSHED_NETHERRACK.get(),
                        ModItems.pieceMap.get(ore.getName()).get(), null, 0.25F, 0.25F, 0.4F);
                    addMultiMeshDrop(Blocks.GRAVEL,
                        ModItems.pieceMap.get(ore.getName()).get(), null, 0.05F, 0.075F, 0.15F);
                    break;
                }
            }
        }

        // Seeds
        for (EnumSeed seed : EnumSeed.values()) {
            addDrop(Blocks.DIRT,
                ModItems.seedMap.get(seed.getSeedName()).get(), 0.05F, EnumMesh.STRING);
        }

        getLeavesSaplings().forEach((input, drop) -> {
            LeavesBlock leavesBlock = (LeavesBlock) input;
            if (leavesBlock.getRegistryName().equals(new ResourceLocation("jungle_leaves"))) {
                addMultiMeshDrop(leavesBlock, drop, 0.025F, 0.05F, 0.075F, 0.1F);
            } else {
                addMultiMeshDrop(leavesBlock, drop, 0.05F, 0.10F, 0.15F, 0.2F);
            }

            // Apple
            addMultiMeshDrop(leavesBlock, Items.APPLE, 0.05F, 0.10F,
                0.15F, 0.20F);

            // Golden Apple
            addMultiMeshDrop(leavesBlock, Items.GOLDEN_APPLE, 0.001F,
                0.003F, 0.005F, 0.01F);

            // Silk Worm
            addMultiMeshDrop(leavesBlock,
                ModItems.resourceMap.get(Constants.Items.SILKWORM).get(), 0.025F, 0.05F, 0.1F,
                0.2F);
        });
    }

    private static Map<Block, Item> getLeavesSaplings() {
        Map<Block, Item> saplingMap = new HashMap<>();
        saplingMap.put(Blocks.ACACIA_LEAVES, Items.ACACIA_SAPLING);
        saplingMap.put(Blocks.BIRCH_LEAVES, Items.BIRCH_SAPLING);
        saplingMap.put(Blocks.DARK_OAK_LEAVES, Items.DARK_OAK_SAPLING);
        saplingMap.put(Blocks.JUNGLE_LEAVES, Items.JUNGLE_SAPLING);
        saplingMap.put(Blocks.OAK_LEAVES, Items.OAK_SAPLING);
        saplingMap.put(Blocks.SPRUCE_LEAVES, Items.SPRUCE_SAPLING);

        return saplingMap;
    }

    private static void addMultiMeshDrop(Block input, Item result, Float stringRarity,
                                         Float flintRarity, Float ironRarity, Float diamondRarity) {
        addMultiMeshDrop(input.getRegistryName(), result.getRegistryName(), stringRarity, flintRarity,ironRarity, diamondRarity);
    }

    private static void addMultiMeshDrop(ResourceLocation input, ResourceLocation result, Float stringRarity,
                                         Float flintRarity, Float ironRarity, Float diamondRarity) {
        if (stringRarity != null) {
            addDrop(input, result, stringRarity, EnumMesh.STRING);
        }
        if (flintRarity != null) {
            addDrop(input, result, flintRarity, EnumMesh.FLINT);
        }
        if (ironRarity != null) {
            addDrop(input, result, ironRarity, EnumMesh.IRON);
        }
        if (diamondRarity != null) {
            addDrop(input, result, diamondRarity, EnumMesh.DIAMOND);
        }
    }

    private static Collection<? extends Item> retrieveFromMap(
        Map<ResourceLocation, List<SieveDropEntry>> dropsMap, ResourceLocation input) {
        List<Item> returnList = new ArrayList<>();
        Random random = new Random();
        if (!dropsMap.containsKey(input)) {
            return returnList;
        }
        for (SieveDropEntry entry : dropsMap.get(input)) {
            if (random.nextFloat() <= entry.getRarity()) {
                returnList.add(ForgeRegistries.ITEMS.getValue(entry.getResult()));
            }
        }
        return returnList;
    }

    public static boolean isBlockSiftable(Block block, EnumMesh mesh) {
        Objects.requireNonNull(block.getRegistryName());

        switch (mesh) {
            case STRING: {
                return stringMeshMap.containsKey(block.getRegistryName());
            }
            case FLINT: {
                return flintMeshMap.containsKey(block.getRegistryName());
            }
            case IRON: {
                return ironMeshMap.containsKey(block.getRegistryName());
            }
            case DIAMOND: {
                return diamondMeshMap.containsKey(block.getRegistryName());
            }
            default: {
                return false;
            }
        }
    }

    public static Map<String, List<SieveJson>> toJSONReady() {
        Map<String, List<SieveJson>> jsonMap = new HashMap<>();
        for(Map.Entry<ResourceLocation, List<SieveDropEntry>> entry : stringMeshMap.entrySet()) {
            List<SieveJson> list = new ArrayList<>();
            for(SieveDropEntry dropEntry : entry.getValue()) {
                list.add(new SieveJson(dropEntry.getResult().toString(), dropEntry.getRarity(), EnumMesh.STRING));
            }
            jsonMap.put(entry.getKey().toString(), list);
        }

        for(Map.Entry<ResourceLocation, List<SieveDropEntry>> entry : flintMeshMap.entrySet()) {
            List<SieveJson> list = jsonMap.get(entry.getKey().toString());

            if(list == null) {
                list = new ArrayList<>();
            }

            for(SieveDropEntry dropEntry : entry.getValue()) {
                list.add(new SieveJson(dropEntry.getResult().toString(), dropEntry.getRarity(), EnumMesh.FLINT));
            }

            jsonMap.put(entry.getKey().toString(), list);
        }

        for(Map.Entry<ResourceLocation, List<SieveDropEntry>> entry : ironMeshMap.entrySet()) {
            List<SieveJson> list = jsonMap.get(entry.getKey().toString());

            if(list == null) {
                list = new ArrayList<>();
            }

            for(SieveDropEntry dropEntry : entry.getValue()) {
                list.add(new SieveJson(dropEntry.getResult().toString(), dropEntry.getRarity(), EnumMesh.IRON));
            }

            jsonMap.put(entry.getKey().toString(), list);
        }

        for(Map.Entry<ResourceLocation, List<SieveDropEntry>> entry : diamondMeshMap.entrySet()) {
            List<SieveJson> list = jsonMap.get(entry.getKey().toString());

            if(list == null) {
                list = new ArrayList<>();
            }

            for(SieveDropEntry dropEntry : entry.getValue()) {
                list.add(new SieveJson(dropEntry.getResult().toString(), dropEntry.getRarity(), EnumMesh.DIAMOND));
            }

            jsonMap.put(entry.getKey().toString(), list);
        }
        return jsonMap;
    }
}
