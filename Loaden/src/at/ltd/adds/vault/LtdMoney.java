package at.ltd.adds.vault;

import java.util.List;

import org.bukkit.OfflinePlayer;

import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class LtdMoney implements Economy {

	@Override
	public EconomyResponse bankBalance(String arg0) {
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String arg0, double arg1) {
		return null;
	}

	@Override
	public EconomyResponse bankHas(String arg0, double arg1) {
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		return null;
	}

	@Override
	public EconomyResponse createBank(String arg0, String arg1) {
		return null;
	}

	@Override
	public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
		return null;
	}

	@Override
	public boolean createPlayerAccount(String arg0) {
		return true;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0) {
		return true;
	}

	@Override
	public boolean createPlayerAccount(String arg0, String arg1) {
		return true;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
		return true;
	}

	@Override
	public String currencyNamePlural() {
		return "Coins";
	}

	@Override
	public String currencyNameSingular() {
		return "Coin";
	}

	@Override
	public EconomyResponse deleteBank(String arg0) {
		return new EconomyResponse(0, 0, ResponseType.FAILURE, "");
	}

	@Override
	public EconomyResponse depositPlayer(String arg0, double arg1) {
		throw new IllegalAccessError("no");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player2, double money) {
		SQLPlayer player = SQLCollection.getPlayer(player2.getPlayer());
		System.out.println(player.getCoins() + "  " + money);
		player.setCoins((int) (player.getCoins() + money));
		System.out.println(player.getCoins());
		return new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
		throw new IllegalAccessError("no");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		throw new IllegalAccessError("no");
	}

	@Override
	public String format(double arg0) {
		throw new IllegalAccessError("no");
	}

	@Override
	public int fractionalDigits() {
		throw new IllegalAccessError("no");
	}

	@Override
	public double getBalance(String arg0) {
		throw new IllegalAccessError("no");
	}

	@Override
	public double getBalance(OfflinePlayer player2) {
		SQLPlayer player = SQLCollection.getPlayer(player2.getPlayer());
		return player.getCoins();
	}

	@Override
	public double getBalance(String arg0, String arg1) {
		throw new IllegalAccessError("no");
	}

	@Override
	public double getBalance(OfflinePlayer arg0, String arg1) {
		throw new IllegalAccessError("no");
	}

	@Override
	public List<String> getBanks() {
		return null;
	}

	@Override
	public String getName() {
		return "LTD";
	}

	@Override
	public boolean has(String arg0, double arg1) {
		throw new IllegalAccessError("no");
	}

	@Override
	public boolean has(OfflinePlayer arg0, double arg1) {
		throw new IllegalAccessError("no");
	}

	@Override
	public boolean has(String arg0, String arg1, double arg2) {
		throw new IllegalAccessError("no");
	}

	@Override
	public boolean has(OfflinePlayer arg0, String arg1, double arg2) {
		throw new IllegalAccessError("no");
	}

	@Override
	public boolean hasAccount(String arg0) {
		return true;
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0) {
		return true;
	}

	@Override
	public boolean hasAccount(String arg0, String arg1) {
		return true;
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0, String arg1) {
		return true;
	}

	@Override
	public boolean hasBankSupport() {
		return true;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public EconomyResponse withdrawPlayer(String uuid, double money) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Only Online Players");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player2, double money) {
		SQLPlayer player = SQLCollection.getPlayer(player2.getPlayer());
		player.setCoins((int) (player.getCoins() - money));
		return new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Only Online Players");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Only Online Players");
	}

}
