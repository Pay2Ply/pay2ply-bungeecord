package com.pay2ply.bungeecord.config;

import com.pay2ply.bungeecord.Pay2Ply;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;

public class Config {

  public String token;
  public boolean messages;
  protected static YamlConfig config;
  public static Configuration configuration;

  public Config() {
    config = null;
    try {
      (config = new YamlConfig("config.yml", Pay2Ply.getInstance())).saveDefaultConfig();
      config.loadConfig();
      configuration = config.getConfig();
      loadConfig();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void saveConfig() {
    try {
      config.saveConfig();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean loadConfig() {
    if (configuration == null) {
      return false;
    }
    token = configuration.getString("settings.token");
    messages = configuration.getBoolean("settings.messages");
    return true;
  }

}
