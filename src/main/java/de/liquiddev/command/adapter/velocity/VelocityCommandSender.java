package de.liquiddev.command.adapter.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.liquiddev.command.AbstractCommandSender;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocityCommandSender<T extends CommandSource> extends AbstractCommandSender<T> {
    public VelocityCommandSender(T sender) {
        super(sender);
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public String getName() {
        return sender instanceof Player player ? player.getUsername() : "null";
    }
}
