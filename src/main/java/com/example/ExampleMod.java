import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import net.minecraft.util.hitbox.BoundingBox;

public class ExampleMod implements ModInitializer {

    private static final KeyBinding keyAltZ = KeyBindingHelper.registerKeyBinding(
        new KeyBinding("key.examplemod.alt_z", GLFW.GLFW_KEY_Z, "category.examplemod")
    );

    private static final KeyBinding keyAltX = KeyBindingHelper.registerKeyBinding(
        new KeyBinding("key.examplemod.alt_x", GLFW.GLFW_KEY_X, "category.examplemod")
    );

    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyAltZ.isPressed() && InputUtil.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT)) {
                adjustHitbox(client, -5);
            }

            if (keyAltX.isPressed() && InputUtil.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT)) {
                adjustHitbox(client, 5);
            }
        });
    }

    private void adjustHitbox(MinecraftClient client, int percent) {
        if (client.world != null) {
            for (PlayerEntity player : client.world.getPlayers()) {
                double scale = (100 + percent) / 100.0;
                BoundingBox newHitbox = player.getBoundingBox().expand(scale);
                player.setBoundingBox(newHitbox);
                player.sendMessage(Text.of("Hitbox changed by " + percent + "%"), false);
            }
        }
    }
}
