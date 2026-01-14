package net.ledestudios.fireneedsoxygen.config;

import com.hypixel.hytale.logger.HytaleLogger;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * A utility class for managing YAML configuration files.
 * This class handles loading configuration files from the plugin data directory,
 * and creating default configuration files from resources if they don't exist.
 */
@RequiredArgsConstructor
public class YamlConfigurator {

    private final HytaleLogger logger = HytaleLogger.getLogger();

    private final @NotNull Path dataPath;
    private final @Nullable URL resourceUrl;

    private @NotNull ConfigurationNode config = BasicConfigurationNode.root();

    public YamlConfigurator(@NotNull Path dataPath, @NotNull String filename) {
        this.dataPath = dataPath.resolve(filename);
        this.resourceUrl = getClass().getClassLoader().getResource(filename);
    }

    /**
     * Returns the root configuration node.
     *
     * @return The root configuration node
     */
    public @NotNull ConfigurationNode getConfig() {
        return config;
    }

    /**
     * Returns a configuration node at the specified path.
     *
     * @param path Path to the configuration node
     * @return The configuration node at the specified path
     */
    public @NotNull ConfigurationNode getConfig(Object... path) {
        return config.node(path);
    }

    /**
     * Loads the configuration file asynchronously.
     * If the file doesn't exist, it creates parent directories if needed and copies
     * the default configuration from the plugin resources if available.
     *
     * @return A CompletableFuture that completes when the configuration is loaded
     */
    public @NotNull CompletableFuture<Void> load() {
        return CompletableFuture.runAsync(() -> {
            if (Files.notExists(dataPath)) {
                Path parent = dataPath.getParent();
                if (parent != null && Files.notExists(parent)) {
                    try {
                        Files.createDirectories(parent);
                        logger.atInfo().log("Creating directory {}", parent);
                    } catch (Exception e) {
                        logger.atWarning().log("Failed to create directories for {}", dataPath, e);
                        return;
                    }
                }

                if (resourceUrl != null) {
                    try (InputStream input = resourceUrl.openStream()) {
                        Files.copy(input, dataPath);
                        logger.atInfo().log("Copied resource {} to {}", resourceUrl, dataPath);
                    } catch (Exception e) {
                        logger.atWarning().log("Failed to copy resource {} to {}", resourceUrl, dataPath, e);
                        return;
                    }
                }
            }

            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .path(dataPath)
                    .build();
            try {
                config = loader.load();
                logger.atInfo().log("Loaded YAML configuration from {}", dataPath);
            } catch (Exception e) {
                logger.atWarning().log("Failed to load YAML configuration from {}", dataPath, e);
            }
        });
    }
}