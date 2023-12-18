package de.liquiddev.command.adapter.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandRoot;
import de.liquiddev.command.adapter.AbstractCommandAdapter;

public class VelocityCommandAdapter extends AbstractCommandAdapter<CommandSource> {

    private final ProxyServer proxyServer;

    public VelocityCommandAdapter(CommandRoot<?> command, ProxyServer proxyServer) {
        super(command);
        this.proxyServer = proxyServer;
    }

    public void register() {
        CommandManager commandManager = proxyServer.getCommandManager();
        CommandMeta meta = commandManager.metaBuilder(getCommand().getName())
                .aliases(getCommand().getAliases().toArray(String[]::new))
                .build();
        commandManager.register(meta, new CommandListener());
    }

    @Override
    public AbstractCommandSender<CommandSource> abstractSender(CommandSource sender) {
        return new VelocityCommandSender<>(sender);
    }

    @Override
    public CommandArguments getArguments(String[] args, AbstractCommandSender<CommandSource> sender) {
        return VelocityArguments.fromStrings(getCommand(), sender, args, proxyServer);
    }

    private class CommandListener implements SimpleCommand {

        @Override
        public void execute(Invocation invocation) {
            VelocityCommandAdapter.this.onCommand(invocation.source(), invocation.arguments());
        }
    }
}
