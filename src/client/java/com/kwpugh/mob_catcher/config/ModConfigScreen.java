package com.kwpugh.mob_catcher.config;

import com.kwpugh.mob_catcher.MobCatcher;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModConfigScreen {
    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("item.mob_catcher.mob_catcher"))
                .setSavingRunnable(MobCatcher.CONFIG::saveConfigFile);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory settings = builder.getOrCreateCategory(Text.literal("dummy"));
        settings.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.mob_catcher.option.enableDatapackMobTypes"), MobCatcher.CONFIG.enableDatapackMobTypes)
                .setTooltip(
                        Text.translatable("config.mob_catcher.option.enableDatapackMobTypes.@Tooltip[0]"),
                        Text.translatable("config.mob_catcher.option.enableDatapackMobTypes.@Tooltip[1]"))
                .setSaveConsumer(newValue -> MobCatcher.CONFIG.enableDatapackMobTypes = newValue)
                .build());

        return builder.build();
    }
}
