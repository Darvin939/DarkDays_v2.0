package darvin939.DarkDays.Commands;

import org.bukkit.entity.Player;

import darvin939.DarkDays.DarkDays;

public abstract class Handler {

	protected final DarkDays plugin;

	public Handler(DarkDays plugin) {
		this.plugin = plugin;
	}

	public abstract boolean perform(Player p, String[] args) throws InvalidUsage;

	protected boolean hasPermissions(Player p, String command, Boolean mess) {
		return plugin.hasPermissions(p, command, mess);
	}
	
	protected void getHelp(Player p, String command){
		plugin.getHelp(p, command);
	}
}