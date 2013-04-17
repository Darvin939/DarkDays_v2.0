package darvin939.DarkDays.Players.Sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import darvin939.DarkDays.Utils.PatPeter.SQLibrary.Database;

public class PlayerManager {
	private static final PlayerManager instance = new PlayerManager();
	public final Map<String, DDPlayer> players = new HashMap<String, DDPlayer>();
	public final static Map<String, Long> playersID = new HashMap<String, Long>();
	private final PreparedStatement insertPlayer;

	public static PlayerManager getInstance() {
		return instance;
	}

	private PlayerManager() {
		insertPlayer = Database.DATABASE.prepare("INSERT INTO `players` (`username`) VALUES (?)");
	}

	public static void init() {
		final ResultSet rs = Database.DATABASE.query("SELECT `username`,`id` FROM `players`");
		try {
			while (rs.next()) {
				playersID.put(rs.getString("username"), rs.getLong("id"));
			}
			rs.close();
		} catch (final SQLException e) {
		}
	}

	public DDPlayer addPlayer(String player) {
		addExtPlayer(player);
		final Long id = getPlayerID(player);
		DDPlayer play = new SQLPlayer(player, id);
		players.put(player, play);
		return play;
	}

	public DDPlayer addPlayer(Player player) {
		addExtPlayer(player.getName());
		final Long id = getPlayerID(player);
		DDPlayer play = new SQLPlayer(player, id);
		players.put(player.getName(), play);
		return play;
	}

	public DDPlayer getPlayer(String player) {
		return players.get(player);
	}

	public Long getPlayerID(String player) {
		return playersID.get(player);
	}

	public DDPlayer getPlayer(Player player) {
		return players.get(player.getName());
	}

	public Long getPlayerID(Player player) {
		return playersID.get(player.getName());
	}

	private void addExtPlayer(String player) {
		if (!playersID.containsKey(player)) {
			ResultSet rs = null;
			try {
				synchronized (insertPlayer) {
					insertPlayer.clearParameters();
					insertPlayer.setString(1, player);
					insertPlayer.executeUpdate();
					rs = insertPlayer.getGeneratedKeys();
					if (rs.next()) {
						playersID.put(player, rs.getLong(1));
					}
					if (rs != null) {
						rs.close();
					}
				}
			} catch (final SQLException e) {
				if (rs != null) {
					try {
						rs.close();
					} catch (final SQLException e1) {
					}
				}
			}
		}

	}
}
