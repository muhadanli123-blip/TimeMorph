package com.example.timemorph;

import net.fabricmc.api.ClientModInitializer;

public class TimeMorphClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TimeMorphConfig.load();
    }
}
