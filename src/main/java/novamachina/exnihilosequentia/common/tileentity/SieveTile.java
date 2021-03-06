package novamachina.exnihilosequentia.common.tileentity;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import novamachina.exnihilosequentia.api.ExNihiloRegistries;
import novamachina.exnihilosequentia.api.crafting.sieve.SieveRecipe;
import novamachina.exnihilosequentia.common.block.BlockSieve;
import novamachina.exnihilosequentia.common.init.ExNihiloTiles;
import novamachina.exnihilosequentia.common.item.mesh.EnumMesh;
import novamachina.exnihilosequentia.common.item.mesh.MeshItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import novamachina.exnihilosequentia.common.utility.Config;
import novamachina.exnihilosequentia.common.utility.ExNihiloLogger;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SieveTile extends TileEntity {
    private static final ExNihiloLogger logger = new ExNihiloLogger(LogManager.getLogger());
    private static final String BLOCK_TAG = "block";
    private static final String PROGRESS_TAG = "progress";
    private static final String MESH_TAG = "mesh";
    private final Random random = new Random();

    private ItemStack meshStack = ItemStack.EMPTY;
    private ItemStack blockStack = ItemStack.EMPTY;
    private EnumMesh meshType = EnumMesh.NONE;
    private float progress = 0;

    public SieveTile() {
        super(ExNihiloTiles.SIEVE.get());
    }

    public void insertMesh(ItemStack stack) {
        logger.debug("Insert Mesh: " + stack);
        EnumMesh mesh = ((MeshItem) stack.getItem()).getMesh();
        if (meshStack.isEmpty()) {
            meshStack = stack.copy();
            meshStack.setCount(1);
            stack.shrink(1);
            meshType = mesh;
            if (!isRemoved()) {
                setSieveState();
            }
            markDirty();
        }
    }

    public void removeMesh(boolean rerenderSieve) {
        logger.debug("Remove mesh: Rerender Sieve: " + rerenderSieve);
        if (!meshStack.isEmpty()) {
            world.addEntity(
                new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                    meshStack.copy()));
            meshStack = ItemStack.EMPTY;
            meshType = EnumMesh.NONE;
            if (rerenderSieve) {
                setSieveState();
            }
        }
    }

    public void setSieveState() {
        logger.debug("Set Sieve State, Mesh: " + meshType);
        BlockState state = getBlockState();
        if (state.getBlock() instanceof BlockSieve) {
            world.setBlockState(getPos(), state.with(BlockSieve.MESH, meshType));
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        if (compound.contains(MESH_TAG)) {
            meshStack = ItemStack.read((CompoundNBT) compound.get(MESH_TAG));
            if (meshStack.getItem() instanceof MeshItem) {
                meshType = ((MeshItem) meshStack.getItem()).getMesh();
            }
        } else {
            meshStack = ItemStack.EMPTY;
        }

        if (compound.contains(BLOCK_TAG)) {
            blockStack = ItemStack.read((CompoundNBT) compound.get(BLOCK_TAG));
        } else {
            blockStack = ItemStack.EMPTY;
        }

        progress = compound.getFloat(PROGRESS_TAG);

        super.read(state, compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (!meshStack.isEmpty()) {
            CompoundNBT meshNBT = meshStack.write(new CompoundNBT());
            compound.put(MESH_TAG, meshNBT);
        }

        if (!blockStack.isEmpty()) {
            CompoundNBT blockNBT = blockStack.write(new CompoundNBT());
            compound.put(BLOCK_TAG, blockNBT);
        }

        compound.putFloat(PROGRESS_TAG, progress);

        return super.write(compound);
    }

    @Override
    public void remove() {
        if (!world.isRemote()) {
            removeMesh(false);
        }
        super.remove();
    }

    public void insertSiftableBlock(ItemStack stack) {
        logger.debug("Insert Siftable Block: " + stack);
        if (!meshStack.isEmpty() && blockStack.isEmpty()) {
            blockStack = stack.copy();
            blockStack.setCount(1);
            stack.shrink(1);
        }
    }

    public void activateSieve(boolean isWaterlogged) {
        logger.debug("Activate Sieve, isWaterlogged: " + isWaterlogged);
        if (isReadyToSieve()) {
            progress += 0.1F;

            if (progress >= 1.0F) {
                logger.debug("Sieve progress complete");
                List<SieveRecipe> drops = ExNihiloRegistries.SIEVE_REGISTRY
                    .getDrops(((BlockItem) blockStack.getItem()).getBlock(), meshType, isWaterlogged);
                drops.forEach((entry -> entry.getRolls().forEach(meshWithChance -> {
                    if (random.nextFloat() <= meshWithChance.getChance()) {
                        logger.debug("Spawning Item: " + entry.getDrop());
                        world.addEntity(new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 1.1F, pos
                            .getZ() + 0.5F, entry.getDrop()));
                    }
                })));
                resetSieve();
            }
        }
    }

    private void resetSieve() {
        logger.debug("Resetting sieve");
        if (Config.enableMeshDurability()) {
            logger.debug("Damaging mesh");
            meshStack.damageItem(1, new FakePlayer((ServerWorld) world, new GameProfile(UUID
                .randomUUID(), "Fake Player")), player -> logger.debug("Broken"));
        }
        blockStack = ItemStack.EMPTY;
        progress = 0.0F;
        if (meshStack.isEmpty()) {
            logger.debug("Setting mesh to none, potential broken mesh");
            meshType = EnumMesh.NONE;
            setSieveState();
        }
    }

    public boolean isReadyToSieve() {
        return !meshStack.isEmpty() && !blockStack.isEmpty();
    }

    public ResourceLocation getTexture() {
        if (!blockStack.isEmpty()) {
            return blockStack.getItem().getRegistryName();
        }
        return null;
    }

    public ItemStack getBlockStack() {
        return blockStack;
    }

    public float getProgress() {
        return progress;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        if (!meshStack.isEmpty()) {
            CompoundNBT meshNBT = meshStack.write(new CompoundNBT());
            nbt.put(MESH_TAG, meshNBT);
        }

        if (!blockStack.isEmpty()) {
            CompoundNBT blockNbt = blockStack.write(new CompoundNBT());
            nbt.put(BLOCK_TAG, blockNbt);
        }
        nbt.putFloat(PROGRESS_TAG, progress);

        return new SUpdateTileEntityPacket(getPos(), -1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        CompoundNBT nbt = packet.getNbtCompound();
        if (nbt.contains(MESH_TAG)) {
            meshStack = ItemStack.read((CompoundNBT) nbt.get(MESH_TAG));
            if (meshStack.getItem() instanceof MeshItem) {
                meshType = ((MeshItem) meshStack.getItem()).getMesh();
            }
        } else {
            meshStack = ItemStack.EMPTY;
        }

        if (nbt.contains(BLOCK_TAG)) {
            blockStack = ItemStack.read((CompoundNBT) nbt.get(BLOCK_TAG));
        } else {
            blockStack = ItemStack.EMPTY;
        }
        progress = nbt.getFloat(PROGRESS_TAG);
    }

    public EnumMesh getMesh() {
        return meshType;
    }
}
