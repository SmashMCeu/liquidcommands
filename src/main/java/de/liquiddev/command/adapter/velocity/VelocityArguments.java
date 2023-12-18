package de.liquiddev.command.adapter.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.liquiddev.command.*;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class VelocityArguments extends CommandArguments {

    public static CommandArguments fromStrings(CommandNode<?> command, AbstractCommandSender<?> sender, String[] args, ProxyServer server) {
        return new VelocityArguments(command, sender, args, server);
    }

    private final ProxyServer proxyServer;

    VelocityArguments(@NonNull CommandNode<?> command, @NonNull AbstractCommandSender<?> sender, @NotNull @NonNull String[] arguments, ProxyServer server) {
        super(command, sender, arguments);
        this.proxyServer = server;
    }

    public Player getPlayer(int index) throws CommandFailException {
        if (!isPresent(index)) {
            throw new MissingCommandArgException(getCommand(), Player.class, index);
        }
        String arg = get(index);
        Optional<Player> player = proxyServer.getPlayer(arg);
        if (player.isEmpty()) {
            throw new InvalidCommandArgException(getCommand(), Player.class, arg);
        }
        return player.get();
    }
}
