package mnm.mods.util;

import java.io.File;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mumfrey.liteloader.JoinGameListener;

import mnm.mods.util.update.UpdateChecker;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.SPacketJoinGame;

public class LiteModMnmUtils implements JoinGameListener {
    @Override
    public String getName() {
        return "MnmUtils";
    }

    @Override
    public String getVersion() {
        return "@VERSION@";
    }

    @Override
    public void init(File arg0) {}

    @Override
    public void upgradeSettings(String arg0, File arg1, File arg2) {}

    @Override
    public void onJoinGame(INetHandler netHandler, SPacketJoinGame joinGamePacket, ServerData serverData, RealmsServer realmsServer) {
        UpdateChecker.runUpdateChecks();
    }
}
