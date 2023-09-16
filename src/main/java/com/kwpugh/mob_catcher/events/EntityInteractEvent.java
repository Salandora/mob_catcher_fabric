package com.kwpugh.mob_catcher.events;

import com.kwpugh.mob_catcher.util.CatcherUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class EntityInteractEvent {
    public static ActionResult onUseEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (CatcherUtil.catchMob(player, entity, hand)) {
            player.swingHand(hand);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}