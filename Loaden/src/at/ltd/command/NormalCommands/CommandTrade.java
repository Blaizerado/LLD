package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.TransactionStatus;
import at.ltd.adds.utils.threading.SyncWorker;

public class CommandTrade implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (args.size() == 1 || args.size() == 2) {
			p.sendMessage(Main.getPrefix() + "/trade <Player> <Coins>");
			return;
		} else {
			if (args.size() == 3) {
				if (Bukkit.getPlayer(args.get(1)) != null) {
					Player res = Bukkit.getPlayer(args.get(1));
					Integer am;
					try {
						am = Integer.valueOf(args.get(2));
					} catch (Exception e) {
						Cf.rsS(Cf.TRADE_ERROR_INVALID_INFORMATION, p);
						return;
					}

					if (res.getUniqueId().equals(p.getUniqueId())) {
						Cf.rsS(Cf.TRADE_OWN_ACC, p);
						return;
					}
					if (am < 1) {
						Cf.rsS(Cf.TRADE_CANCEL, p);
						return;
					}
					if (am < 15) {
						Cf.rsS(Cf.TRADE_UNDER_15, p);
						return;
					}
					new SyncWorker(new Runnable() {

						@Override
						public void run() {
							TransactionStatus ts = Main.getTransaction().FromTo("TRADE", p, res, am);
							if (ts == TransactionStatus.EXEPTION) {
								Cf.rsS(Cf.TRADE_EXEPTION, p);
							}
							if (ts == TransactionStatus.NOTENOUGHCOINS) {
								Cf.rsS(Cf.TRADE_NOT_ENOUGHCOINS, p);
							}
							if (ts == TransactionStatus.SUCCESS) {
								Cf.rsS(Cf.TRADE_SUCCESS_1, p);
								Cf.rsS(Cf.TRADE_SUCCESS_2, res, "[COINSADD]", am, "[TRADER]", p.getName());
							}
						}
					}, true);

				} else {
					Cf.rsS(Cf.TRADE_PLAYER_NOTFOUND, p);
				}
			} else {
				p.sendMessage(Main.getPrefix() + "/trade <Player> <Coins>");
			}
		}

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
