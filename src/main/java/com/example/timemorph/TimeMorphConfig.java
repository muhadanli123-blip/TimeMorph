package com.example.timemorph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

/** config/timemorph.json - oyunu acmadan da duzenleyebilirsin. */
public class TimeMorphConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("timemorph.json");

    public static TimeMorphConfig INSTANCE = new TimeMorphConfig();

    public boolean affectPlayers = true;
    public boolean affectMobs = true;

    // Golge carpanlari (1.0 = vanilla). Radius = yuvarlagin buyuklugu, opacity = koyulugu.
    public float dayShadowRadius = 1.0f;
    public float dayShadowOpacity = 1.0f;
    public float nightShadowRadius = 1.6f;
    public float nightShadowOpacity = 0.45f;

    // Gece/gunduz gecis saatleri (24000 tick = 1 gun)
    public int sunsetStart = 12000;
    public int nightStart = 13000;
    public int nightEnd = 23000;
    public int dawnEnd = 24000;

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                INSTANCE = GSON.fromJson(Files.readString(FILE), TimeMorphConfig.class);
            } else {
                Files.writeString(FILE, GSON.toJson(INSTANCE));
            }
        } catch (Exception e) {
            System.err.println("[TimeMorph] config okunamadi, varsayilanlar kullaniliyor: " + e);
            INSTANCE = new TimeMorphConfig();
        }
    }

    /** 0 = tam gunduz, 1 = tam gece (yumusak gecis). */
    public float nightFactor(long timeOfDay) {
        float t = (float) (timeOfDay % 24000L);
        if (t < sunsetStart) return 0f;
        if (t < nightStart) return (t - sunsetStart) / (float) (nightStart - sunsetStart);
        if (t < nightEnd) return 1f;
        return 1f - (t - nightEnd) / (float) (dawnEnd - nightEnd);
    }

    public float radiusMultiplier(long time) {
        float n = nightFactor(time);
        return dayShadowRadius + (nightShadowRadius - dayShadowRadius) * n;
    }

    public float opacityMultiplier(long time) {
        float n = nightFactor(time);
        return dayShadowOpacity + (nightShadowOpacity - dayShadowOpacity) * n;
    }
}
