package net.ledestudios.fireneedsoxygen.handler;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.ledestudios.fireneedsoxygen.FireNeedsOxygenPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//TODO: ~~System 형식으로 이름 변경할 것
public class BlockPlaceHandler extends EntityEventSystem<EntityStore, PlaceBlockEvent> {

    private final FireNeedsOxygenPlugin plugin;

    public BlockPlaceHandler(@Nonnull FireNeedsOxygenPlugin plugin) {
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
        boolean cancel = this.plugin.getConfig().items().contains(id);
        event.setCancelled(cancel);
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }

}
