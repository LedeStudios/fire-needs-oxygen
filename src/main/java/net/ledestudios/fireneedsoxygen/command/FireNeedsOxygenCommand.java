package net.ledestudios.fireneedsoxygen.command;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.ledestudios.fireneedsoxygen.FireNeedsOxygenPlugin;
import org.jetbrains.annotations.NotNull;

public class FireNeedsOxygenCommand extends AbstractPlayerCommand {

    private final FireNeedsOxygenPlugin plugin;

    public FireNeedsOxygenCommand(FireNeedsOxygenPlugin plugin) {
        super("fno", "Reload Fire Needs Oxygen Plugin Configuration.", true);
        this.plugin = plugin;
        requirePermission("fireneedsoxygen.command.fno");
    }

    @Override
    protected void execute(
            @NotNull CommandContext context,
            @NotNull Store<EntityStore> store,
            @NotNull Ref<EntityStore> ref,
            @NotNull PlayerRef playerRef,
            @NotNull World world
    ) {
        this.plugin.reloadConfig().thenRun(() -> {
            playerRef.sendMessage(Message.raw("Fire Needs Oxygen Plugin Configuration Reloaded."));
            this.plugin.getConfig().items().forEach(item -> playerRef.sendMessage(Message.raw("- " + item)));
        });
    }

}
