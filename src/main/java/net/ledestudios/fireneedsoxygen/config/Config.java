package net.ledestudios.fireneedsoxygen.config;


import io.leangen.geantyref.TypeToken;

import javax.annotation.Nonnull;
import java.util.Set;

public record Config(
        @Nonnull Set<String> items
) {

    public static class Nodes {
        public static final String ITEMS = "items";
    }

    public static class Types {

        public static final TypeToken<Set<String>> ITEMS = new TypeToken<>() {};

    }

}
