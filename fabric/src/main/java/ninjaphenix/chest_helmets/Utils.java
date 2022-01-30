package ninjaphenix.chest_helmets;

import net.minecraft.util.Identifier;

public final class Utils {
    public static final String MOD_ID = "chest_helmets";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    private Utils() {
        throw new IllegalStateException("Utility class should not be instantiated.");
    }
}
