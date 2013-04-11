package darvin939.DarkDays.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import darvin939.DarkDays.DarkDays;
import darvin939.DarkDays.Players.EffectManager;
import darvin939.DarkDays.Players.ItemManager;
import darvin939.DarkDays.Sql.Players.PlayerManager;
import darvin939.DarkDays.Sql.Players.SQLPlayer;
import darvin939.DarkDays.Utils.FGUtilCore;
import darvin939.DarkDays.Utils.PatPeter.SQLibrary.Database;

public class Config extends FGUtilCore {
	private static Logger log = Logger.getLogger("Minecraft");
	public static FGUtilCore FGU;

	private static DarkDays plugin;
	private static PC playerCfg;
	private static EffectManager effects;
	private static ItemManager items;
	private static CC chestCfg;
	private static RC regionCfg;
	private static LC lootCfg;

	public Config(DarkDays plg, boolean vcheck, String lng, String devbukkitname, String px) {
		super(plg, vcheck, lng, devbukkitname, px);
		plugin = plg;
		setupMessages();
		SaveMSG();
		effects = new EffectManager(plg);
		items = new ItemManager(plg);
		FGU = this;
	}

	public static EffectManager getEffectManager() {
		return effects;
	}

	public static ItemManager getItemManager() {
		return items;
	}

	public static enum Nodes {
		language("General.Language", "english"), verCheck("General.Version-check", true), prefix("General.Prefix", "DarkDays"), only_zombies("General.Zombie.OnlyZombies", true), zombie_speed("General.Zombie.Speed", 3), zombie_smoothness("General.Zombie.Smoothness", 15), attack_strength(
				"General.Zombie.AttackStrength", 4), zombie_health("General.Zombie.Health", 24), thirst_speed("General.Thirst.Speed", 2), bandage_id("General.Bandages.Id", 339), bandage_health("General.Bandages.Restore", 8), chest_empty("General.Chest.IfOnlyEmpty", true), chest_regen(
				"General.Chest.RegenTime", 2), chest_disappear("General.Chest.Disappear", true), wand_item("General.WandItem", 369), control_sitemst("General.ControlSItems", true), coloured_tegs("General.ColouredTags", true), enable_regions("General.EnableRegions", false), MYSQL_USER(
				"MySQL.Username", "root"), MYSQL_PASS("MySQL.Password", "root"), MYSQL_HOST("MySQL.Hostname", "localhost"), MYSQL_PORT("MySQL.Port", 3306), MYSQL_DATABASE("MySQL.Database", "darkdays"), MYSQL_DBWRAPPER("MySQL.DataWrapper", "none");

		String node;
		Object value;

		private Nodes(String node, Object value) {
			this.node = node;
			this.value = value;
		}

		public String getNode() {
			return node;
		}

		public Object getValue() {
			return this.value;
		}

		public Boolean getBoolean() {
			return (Boolean) value;
		}

		public Integer getInteger() {
			if (value instanceof Double)
				return ((Double) value).intValue();

			return (Integer) value;
		}

		public Double getDouble() {
			if (value instanceof Integer)
				return (double) ((Integer) value).intValue();

			return (Double) value;
		}

		public String getString() {
			return (String) value;
		}

		public Long getLong() {
			if (value instanceof Integer)
				return ((Integer) value).longValue();

			return (Long) value;
		}

		@SuppressWarnings("unchecked")
		public List<String> getStringList() {
			return (List<String>) value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public String toString() {
			return String.valueOf(value);
		}
	}

