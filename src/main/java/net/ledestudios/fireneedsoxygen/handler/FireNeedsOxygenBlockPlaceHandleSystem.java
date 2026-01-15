package net.ledestudios.fireneedsoxygen.handler;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.ledestudios.fireneedsoxygen.FireNeedsOxygenPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FireNeedsOxygenBlockPlaceHandleSystem extends EntityEventSystem<EntityStore, PlaceBlockEvent> {

    private final FireNeedsOxygenPlugin plugin;

    public FireNeedsOxygenBlockPlaceHandleSystem(@Nonnull FireNeedsOxygenPlugin plugin) {
        super(PlaceBlockEvent.class);
        this.plugin = plugin;
    }

    @Override
    public void handle(
            int index,
            @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull PlaceBlockEvent event
    ) {
        ItemStack item = event.getItemInHand();
        if (item == null) {
            return;
        }

        String id = item.getItemId();
        boolean match = this.plugin.getConfig().items().contains(id);
        if (!match) {
            return;
        }

        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            return;
        }

        World world = player.getWorld();
        if (world == null) {
            return;
        }

        Vector3i pos = event.getTargetBlock();
        int fluid = world.getFluidId(pos.x, pos.y, pos.z);
        if (fluid == 0) {
            return;
        }

        event.setCancelled(true);
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }

}
