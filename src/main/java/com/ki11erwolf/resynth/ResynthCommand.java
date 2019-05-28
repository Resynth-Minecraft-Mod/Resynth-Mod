package com.ki11erwolf.resynth;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Special command for anything resynth related.
 *
 * Currently used for debugging.
 */
public class ResynthCommand extends CommandBase {

    /**
     * @return command identifier name.
     */
    @Override
    public String getName() {
        return "resynth";
    }

    /**
     * @return 2 - Creative Only.
     */
    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * @return lang key.
     */
    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.resynth.usage";
    }

    /**
     * Execution Logic.
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {

        //Version
        if(args.length == 0){
            sender.sendMessage(new TextComponentString("Resynth Version: " + ResynthMod.MOD_VERSION));
            return;
        }

        String arg1 = args[0];

        // get Command
        if(arg1.equals("get")){
            if(sender.getCommandSenderEntity() instanceof EntityPlayer){
                sender.sendMessage(new TextComponentString("Printing item info to console..."));

                ItemStack item = ((EntityPlayer)sender.getCommandSenderEntity()).getHeldItemMainhand();
                ResynthMod.getLogger().info("Held Item Info:" +
                        "\n\t\tgetUnlocalizedName() - " + item.getUnlocalizedName() +
                        "\n\t\tgetItemDamage() - " + item.getItemDamage() +
                        "\n\t\tgetMetadata() - " + item.getMetadata());
            }
        }
    }
}
