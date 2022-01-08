package com.pay2ply.bungeecord;

import com.pay2ply.bungeecord.command.Pay2PlyCommand;
import com.pay2ply.bungeecord.config.Config;
import com.pay2ply.sdk.SDK;
import com.pay2ply.sdk.dispense.Dispense;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class Pay2Ply extends Plugin {
  private static Pay2Ply instance;

  public Config config;

  public Pay2Ply() {
    Pay2Ply.instance = this;
  }

  public static Pay2Ply getInstance() {
    return Pay2Ply.instance;
  }

  private final SDK sdk = new SDK();

  public SDK getSdk() {
    return sdk;
  }

  @Override
  public void onEnable() {
    config = new Config();

    this.sdk.setToken(Config.configuration.getString("settings.token"));

    getProxy().getPluginManager().registerCommand(this, new Pay2PlyCommand());

    getProxy().getScheduler().schedule(this, new Runnable() {
      @Override
      public void run() {
        Dispense[] dispenses = null;

        try {
          dispenses = sdk.getDispenses();
        } catch (Exception exception) {
          getLogger().warning(String.format("[%s] %s", getDescription().getName(), exception.getMessage()));
        }

        if (dispenses == null) {
          return;
        }

        for (Dispense dispense : dispenses) {
          getProxy().getScheduler().schedule(getInstance(), new Runnable() {
            @Override
            public void run() {
              ProxiedPlayer player = getProxy().getPlayer(dispense.getUsername());

              if (player == null) {
                return;
              }

              try {
                sdk.update(dispense.getUsername(), dispense.getId());
                BungeeCord.getInstance().getPluginManager().dispatchCommand(BungeeCord.getInstance().getConsole(), dispense.getCommand());

                if (Config.configuration.getBoolean("settings.messages")) {
                  getLogger().info(String.format("%sO produto de %s foi ativo.", ChatColor.GREEN, dispense.getUsername()));
                }
              } catch (Exception exception) {
                getLogger().warning(String.format("[%s] %s", getDescription().getName(), exception.getMessage()));
              }
            }
          }, 1, TimeUnit.SECONDS);
        }
      }
    }, 1, 1, TimeUnit.MINUTES);
  }
}
