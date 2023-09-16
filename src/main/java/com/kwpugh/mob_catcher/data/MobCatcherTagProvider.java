package com.kwpugh.mob_catcher.data;

import com.kwpugh.mob_catcher.init.TagInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class MobCatcherTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    public MobCatcherTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        var passiveBuilder = getOrCreateTagBuilder(TagInit.MOBS_PASSIVE);
        var hostileBuilder = getOrCreateTagBuilder(TagInit.MOBS_HOSTILE);

        for (var entityType : Registries.ENTITY_TYPE) {
            switch (entityType.getSpawnGroup()) {
                case MONSTER -> {
                    if (entityType == EntityType.WITHER || entityType == EntityType.ENDER_DRAGON) {
                        break;
                    }
                    hostileBuilder.add(entityType);
                }
                case AMBIENT,
                    AXOLOTLS,
                    CREATURE,
                    UNDERGROUND_WATER_CREATURE,
                    WATER_AMBIENT,
                    WATER_CREATURE -> passiveBuilder.add(entityType);
            }
        }

        passiveBuilder.add(EntityType.IRON_GOLEM);
        passiveBuilder.add(EntityType.SNOW_GOLEM);
        passiveBuilder.add(EntityType.VILLAGER);

    }
}
