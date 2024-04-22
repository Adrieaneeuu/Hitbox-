package com.yourdomain.hitboxchangermod;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.ClientCommand;
import net.fabricmc.fabric.api.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.Keyboard;

import java.util.Set;

public class HitboxChangerMod implements ClientCommand {

    private static final float HITBOX_CHANGE_AMOUNT = 0.05f;

    @Override
    public Set<String> getAliases() {
        return ImmutableSet.of("decreasehitbox", "increasehitbox");
    }

    @Override
    public void execute(MinecraftClient client, String message) {
        // This method is not used in this implementation
    }

    public void decreaseHitbox() {
        changeHitbox(-HITBOX_CHANGE_AMOUNT);
    }

    public void increaseHitbox() {
        changeHitbox(HITBOX_CHANGE_AMOUNT);
    }

    private void changeHitbox(float changeAmount) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
                if (entity != player) {
                    float currentHitbox = entity.getBoundingBox().getRadiusX();
                    entity.setBoundingBox(entity.getBoundingBox().withNewDimensions(
                            entity.getBoundingBox().getWidth() + changeAmount,
                            entity.getBoundingBox().getHeight(),
                            entity.getBoundingBox().getLength() + changeAmount));
                    // Send message to chat (optional)
                    MinecraftClient.getInstance().player.sendMessage(LiteralText.of("Хитбокс изменен на " + (changeAmount * 100) + "%"), true);
                }
            }
        }
    }

    public void register() {
        ClientCommandRegistrationCallback.EVENT.register(this);
        MinecraftClient.getInstance().getInput().addKeyListener(this::handleKeypress);
    }

    @Override
    public void handleKeypress(KeyboardInputEvent event) {
        if (event.isKeyPressed(Keyboard.KEY_ALT) && event.isKeyPressed(Keyboard.KEY_Z)) {
            decreaseHitbox();
        } else if (event.isKeyPressed(Keyboard.KEY_ALT) && event.isKeyPressed(Keyboard.KEY_X)) {
            increaseHitbox();
        }
    }
}
