package com.kwpugh.mob_catcher.util;

import com.kwpugh.mob_catcher.MobCatcher;
import com.kwpugh.mob_catcher.init.ItemInit;
import com.kwpugh.mob_catcher.init.TagInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class CatcherUtil {
    public static boolean catchMob(PlayerEntity player, Entity entity, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (MobCatcher.CONFIG.enableDatapackMobTypes) {
            TagKey<EntityType<?>> tag = (stack.getItem() == ItemInit.MOB_CATCHER ? TagInit.MOBS_PASSIVE : (stack.getItem() == ItemInit.MOB_CATCHER_HOSTILE ? TagInit.MOBS_HOSTILE : null));
            if (tag == null) {
                return false;
            }

            // Datapack tag-based checking
            if (stack.getOrCreateSubNbt("captured_entity").isEmpty() && entity.getType().isIn(tag)) {
                if(saveEntityToStack(entity, stack)) {
                    player.setStackInHand(hand, stack);
                }

                return true;
            }
        } else if (stack.getItem() == ItemInit.MOB_CATCHER) {
            // Traditional hard-coded logic
            if (stack.getOrCreateSubNbt("captured_entity").isEmpty() && (entity instanceof AmbientEntity || entity instanceof PathAwareEntity)) {
                if(saveEntityToStack(entity, stack)) {
                    player.setStackInHand(hand, stack);
                }

                return true;
            }
        } else if (stack.getItem() == ItemInit.MOB_CATCHER_HOSTILE) {
            // Traditional hard-coded logic for hostile only
            if (stack.getOrCreateSubNbt("captured_entity").isEmpty() && !(entity instanceof WitherEntity) && (entity instanceof HostileEntity || entity instanceof SlimeEntity || entity instanceof GhastEntity || entity instanceof PhantomEntity)) {
                if(saveEntityToStack(entity, stack)) {
                    player.setStackInHand(hand, stack);
                }

                return true;
            }
        }

        return false;
    }

    // Method to save an entity to a tag and remove entity from world
    public static boolean saveEntityToStack(Entity entity, ItemStack stack) {
        NbtCompound entityTag = new NbtCompound();
        if(!entity.saveSelfNbt(entityTag)) {
            return false;
        }

        stack.getOrCreateNbt().put("captured_entity", entityTag);
        stack.getOrCreateNbt().putString("name", entity.getDisplayName().getString());
        entity.discard();

        return true;
    }

    public static void respawnEntity(ItemUsageContext context, ItemStack stack) {
        ServerWorld serverWorld = (ServerWorld) context.getWorld();
        BlockPos pos = context.getBlockPos().offset(context.getSide());
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();

        NbtCompound entityTag = context.getStack().getSubNbt("captured_entity");   // KEEP

        Optional<Entity> entity = EntityType.getEntityFromNbt(entityTag, serverWorld);
        if(entity.isPresent())
        {
            Entity entity2 = entity.get();
            entity2.updatePositionAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, player.getYaw(), player.getPitch());
            serverWorld.spawnEntity(entity2);
        }

        stack.removeSubNbt("name");  //KEEP
        stack.removeSubNbt("captured_entity");  // KEEP

        //context.getPlayer().getStackInHand(context.getHand());
    }
}
