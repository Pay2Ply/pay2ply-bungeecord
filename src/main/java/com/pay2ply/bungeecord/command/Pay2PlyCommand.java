package com.pay2ply.bungeecord.command;

import com.pay2ply.bungeecord.Pay2Ply;
import com.pay2ply.bungeecord.config.Config;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Pay2PlyCommand extends Command {

  public Pay2PlyCommand(){
    super("pay2plybungee", "");
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (commandSender.hasPermission("pay2ply.command")) {
      if (args.length > 0) {
        String token = args[0].trim().toLowerCase();

        Pay2Ply.getInstance().getSdk().setToken(token);

        Config.configuration.set("settings.token", token);
        Config.saveConfig();

        commandSender.sendMessage(String.format("%sSe o token do servidor estiver correto, a loja será vinculada em alguns instantes.", ChatColor.GREEN));
      } else {
        commandSender.sendMessage(String.format("%s/%s <token do servidor>.", ChatColor.RED, "pay2plybungee"));
      }
    } else {
      commandSender.sendMessage(String.format("%sVocê não possui permissão para executar este comando.", ChatColor.RED));
    }
  }

}
