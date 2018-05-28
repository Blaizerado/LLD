package at.ltd.adds.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.threading.AsyncWorker;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class Transaction {
	public Connection c = null;

	public synchronized TransactionStatus add(String reason, int amount, Player p) {

		try {
			Integer coins = SQLCollection.getPlayer(p).getCoins();
			coins = coins + amount;
			SQLCollection.getPlayer(p).setCoins(coins);
		} catch (Exception e) {
			e.printStackTrace();
			return TransactionStatus.EXEPTION;
		}
		new AsyncWorker(new Runnable() {

			@Override
			public void run() {
				registerTransaction(reason, "SYSTEM", makeName(p), amount);
			}
		}, true);
		return TransactionStatus.SUCCESS;

	}

	public synchronized TransactionStatus minus(String reason, int amount, Player p) {
		try {
			Integer coins = SQLCollection.getPlayer(p).getCoins();
			if (!hasEnough(coins, amount)) {
				return TransactionStatus.NOTENOUGHCOINS;
			}
			coins = coins - amount;
			SQLCollection.getPlayer(p).setCoins(coins);
		} catch (Exception e) {
			e.printStackTrace();
			return TransactionStatus.EXEPTION;
		}
		AsyncThreadWorkers.submitWork(new Runnable() {

			@Override
			public void run() {
				registerTransaction(reason, makeName(p), "SYSTEM", amount);
			}
		});
		return TransactionStatus.SUCCESS;

	}

	public synchronized TransactionStatus FromTo(String reason, Player from, Player to, int amount) {
		try {
			Integer coinsfrom = SQLCollection.getPlayer(from).getCoins();
			if (!hasEnough(coinsfrom, amount)) {
				return TransactionStatus.NOTENOUGHCOINS;
			}
			coinsfrom = coinsfrom - amount;
			SQLCollection.getPlayer(from).setCoins(coinsfrom);

			Integer coinsto = SQLCollection.getPlayer(to).getCoins();
			coinsto = coinsto + amount;
			SQLCollection.getPlayer(to).setCoins(coinsto);
		} catch (Exception e) {
			e.printStackTrace();
			return TransactionStatus.EXEPTION;
		}
		AsyncThreadWorkers.submitWork(new Runnable() {

			@Override
			public void run() {
				registerTransaction(reason, makeName(from), makeName(to), amount);
			}
		});
		return TransactionStatus.SUCCESS;
	}

	public String makeName(Player p) {
		return p.getUniqueId().toString() + " (" + p.getName() + ")";
	}

	public synchronized Boolean registerTransaction(String reason, String from, String to, Integer amount) {
		sche();
		try {
			if (c == null) {
				MySQL MySQL = new MySQL("ltd-net.eu", "3306", Main.name, Main.name, Main.password);
				c = MySQL.openConnection();
			}
			if (c.isClosed()) {
				MySQL MySQL = new MySQL("ltd-net.eu", "3306", Main.name, Main.name, Main.password);
				c = MySQL.openConnection();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
		try {
			Statement statement = c.createStatement();
			java.util.Date dt = new java.util.Date();

			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			statement.executeUpdate("INSERT INTO `Transactions` (`DATE`,`REASON`, `FROM`,`TO`,`AMOUNT`) VALUES ('" + currentTime + "', '" + reason + "', '" + from + "', '" + to + "', '" + amount + "');");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public synchronized Boolean hasEnough(Integer current, Integer minus) {
		if (current == minus || current > minus) {
			return true;
		} else {
			return false;
		}
	}
	public Integer i = 60;

	public CopyOnWriteArrayList<Integer> id = new CopyOnWriteArrayList<>();
	@SuppressWarnings("deprecation")
	public synchronized void sche() {
		if (i != 60) {
			i = 60;
		} else {
			i = 59;
			id.add(Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getPlugin(), new Runnable() {

				@Override
				public void run() {
					i = i - 1;
					if (i == 0) {
						i = 60;
						try {
							c.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						for (Integer ids : id) {
							try {
								Bukkit.getScheduler().cancelTask(ids);
							} catch (Exception e) {
							}

						}
						id.clear();
					}
				}
			}, 1, 20));
		}

	}

}
