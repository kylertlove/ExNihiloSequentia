package com.novamachina.exnihilosequentia.common.setup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.novamachina.exnihilosequentia.common.item.tools.crook.CrookDrops;
import com.novamachina.exnihilosequentia.common.item.tools.hammer.HammerDrops;
import com.novamachina.exnihilosequentia.common.json.BarrelRegistriesJson;
import com.novamachina.exnihilosequentia.common.json.CrucibleRegistriesJson;
import com.novamachina.exnihilosequentia.common.tileentity.barrel.BarrelModeRegistry;
import com.novamachina.exnihilosequentia.common.tileentity.barrel.compost.CompostRegistry;
import com.novamachina.exnihilosequentia.common.tileentity.barrel.fluid.FluidBlockTransformRegistry;
import com.novamachina.exnihilosequentia.common.tileentity.barrel.fluid.FluidOnTopRegistry;
import com.novamachina.exnihilosequentia.common.tileentity.barrel.transform.FluidTransformRegistry;
import com.novamachina.exnihilosequentia.common.tileentity.crucible.FiredCrucibleMeltableItems;
import com.novamachina.exnihilosequentia.common.tileentity.crucible.HeatRegistry;
import com.novamachina.exnihilosequentia.common.tileentity.crucible.WoodCrucibleMeltableItems;
import com.novamachina.exnihilosequentia.common.tileentity.sieve.SieveDrops;
import com.novamachina.exnihilosequentia.common.utility.Config;
import com.novamachina.exnihilosequentia.common.utility.Constants;
import com.novamachina.exnihilosequentia.common.utility.Constants.ModInfo;
import com.novamachina.exnihilosequentia.common.utility.LogUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ObjectHolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.ModInfo.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModInitialization {

    public static final ItemGroup ITEM_GROUP = new ItemGroup(Constants.ModInfo.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.SIEVE.get());
        }
    };

    @ObjectHolder(ModInfo.MOD_ID + ":use_hammer")
    public static GlobalLootModifierSerializer<?> HAMMER_MODIFIER = null;

    public static void init(IEventBus modEventBus) {
        ModBlocks.init(modEventBus);
        ModItems.init(modEventBus);
        ModTiles.init(modEventBus);
        ModFluids.init(modEventBus);
    }

    @SubscribeEvent
    public static void setupNonTagBasedRegistries(FMLCommonSetupEvent event) {
        BarrelModeRegistry.initialize();
    }

    @SubscribeEvent
    public static void setupTagBasedRegistries(FMLServerStartingEvent event) {
        if(Config.USE_JSON_REGISTRIES.get()) {
            ModRegistries.BUS.useJson();
        } else {
            ModRegistries.BUS.useDefault();
        }
    }

    @SubscribeEvent
    public static void clearRegistriesOnServerExit(FMLServerStoppedEvent event) {
        ModRegistries.BUS.clearRegistries();
    }
}
