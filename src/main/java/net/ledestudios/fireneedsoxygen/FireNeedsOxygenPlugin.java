package net.ledestudios.fireneedsoxygen;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import lombok.Getter;
import net.ledestudios.fireneedsoxygen.command.FireNeedsOxygenCommand;
import net.ledestudios.fireneedsoxygen.config.Config;
import net.ledestudios.fireneedsoxygen.config.YamlConfigurator;
import net.ledestudios.fireneedsoxygen.handler.BlockInWaterHandleSystem;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FireNeedsOxygenPlugin extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final @Nonnull YamlConfigurator configurator;

    @Getter
    private @Nonnull Config config = new Config(Set.of());

    public FireNeedsOxygenPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        this.configurator = new YamlConfigurator(getDataDirectory(), "config.yml");
    }

    @Override
    protected void setup() {
        reloadConfig().join();
        this.getEntityStoreRegistry().registerSystem(new BlockInWaterHandleSystem(this));
        this.getCommandRegistry().registerCommand(new FireNeedsOxygenCommand(this));
    }

    public CompletableFuture<Void> reloadConfig() {
        return configurator.load().whenComplete((unused, throwable) -> {
            if (throwable != null) {
                LOGGER.atWarning().log("Configuration load failed.", throwable.getMessage());
                return;
            }

            ConfigurationNode node = configurator.getConfig(Config.Nodes.ITEMS);
            try {
                Set<String> items = node.get(Config.Types.ITEMS);
                if (items == null || items.isEmpty()) {
                    this.config = new Config(Set.of());
                    LOGGER.atInfo().log("Empty configuration.");
                    return;
                }

                this.config = new Config(Set.copyOf(items));
            } catch (SerializationException e) {
                LOGGER.atWarning().log("Configuration load failed.", e);
                return;
            }

            LOGGER.atInfo().log("Configuration loaded successfully.");
        });
    }

}
