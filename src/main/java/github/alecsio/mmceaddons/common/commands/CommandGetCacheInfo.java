package github.alecsio.mmceaddons.common.commands;

import github.alecsio.mmceaddons.common.cache.ScrubbedChunksCache;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CommandGetCacheInfo extends CommandBase {

    @Override
    public String getName() {
        return "getScrubbedChunksCacheInfo";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.mmcea.getcacheinfo.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        sender.sendMessage(new TextComponentString(ScrubbedChunksCache.getInformation()));
    }
}