	private void setupMessages() {
		// loot_
		addMSG("loot_set", "LootID is set to %1%");
		addMSG("loot_error", "Error occurred with assigning LootID");
		addMSG("loot_nf", "LootID not found!");
		// chest_
		addMSG("chest_normal", "This is a normal chest");
		addMSG("chest_newWithID", "Created new looted chest with LootID");
		addMSG("chest_new", "Created new looted chest");
		addMSG("chest_remove", "Deleted loot chest");
		addMSG("chest_cantDestroy", "You can't destroy the loot chest!");
		// cmd_
		addMSG("cmd_indev", "This command in the process of developing");
		addMSG("cmd_occurred", "Error occurred when trying the command /%1%");
		addMSG("cmd_noperm", "You don't have the Permission(%1%) to do that!");
		// game_
		addMSG("game_start", "You are in the world. Good luck!");
		addMSG("game_alrady", "You alrady spawned!");
		addMSG("game_need_water", "Need more water");
		addMSG("game_noplay", "You are not in the world. Type the %1% to start playing");
		// spawn_
		addMSG("spawn_lobby_new", "New lobby location created");
		addMSG("spawn_lobby_error", "Failed to create location of lobby");
		addMSG("spawn_new", "Add new spawn point");
		addMSG("spawn_error", "Failed to add new spawn point");
		addMSG("spawn_lobby_nf", "Lobby not found");
		addMSG("spawn_nf", "Spawns not found");
		// item_
		addMSG("item_drinkmilk", "You drink a bucket of milk");
		addMSG("item_drink_water", "You drink some water");
		// tags_
		addMSG("tag_ends", "TAG's %1%");
		// hlp_
		addMSG("hlp_topic", "Type %1% to show help topic");
		addMSG("hlp_nf", "Help for %1% command not found");
		// hlp_cmd_
		addMSG("hlp_cmd_status", "Show your progress");
		addMSG("hlp_cmd_help", "Show this help topic");
		addMSG("hlp_cmd_chest", "Show information about the chest (look at the chest)");
		addMSG("hlp_cmd_chest_create", "Create new looted chest. Type &2..create &7[name] &fto create chest with lootID");
		addMSG("hlp_cmd_chest_delete", "Remove looted chest");
		addMSG("hlp_cmd_chest_loot", "Type &2..loot set &fto set the lootID for looked chest");
		addMSG("hlp_cmd_spawn", "Start playing");
		addMSG("hlp_cmd_spawn_set", "Add new spawn point. Type &2..set lobby &fto add new lobby location");
		addMSG("hlp_cmd_spawn_list", "Show list of all spawns and lobby locations");
		addMSG("hlp_cmd_tag", "Type&2 ..tag enable/disable &fto enable/disable colored names");
		addMSG("hlp_cmd_region", "Make a region of zombie spawn locations. Select region with WorldEdit Wand. Then type &2..region save &7[name] [parametrs]&f. Parametrs: &2s=&7[true/false]&f - can spawn, &2h=&7[true/false]&f - top-to-bottom");
	}

	public void initOtherConfigs() {
		if (isSqlWrapper()) {
			Database.initDb();
			Database db = Database.DATABASE;
			try {
				db.open();
			} catch (SQLException e) {
				log.warning("Couldn't connect to Database");
				e.printStackTrace();
			}
			SQLPlayer.createTables();
			SQLPlayer.initPrep();
			PlayerManager.init();
		}

		playerCfg = new PC(plugin);
		chestCfg = new CC(plugin);
		lootCfg = new LC(plugin);
		regionCfg = new RC(plugin);
	}

	public static void init() {
		extract(new String[] { "config.yml" });
		load(new File(DarkDays.getDataPath(), "config.yml"));

		if (!Nodes.prefix.getString().toLowerCase().equalsIgnoreCase("darkdays")) {
			String px = "[" + Nodes.prefix.getString() + "] ";
			log.info(DarkDays.prefix + "Found custom prefix " + px + ". Use it");
			DarkDays.setPrefix(px);
		}
		if (Nodes.zombie_smoothness.getInteger() > 19)
			Nodes.zombie_smoothness.setValue(19);
		else if (Nodes.zombie_smoothness.getInteger() < 1) {
			Nodes.zombie_smoothness.setValue(1);
		}
		Nodes.zombie_smoothness.setValue(20 - Nodes.zombie_smoothness.getInteger());
	}

	public static LC getLC() {
		return lootCfg;
	}

	public static RC getRC() {
		return regionCfg;
	}

	public static CC getCC() {
		return chestCfg;
	}

	public static PC getPC() {
		return playerCfg;
	}

	public static boolean isSqlWrapper() {
		String wrapper = Nodes.MYSQL_DBWRAPPER.getString();
		return wrapper.equalsIgnoreCase("mysql") || wrapper.equalsIgnoreCase("sqlite");
	}

	public static void extract(String[] names) {
		for (String name : names) {
			File actual = new File(DarkDays.getDataPath(), name);
			if (!actual.exists()) {
				InputStream input = DarkDays.class.getResourceAsStream("/" + name);
				if (input != null) {
					FileOutputStream output = null;
					try {
						output = new FileOutputStream(actual);
						byte[] buf = new byte[8192];
						int length = 0;
						while ((length = input.read(buf)) > 0) {
							output.write(buf, 0, length);
						}
					} catch (Exception e) {
					} finally {
						try {
							if (input != null)
								input.close();
						} catch (Exception e) {
						}
						try {
							if (output != null)
								output.close();
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

	public static void load(File file) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (Nodes n : Nodes.values())
			if (!n.getNode().isEmpty())
				if (config.get(n.getNode()) != null)
					n.setValue(config.get(n.getNode()));
	}

	public static void save(File file) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (Nodes n : Nodes.values())
			if (!n.getNode().isEmpty())
				config.set(n.getNode(), n.getValue());
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
