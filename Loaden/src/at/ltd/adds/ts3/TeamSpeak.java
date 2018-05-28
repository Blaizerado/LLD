package at.ltd.adds.ts3;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import at.ltd.adds.utils.threading.AsyncWorker;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class TeamSpeak {

	public static TS3Api API;
	public static TS3Query QUERY;
	public static TS3ApiAsync ASYNC_API;
	public static HashMap<Integer, ClientInfo> INFO = new HashMap<>();
	public static HashMap<String, Player> IP_TABLES_MC = new HashMap<>();
	public static HashMap<String, ClientInfo> IP_TABLES_TS = new HashMap<>();
	public static int MY_ID;

	public static final int MC_VER_GROUP_ID = 20;
	public static final int MC_ONL_GROUP_ID = 24;

	public static void init() {
		new AsyncWorker(() -> {

			TS3Config config = new TS3Config();
			config.setHost("ltd-net.eu");
			config.setReconnectStrategy(ReconnectStrategy.constantBackoff());
			config.setFloodRate(FloodRate.UNLIMITED);
			QUERY = new TS3Query(config);
			QUERY.connect();

			API = QUERY.getApi();
			API.login("LTD_BOT", "cEhGJCgU");
			ASYNC_API = QUERY.getAsyncApi();
			ASYNC_API.selectVirtualServerById(1);
			API.selectVirtualServerById(1);
			API.setNickname("LTD-NET | BOT Sync");
			ASYNC_API.setNickname("LTD-NET | BOT");
			MY_ID = API.whoAmI().getId();

			AsyncThreadWorkers.submitSchedulerWorkSec(() -> {
				HashMap<Integer, ClientInfo> info = new HashMap<>();
				for (Client client : API.getClients()) {
					info.put(client.getId(), API.getClientInfo(client.getId()));
				}
				synchronized (INFO) {
					INFO = info;
				}
			}, 0, 1);

			TeamSpeakWatcher.init();
			TeamSpeakVeri.init();

			ASYNC_API.addTS3Listeners(new TeamSpeakEvent());
			ASYNC_API.registerAllEvents();

		}, true);

		AsyncThreadWorkers.submitSchedulerWorkSec(() -> {
			synchronized (IP_TABLES_MC) {
				IP_TABLES_MC.clear();
				for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
					IP_TABLES_MC.put(getIPViaPlayer(p), p);
				}
			}
			synchronized (IP_TABLES_TS) {
				IP_TABLES_TS.clear();
				for (Client ci : INFO.values()) {
					if (!ci.isServerQueryClient()) {
						String ip = ci.getIp();
						IP_TABLES_TS.put(ip, getInfo(ci.getId()));
					}
				}
			}

		}, 2, 1);

	}

	public static String getIPViaPlayer(Player p) {
		InetSocketAddress IPAdressPlayer = p.getAddress();
		String sfullip = IPAdressPlayer.toString();
		String[] fullip;
		String[] ipandport;
		fullip = sfullip.split("/");
		String sIpandPort = fullip[1];
		ipandport = sIpandPort.split(":");
		String sIp = ipandport[0];
		return sIp;
	}

	public static boolean isOnlineMC(String ip) {
		synchronized (IP_TABLES_MC) {
			return IP_TABLES_MC.containsKey(ip);
		}
	}

	public static Player getPlayerViaMC(String ip) {
		synchronized (IP_TABLES_MC) {
			return IP_TABLES_MC.get(ip);
		}
	}

	public static boolean isOnlineTS(String ip) {
		synchronized (IP_TABLES_TS) {
			return IP_TABLES_TS.containsKey(ip);
		}
	}

	public static ClientInfo getClientInfoViaIP(String ip) {
		synchronized (IP_TABLES_TS) {
			return IP_TABLES_TS.get(ip);
		}
	}

	public static ClientInfo getInfo(int id) {
		synchronized (INFO) {
			return INFO.get(id);
		}
	}

	public static boolean containsClientInfo(int id) {
		synchronized (INFO) {
			return INFO.containsKey(id);
		}
	}

	public static void updateInfo(int id) {
		synchronized (INFO) {
			if (INFO.containsKey(id)) {
				INFO.remove(id);
			}
			INFO.put(id, API.getClientInfo(id));
		}
	}

	public static boolean isPlayerOnTeamSpeak(Player p) {
		return isOnlineMC(getIPViaPlayer(p));
	}

	public static HashMap<Integer, ClientInfo> getAllClientInfos() {
		synchronized (INFO) {
			return (HashMap<Integer, ClientInfo>) INFO.clone();
		}
	}

	public static void sendMessgae(String text, int id) {
		ASYNC_API.sendTextMessage(TextMessageTargetMode.CLIENT, id, text);
	}
	
	public static void sendMessgae(String text, ClientInfo id) {
		ASYNC_API.sendTextMessage(TextMessageTargetMode.CLIENT, id.getId(), text);
	}

	public static void addClientToServerGroup(int gid, ClientInfo ci) {
		ASYNC_API.addClientToServerGroup(gid, ci.getDatabaseId());
	}

	public static void removeClientFromServerGroup(int gid, ClientInfo ci) {
		ASYNC_API.removeClientFromServerGroup(gid, ci.getDatabaseId());
	}

	public static boolean isVerified(ClientInfo ci) {
		String des = ci.getDescription();
		return des != null && des.startsWith("Minecraft Verified | UUID: ")
				&& TeamSpeakWatcher.contains(ci.getServerGroups(), TeamSpeak.MC_VER_GROUP_ID);
	}

	public static String getUUID(ClientInfo ci) {
		return ci.getDescription().replace("Minecraft Verified | UUID: ", "");
	}

	public static boolean isSameHuman(ClientInfo ci, Player p) {
		return p.getUniqueId().toString().equals(getUUID(ci));
	}

	public static boolean isOnlineAndVeri(Player p) {
		for (ClientInfo ci : TeamSpeak.getAllClientInfos().values()) {
			if (isVerified(ci)) {
				if (isSameHuman(ci, p)) {
					return true;
				}
			}
		}
		return false;
	}

	public static ClientInfo getClientInfoFromVeriPlayer(Player p) {
		for (ClientInfo ci : TeamSpeak.getAllClientInfos().values()) {
			if (isVerified(ci)) {
				if (isSameHuman(ci, p)) {
					return ci;
				}
			}
		}
		return null;
	}

}
