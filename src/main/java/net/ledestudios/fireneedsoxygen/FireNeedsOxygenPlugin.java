package net.ledestudios.fireneedsoxygen;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;

public class FireNeedsOxygenPlugin extends JavaPlugin {

    public FireNeedsOxygenPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        getLogger().atInfo().log("FireNeedsOxygenPlugin setup");
    }

}
