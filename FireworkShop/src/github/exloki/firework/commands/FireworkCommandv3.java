package com.github.exloki.firework.commands;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.github.exloki.firework.Permissions;
import com.github.exloki.firework.Fireworks;
import com.stirante.PrettyScaryLib.Firework;
import com.stirante.PrettyScaryLib.FireworkExplosion;
import com.stirante.PrettyScaryLib.FireworkStar;
import com.stirante.PrettyScaryLib.FireworkType;

public class FireworkCommandv3 extends JavaPlugin implements CommandExecutor, Listener {

	private Fireworks plugin = Fireworks.getInstance();
	String cmd = "/firework <set|check|create|reset> [flag|star|rocket] [value]";

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(player.hasPermission(Permissions.FIREWORK)){
			File file = new File(plugin.getDataFolder(), "config.yml");
			EnhancedConfiguration config = new EnhancedConfiguration(file, plugin);
			config.load();
			if(!config.getBoolean(player.getName() + ".exists")){
				config.set(player.getName() + ".trail", false);
				config.set(player.getName() + ".flicker", false);
				config.set(player.getName() + ".type", "small");
				config.set(player.getName() + ".height", 2);
				config.set(player.getName() + ".colours", new ArrayList<Integer>());
				config.set(player.getName() + ".coloursmess", "");
				config.set(player.getName() + ".fade", new ArrayList<Integer>());
				config.set(player.getName() + ".fademess", "");
				config.set(player.getName() + ".exists", true);
				config.save();
			}
		}
	}

	public void onEnable(){
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.hasPermission(Permissions.FIREWORK)){
				File file = new File(plugin.getDataFolder(), "config.yml");
				EnhancedConfiguration config = new EnhancedConfiguration(file, plugin);
				config.load();
				if(!config.getBoolean(player.getName() + ".exists")){
					config.set(player.getName() + ".trail", false);
					config.set(player.getName() + ".flicker", false);
					config.set(player.getName() + ".type", "small");
					config.set(player.getName() + ".height", 2);
					config.set(player.getName() + ".colours", new ArrayList<Integer>());
					config.set(player.getName() + ".coloursmess", "");
					config.set(player.getName() + ".fade", new ArrayList<Integer>());
					config.set(player.getName() + ".fademess", "");
					config.set(player.getName() + ".exists", true);
					config.save();
				}
			}
		}
	}

	public FireworkType translateType(String name){
		if(name.equalsIgnoreCase("burst")){
			return FireworkType.BURST;
		}else if(name.equalsIgnoreCase("small")){
			return FireworkType.SMALL_BALL;
		}else if(name.equalsIgnoreCase("large")){
			return FireworkType.LARGE_BALL;
		}else if(name.equalsIgnoreCase("creeper")){
			return FireworkType.CREEPER;
		}else if(name.equalsIgnoreCase("star")){
			return FireworkType.STAR;
		}else{
			return FireworkType.SMALL_BALL;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(command.getName().equalsIgnoreCase("firework")){
			if(sender.hasPermission(Permissions.FIREWORK)){
				File file = new File(plugin.getDataFolder(), "config.yml");
				EnhancedConfiguration config = new EnhancedConfiguration(file, plugin);
				boolean trail = config.getBoolean(sender.getName() + ".trail");
				boolean flicker = config.getBoolean(sender.getName() + ".flicker");
				String type = config.getString(sender.getName() + ".type");
				int height = config.getInt(sender.getName() + ".height");
				ArrayList<Integer> colours = (ArrayList<Integer>) config.getIntegerList(sender.getName() + ".colours");
				String coloursmess = config.getString(sender.getName() + ".coloursmess");
				StringBuilder coloursmsg = new StringBuilder();
				ArrayList<Integer> fade = (ArrayList<Integer>) config.get(sender.getName() + ".fade");
				String fademess = config.getString(sender.getName() + ".fademess");
				StringBuilder fademsg = new StringBuilder();
				if(args.length == 0){
					sender.sendMessage(ChatColor.GRAY + "Try /firework <check|reset>");
					sender.sendMessage(ChatColor.GRAY + "Try /firework set <flag> <value>");
					sender.sendMessage(ChatColor.GRAY + "Try /firework create <rocket|star>");
				}else if(args.length == 1){
					if(args[0].equalsIgnoreCase("check")){
						config.load();
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', trail ? "&6Trail: &7Enabled" : "&6Trail: &7Disabled"));
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', flicker ? "&6Twinkle: &7Enabled" : "&6Twinkle: &7Disabled"));
						if(type.equalsIgnoreCase("burst")){
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Type: &7Burst"));
						}else if(type.equalsIgnoreCase("small")){
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Type: &7Small explosion"));
						}else if(type.equalsIgnoreCase("large")){
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Type: &7Large explosion"));
						}else if(type.equalsIgnoreCase("creeper")){
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Type: &7Creeper head"));
						}else if(type.equalsIgnoreCase("star")){
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Type: &7Star"));
						}else{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Type: &cUnknown"));
						}
						if(colours.isEmpty() || colours == null){
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Colours: &cNone"));
						}else{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Colours: &7" + coloursmess));
						}
						if(fade.isEmpty() || fade == null){
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Fade: &7None"));
						}else{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Fade: &7" + fademess));
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Height: &7" + height));
					}else if(args[0].equalsIgnoreCase("reset")){
						colours.clear();
						fade.clear();
						trail = false;
						flicker = false;
						type = "small";
						height = 2;
						sender.sendMessage(ChatColor.GREEN + "Settings reset to defaults");
					}else if(args[0].equalsIgnoreCase("set")){
						sender.sendMessage(ChatColor.RED + "Try /firework set <trail|twinkle|type|colours|fade|height> [value]");
					}else if(args[0].equalsIgnoreCase("create")){
						sender.sendMessage(ChatColor.RED + "Try /firework create <star|rocket>");
					}else{
						Fireworks.Usage(sender, cmd);
					}
				}else if(args.length == 2){
					if(args[0].equalsIgnoreCase("create")){
						config.load();
						if(args[1].equalsIgnoreCase("rocket")){
							if(!colours.isEmpty() && !(height > 3) && !(height < 0) && type != null){
								ItemStack item = new ItemStack(Material.FIREWORK);
								ItemStack item2 = Firework.addExplosion(item, new FireworkExplosion(trail, translateType(type), colours, flicker, fade));
								ItemStack rocket = Firework.setFlight(item2, height);
								((Player) sender).getInventory().addItem(rocket);
								sender.sendMessage(ChatColor.GREEN + "Here's your firework!");
							}else{
								sender.sendMessage(ChatColor.RED + "There's a problem with your options. Use \"/firework check\" to check them.");
							}
						}else if(args[1].equalsIgnoreCase("star")){
							if(!colours.isEmpty() && type != null){
								ItemStack item = new ItemStack(Material.FIREWORK_CHARGE);
								ItemStack star = FireworkStar.setExplosion(item, new FireworkExplosion(trail, translateType(type), colours, flicker, fade));
								((Player) sender).getInventory().addItem(star);
								sender.sendMessage(ChatColor.GREEN + "Here's your firework star!");
							}else{
								sender.sendMessage(ChatColor.RED + "There's a problem with your options. Use \"/firework check\" to check them.");
							}
						}else{
							sender.sendMessage(ChatColor.RED + "Try /firework create <star|rocket>");
						}
					}else if(args[0].equalsIgnoreCase("set")){
						sender.sendMessage(ChatColor.RED + "Try /firework set <trail|twinkle|type|colours|fade|height> [value]");
					}
				}else if(args.length > 2){
					if(args[0].equalsIgnoreCase("set")){
						config.load();
						if(args[1].equalsIgnoreCase("trail")){
							if(args.length == 3){
								if(args[2].equalsIgnoreCase("true")){
									sender.sendMessage(ChatColor.GREEN + "Trail enabled");
									config.set(sender.getName() + ".trail", true);
									config.save();
								}else if(args[2].equalsIgnoreCase("false")){
									sender.sendMessage(ChatColor.GREEN + "Trail disabled");
									config.set(sender.getName() + ".trail", false);
									config.save();
								}else if(args[2].equalsIgnoreCase("switch")){
									if(config.getString(sender.getName() + ".trail").equalsIgnoreCase("true")){
										sender.sendMessage(ChatColor.GREEN + "Trail disabled");
										config.set(sender.getName() + ".trail", false);
										config.save();
									}else if(config.getString(sender.getName() + ".trail").equalsIgnoreCase("false")){
										sender.sendMessage(ChatColor.GREEN + "Trail enabled");
										config.set(sender.getName() + ".trail", true);
										config.save();
									}else{
										sender.sendMessage(ChatColor.RED + "somethings wrong :S config is broken somewhere");
									}
								}else{
									sender.sendMessage(ChatColor.RED + "Try /firework set trail <true|false|switch>");
								}
							}else{
								sender.sendMessage(ChatColor.RED + "Try /firework set trail <true|false|switch>");
							}
						}else if(args[1].equalsIgnoreCase("twinkle")){
							if(args.length == 3){
								if(args[2].equalsIgnoreCase("true")){
									sender.sendMessage(ChatColor.GREEN + "Twinkle enabled");
									config.set(sender.getName() + ".flicker", true);
									config.save();
								}else if(args[2].equalsIgnoreCase("false")){
									sender.sendMessage(ChatColor.GREEN + "Twinkle disabled");
									config.set(sender.getName() + ".flicker", false);
									config.save();
								}else if(args[2].equalsIgnoreCase("switch")){
									if(config.getString(sender.getName() + ".flicker").equalsIgnoreCase("true")){
										sender.sendMessage(ChatColor.GREEN + "Twinkle disabled");
										config.set(sender.getName() + ".flicker", false);
										config.save();
									}else if(config.getString(sender.getName() + ".flicker").equalsIgnoreCase("false")){
										sender.sendMessage(ChatColor.GREEN + "Twinkle enabled");
										config.set(sender.getName() + ".flicker", true);
										config.save();
									}else{
										sender.sendMessage(ChatColor.RED + "somethings wrong :S config is broken somewhere");
									}
								}else{
									sender.sendMessage(ChatColor.RED + "Try /firework set twinkle <true|false|switch>");
								}
							}else{
								sender.sendMessage(ChatColor.RED + "Try /firework set twinkle <true|false|switch>");
							}
						}else if(args[1].equalsIgnoreCase("type")){
							if(args.length == 3){
								if(args[2].equalsIgnoreCase("large")){
									config.set(sender.getName() + ".type", "large");
									config.save();
									sender.sendMessage(ChatColor.GREEN + "Type set to Large explosion");
								}else if(args[2].equalsIgnoreCase("small")){
									sender.sendMessage(ChatColor.GREEN + "Type set to Small explosion");
									config.set(sender.getName() + ".type", "small");
									config.save();
								}else if(args[2].equalsIgnoreCase("burst")){
									sender.sendMessage(ChatColor.GREEN + "Type set to Burst");
									config.set(sender.getName() + ".type", "burst");
									config.save();
								}else if(args[2].equalsIgnoreCase("creeper")){
									sender.sendMessage(ChatColor.GREEN + "Type set to Creeper head");
									config.set(sender.getName() + ".type", "creeper");
									config.save();
								}else if(args[2].equalsIgnoreCase("star")){
									sender.sendMessage(ChatColor.GREEN + "Type set to Star");
									config.set(sender.getName() + ".type", "star");
									config.save();
								}else{
									sender.sendMessage(ChatColor.RED + "Try /firework set type <large|small|burst|creeper|star>");
								}
							}else{
								sender.sendMessage(ChatColor.RED + "Try /firework set type <large|small|burst|creeper|star>");
							}
						}else if(args[1].equalsIgnoreCase("colours") || args[1].equalsIgnoreCase("colour") ){
							if(args.length > 2){
								String newcolours = (config.getString(sender.getName() + ".coloursmess"));
								for(int i = 2; i < args.length; i++){
									switch(args[i]){
									case "blue":
										if(!colours.contains(2437522)){
											colours.add(2437522);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Blue, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "blue color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "purple":
										if(!colours.contains(8073150)){
											colours.add(8073150);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Purple, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "purple color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "red":
										if(!colours.contains(11743532)){
											colours.add(11743532);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Red, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "red color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "green":
										if(!colours.contains(3887386)){
											colours.add(3887386);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Green, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "green color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "cyan":
										if(!colours.contains(2651799)){
											colours.add(2651799);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Cyan, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "cyan color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "lightgrey":
										if(!colours.contains(11250603)){
											colours.add(11250603);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Light Gray, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "light gray color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "lightgray":
										if(!colours.contains(11250603)){
											colours.add(11250603);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Light Gray, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "light gray color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "grey":
										if(!colours.contains(4408131)){
											colours.add(4408131);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Light Gray, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "gray color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "gray":
										if(!colours.contains(4408131)){
											colours.add(4408131);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Gray, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "gray color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "pink":
										if(!colours.contains(14188952)){
											colours.add(14188952);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Pink, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "pink color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "lime":
										if(!colours.contains(4312372)){
											colours.add(4312372);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Lime, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "lime color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "black":
										if(!colours.contains(1973019)){
											colours.add(1973019);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Black, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "black color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "brown":
										if(!colours.contains(5320730)){
											colours.add(5320730);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Brown, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "brown color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "yellow":
										if(!colours.contains(14602026)){
											colours.add(14602026);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Yellow, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "yellow color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "magenta":
										if(!colours.contains(12801229)){
											colours.add(12801229);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Magenta, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "magenta color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "orange":
										if(!colours.contains(15435844)){
											colours.add(15435844);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Orange, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "orange color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "lightblue":
										if(!colours.contains(671995)){
											colours.add(6719955);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "Light Blue, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "light blue color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "white":
										if(!colours.contains(15790320)){
											colours.add(15790320);
											newcolours = (config.getString(sender.getName() + ".coloursmess") + "White, ");
											coloursmsg.append(newcolours);
											sender.sendMessage(ChatColor.GREEN + "white color set");
											continue;
										}
										sender.sendMessage(ChatColor.GREEN + "This color has already been used!");
										continue;
									case "clear":
											colours.clear();										
											config.set(sender.getName() + ".colours", colours);
											config.set(sender.getName() + ".coloursmess", "");
											config.save();
											coloursmsg.delete(0, 999);
											sender.sendMessage(ChatColor.RED + "Colours list cleared");
											continue;
										
									default:
										sender.sendMessage(ChatColor.RED + "Colour " + args[i] + " not recognised");
										sender.sendMessage(ChatColor.RED + "Colours list cleared");
										sender.sendMessage(ChatColor.GREEN + "Applicable colours: " + ChatColor.GRAY + "Blue, Purple, Red, Green, Cyan, LightGrey, Grey, Pink, Lime, Black, Brown, Yellow, Magenta, Orange, LightBlue, White, Reset.");
										colours.clear();
										config.set(sender.getName() + ".colours", colours);
										config.save();
										coloursmsg.delete(0, 999);
										break;
									}	
								}
								if(colours.size() > 0){
									config.set(sender.getName() + ".colours", colours);
									config.set(sender.getName() + ".coloursmess", coloursmsg.toString());
									config.save();
								}
							}else{
								sender.sendMessage(ChatColor.RED + "No colours chosen");
								sender.sendMessage(ChatColor.GREEN + "Applicable colours: " + ChatColor.GRAY + "Blue, Purple, Red, Green, Cyan, LightGrey, Grey, Pink, Lime, Black, Brown, Yellow, Magenta, Orange, LightBlue, White, Reset.");
							}
						}else if(args[1].equalsIgnoreCase("fade")){
							if(args.length > 2){
								fade.clear();
								fademsg.delete(0, 999);
								for(int i = 2; i < args.length; i++){
									switch(args[i]){
									case "blue":
										if(!fade.contains(2437522)){
											fade.add(2437522);
											fademsg.append("Blue");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "purple":
										if(!fade.contains(8073150)){
											fade.add(8073150);
											fademsg.append("Purple");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "red":
										if(!fade.contains(11743532)){
											fade.add(11743532);
											fademsg.append("Red");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "green":
										if(!fade.contains(3887386)){
											fade.add(3887386);
											fademsg.append("Green");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "cyan":
										if(!fade.contains(2651799)){
											fade.add(2651799);
											fademsg.append("Cyan");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "lightgrey":
										if(!fade.contains(11250603)){
											fade.add(11250603);
											fademsg.append("Light grey");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "lightgray":
										if(!fade.contains(11250603)){
											fade.add(11250603);
											fademsg.append("Light gray");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "grey":
										if(!fade.contains(4408131)){
											fade.add(4408131);
											fademsg.append("Grey");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "gray":
										if(!fade.contains(4408131)){
											fade.add(4408131);
											fademsg.append("Gray");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "pink":
										if(!fade.contains(14188952)){
											fade.add(14188952);
											fademsg.append("Pink");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "lime":
										if(!fade.contains(4312372)){
											fade.add(4312372);
											fademsg.append("Lime");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "black":
										if(!fade.contains(1973019)){
											fade.add(1973019);
											fademsg.append("Black");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "brown":
										if(!fade.contains(5320730)){
											fade.add(5320730);
											fademsg.append("Brown");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "yellow":
										if(!fade.contains(14602026)){
											fade.add(14602026);
											fademsg.append("Yellow");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "magenta":
										if(!fade.contains(12801229)){
											fade.add(12801229);
											fademsg.append("Magenta");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "orange":
										if(!fade.contains(15435844)){
											fade.add(15435844);
											fademsg.append("Orange");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "lightblue":
										if(!fade.contains(671995)){
											fade.add(6719955);
											fademsg.append("Light blue");
											fademsg.append(", ");
											continue;
										}
										continue;
									case "white":
										if(!fade.contains(15790320)){
											fade.add(15790320);
											fademsg.append("White");
											fademsg.append(", ");
											continue;
										}
										continue;
									default:
										sender.sendMessage(ChatColor.RED + "Colour " + args[i] + " not recognised");
										sender.sendMessage(ChatColor.RED + "Fades list cleared");
										sender.sendMessage(ChatColor.GREEN + "Applicable fades: " + ChatColor.GRAY + "Blue, Purple, Red, Green, Cyan, LightGrey, Grey, Pink, Lime, Black, Brown, Yellow, Magenta, Orange, LightBlue, White, Reset.");
										fade.clear();
										config.set(sender.getName() + ".fade", fade);
										config.save();
										fademsg.delete(0, 999);
										break;
									}	
								}
								if(fade.size() > 0){
									sender.sendMessage(ChatColor.GREEN + "Fades set. Use /firework check to see them!");
									config.set(sender.getName() + ".fade", fade);
									config.set(sender.getName() + ".fademess", fademsg.toString().trim().substring(0, fademsg.toString().trim().length() - 1));
									config.save();
								}
							}else{
								sender.sendMessage(ChatColor.RED + "No colours chosen");
								sender.sendMessage(ChatColor.GREEN + "Applicable colours: " + ChatColor.GRAY + "Blue, Purple, Red, Green, Cyan, LightGrey, Grey, Pink, Lime, Black, Brown, Yellow, Magenta, Orange, LightBlue, White, Reset.");
							}
						}else if(args[1].equalsIgnoreCase("height")){
							if(args.length == 3){
								if(args[2].equalsIgnoreCase("1")){
									sender.sendMessage(ChatColor.GREEN + "Explosion height set to 1");
									config.set(sender.getName() + ".height", 1);
									config.save();
								}else if(args[2].equalsIgnoreCase("2")){
									sender.sendMessage(ChatColor.GREEN + "Explosion height set to 2");
									config.set(sender.getName() + ".height", 2);
									config.save();
								}else if(args[2].equalsIgnoreCase("3")){
									sender.sendMessage(ChatColor.GREEN + "Explosion height set to 3");
									config.set(sender.getName() + ".height", 3);
									config.save();
								}else if(args[2].equalsIgnoreCase("0")){
									sender.sendMessage(ChatColor.GREEN + "Explosion height set to 0");
									config.set(sender.getName() + ".height", 0);
									config.save();
								}else{
									sender.sendMessage(ChatColor.RED + "Try /firework set height <1|2|3>");
								}
							}else{
								sender.sendMessage(ChatColor.RED + "No colours chosen");
								sender.sendMessage(ChatColor.GREEN + "Applicable colours: " + ChatColor.GRAY + "Blue, Purple, Red, Green, Cyan, LightGrey, Grey, Pink, Lime, Black, Brown, Yellow, Magenta, Orange, LightBlue, White, Reset.");
							}
						}else{
							sender.sendMessage(ChatColor.RED + "Try /firework set <trail|twinkle|type|colours|fade|height> [value]");
						}
					}else{
						Fireworks.Usage(sender, cmd);
					}
				}

			}else{
				Fireworks.noPermission(sender);
			}
		}
		return false;
	}
}