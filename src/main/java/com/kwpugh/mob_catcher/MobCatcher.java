package com.kwpugh.mob_catcher;

import com.kwpugh.mob_catcher.config.ModConfig;
import com.kwpugh.mob_catcher.events.EntityInteractEvent;
import com.kwpugh.mob_catcher.init.ItemInit;
import com.kwpugh.mob_catcher.init.TagInit;
import com.kwpugh.mob_catcher.util.MobCatcherGroup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobCatcher implements ModInitializer {
    public static final String MOD_ID = "mob_catcher";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ModConfig CONFIG;

    @Override
    public void onInitialize() {
        CONFIG = ModConfig.loadConfigFile();

        TagInit.register();
        ItemInit.register();
        MobCatcherGroup.addGroup();
        UseEntityCallback.EVENT.register(EntityInteractEvent::onUseEntity);
    }
}