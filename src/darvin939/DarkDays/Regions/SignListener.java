package darvin939.DarkDays.Regions;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import darvin939.DarkDays.DarkDays;
import darvin939.DarkDays.Regions.Memory.SignRegionData;
import darvin939.DarkDays.Utils.Util;

public class SignListener extends RegionManager implements Listener {

	public SignListener(DarkDays plg) {
		super(plg);
	}

	public static void entityRespawnTask() {
		for (SignRegionData srd : sData.values()) {
			if (srd.isChunkLoaded()) {
				if (!srd.isMaxMobCount()) {
					System.out.println("Spawn Mob");
					Double[] randomXZ = srd.getRandomPoint();
					if (randomXZ != null) {
						srd.getWorld().spawn(new Location(srd.getWorld(), randomXZ[0], srd.getHighestBlock().getY() + 1, randomXZ[1]), (Class<? extends Entity>) Zombie.class);
						srd.addMod();
					}
				}
			}
		}
	}

	// @EventHandler(priority = EventPriority.NORMAL)
	// public void onEntitySpawn(CreatureSpawnEvent event) {
	// event.setCancelled(insideSignRegion(event.getLocation()));
	// }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent event) {
		Player p = event.getPlayer();
		if (ChatColor.stripColor(event.getLine(0)).equalsIgnoreCase("[dd]")) {
			if (!plg.hasPermissions(p, "sign.create")) {
				event.setLine(0, "{dd}");
			} else {
				setLine(event, 0, "&9[DarkDays]");

				boolean r = false, s = false, m = false, error = false;
				int ri = 0, mi = 0;
				boolean sb = false;

				for (int i = 0; i < 4; i++) {
					String line = ChatColor.stripColor(event.getLine(i));
					String[] params = line.split(" ");
					if (params.length > 0) {
						for (String pr : params) {
							if (pr.startsWith("radius=") || pr.startsWith("r=")) {
								if (!r) {
									try {
										ri = Integer.parseInt(pr.replace("radius=", "").replace("r=", ""));
										r = true;

									} catch (NumberFormatException e) {
										error = true;
									}
								}
							}
							if (pr.startsWith("spawn=") || pr.startsWith("s=")) {
								if (!s) {
									String srepl = pr.replace("spawn=", "").replace("s=", "");
									if (srepl.equalsIgnoreCase("true") || srepl.equalsIgnoreCase("false")) {
										sb = Boolean.parseBoolean(srepl);
										s = true;
									} else
										error = true;
								}
							}
							if (pr.startsWith("max=") || pr.startsWith("m=") || pr.startsWith("maxzombies=")) {
								if (!m) {
									try {
										mi = Integer.parseInt(pr.replace("max=", "").replace("m=", "").replace("maxzombies=", ""));
										m = true;
									} catch (NumberFormatException e) {
										error = true;
									}
								}
							}
						}
					}
				}
				if (!r || error) {
					clearParamLines(event);
					setLine(event, 2, "{&4Error&0}");
				} else {
					clearParamLines(event);
					setLine(event, 1, "Radius=&6" + ri);
					if (!sb)
						setLine(event, 2, "&mSpawn=" + sb);
					else
						setLine(event, 2, "Spawn=&6" + sb);
					if (sb)
						setLine(event, 3, "MaxZmbs=&6" + mi);

					Location l = event.getBlock().getLocation();

					sData.put(l.toString(), new SignRegionData(l, ri, mi, sb));

					String sLoc = new StringBuilder().append(l.getWorld().getName()).append(" ").append(l.getX()).append(" ").append(l.getY()).append(" ").append(l.getZ()).toString();

					File f = new File(plg.getDataFolder() + File.separator + "signs.dat");
					if (!f.exists())
						try {
							f.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}

					String data = loadSignData();
					if (data.isEmpty())
						data = sLoc + ";";
					else
						data = data + sLoc + ";";
					saveSignData(data);
				}

			}
		} else if ((ChatColor.stripColor(event.getLine(0)).equalsIgnoreCase("[DarkDays]"))) {
			event.setLine(0, "{DarkDays}");
			clearParamLines(event);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		Player p = event.getPlayer();
		Location loc = b.getLocation();
		if ((b.getType() == Material.SIGN_POST) || (b.getType() == Material.WALL_SIGN)) {
			BlockState state = b.getState();
			if ((state instanceof Sign)) {
				Sign sign = (Sign) state;
				if (ChatColor.stripColor(sign.getLine(2)).equalsIgnoreCase("{Error}") && ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[DarkDays]")) {
					return;
				}
				if (plg.hasPermissions(p, "sign.destroy")) {

					if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[DarkDays]")) {
						String sLoc = new StringBuilder().append(loc.getX()).append(" ").append(loc.getY()).append(" ").append(loc.getZ()).toString();
						String data = loadSignData();
						saveSignData(data.replaceAll(sLoc + ";", ""));
						sData.remove(loc.toString());
						Util.Print(p, "You destroyed the DarkDays Sign!");
					}
				} else {
					Util.Print(p, "You can't destroy the DarkDays Sign!");
					event.setCancelled(true);
				}

			}
		}
	}

	public void clearParamLines(SignChangeEvent event) {
		event.setLine(1, "");
		event.setLine(2, "");
		event.setLine(3, "");
	}

	public void setLine(SignChangeEvent event, int n, String text) {
		event.setLine(n, ChatColor.translateAlternateColorCodes('&', text));
	}
}