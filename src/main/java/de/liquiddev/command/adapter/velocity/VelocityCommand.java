package de.liquiddev.command.adapter.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.CommandRoot;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class VelocityCommand <T extends CommandSource> extends CommandRoot<T> {

    @Getter(AccessLevel.PROTECTED)
    private final ProxyServer proxyServer;
    private final VelocityCommandAdapter adapter;

    public VelocityCommand(ProxyServer proxyServer, Class<T> senderType, String name) {
        this(proxyServer, senderType, name, "");
    }

    public VelocityCommand(ProxyServer proxyServer, Class<T> senderType, String name, String hint, String... aliases) {
        super(senderType, name, hint);
        this.proxyServer = proxyServer;
        this.adapter = new VelocityCommandAdapter(this, proxyServer);
    }

    @Override
    protected void onCommand(AbstractCommandSender<T> sender, CommandArguments arguments) throws CommandFailException {
        this.onCommand(sender.getSender(), (VelocityArguments) arguments);
    }

    protected abstract void onCommand(T t, VelocityArguments args) throws CommandFailException;

    public void register() {
        this.adapter.register();
    }
}
