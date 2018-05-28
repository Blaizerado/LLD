package at.ltd.adds.utils.visual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;

public class InventoryAnimation {

	private static final int SLOTS_IN_ROW = 9;

	public enum AnimationType {
		UP_TO_DOWN, LEFT_TO_RIGHT, DOWN_TO_UP, RIGHT_TO_LEFT
	}

	private final HashMap<Integer, ItemStack> ITEMS = new HashMap<>();
	private boolean ANIMATION_RUN = false;

	private int ANIMATION_SLOT_POS_X = 0;
	private int ANIMATION_SLOT_POS_Y = 0;
	private int ANIMATION_LAST_SLOT = 0;

	private Runnable onFinish = null;

	private int SCHE_ID = 0;

	public void put(int slot, ItemStack is) {
		ITEMS.put(slot, is);
	}

	public void remove(int slot) {
		ITEMS.remove(slot);
	}

	public ItemStack get(int slot) {
		return ITEMS.get(slot);
	}

	public boolean contains(int slot) {
		return ITEMS.containsKey(slot);
	}

	public HashMap<Integer, ItemStack> getItems() {
		return ITEMS;
	}

	public void animateRandomly(Inventory inv, Runnable onFinish) {
		int randomNum = ThreadLocalRandom.current().nextInt(0, 7 + 1);
		if (randomNum == 0) {
			animate(inv, AnimationType.DOWN_TO_UP, true, 2, onFinish);
		}
		if (randomNum == 1) {
			animate(inv, AnimationType.UP_TO_DOWN, true, 2, onFinish);
		}
		if (randomNum == 2) {
			animate(inv, AnimationType.LEFT_TO_RIGHT, true, 1, onFinish);
		}
		if (randomNum == 3) {
			animate(inv, AnimationType.RIGHT_TO_LEFT, true, 1, onFinish);
		}
		
		if(inv.getSize() < 40) {
			if (randomNum == 4) {
				animate(inv, AnimationType.DOWN_TO_UP, false, 1, onFinish);
			}
			if (randomNum == 5) {
				animate(inv, AnimationType.UP_TO_DOWN, false, 1, onFinish);
			}
			if (randomNum == 6) {
				animate(inv, AnimationType.LEFT_TO_RIGHT, false, 1, onFinish);
			}
			if (randomNum == 7) {
				animate(inv, AnimationType.RIGHT_TO_LEFT, false, 1, onFinish);
			}
			
		}else {
			animate(inv, AnimationType.LEFT_TO_RIGHT, true, 1, onFinish);
		}
		

	}

	public void animate(Inventory inv, AnimationType type, boolean rowOrSlot, int ticksbetween, Runnable onFinish) {
		if (ANIMATION_RUN) {
			throw new IllegalAccessError("Animation is running!");
		}

		this.onFinish = onFinish;
		ANIMATION_RUN = true;
		int INV_SIZE = inv.getSize();

		SCHE_ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			if (rowOrSlot) {
				if (type == AnimationType.LEFT_TO_RIGHT | type == AnimationType.RIGHT_TO_LEFT) {
					int rows = (INV_SIZE + 1) / 9;
					for (int i = 0; i < rows; i++) {
						int slot = getNextSlot(INV_SIZE, type);
						if (slot == -1) {
							stop();
							break;
						}
						if (contains(slot)) {
							inv.setItem(slot, get(slot));
						}

					}
				} else {
					for (int i = 0; i < 9; i++) {
						int slot = getNextSlot(INV_SIZE, type);
						if (slot == -1) {
							stop();
							break;
						}
						if (contains(slot)) {
							inv.setItem(slot, get(slot));
						}
					}
				}
			} else {

				int slot = getNextSlot(INV_SIZE, type);

				if (slot == -1) {
					stop();
				}

				if (contains(slot)) {
					inv.setItem(slot, get(slot));
				}
			}
		}, ticksbetween, ticksbetween);

	}

	public boolean isRunning() {
		return ANIMATION_RUN;
	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(SCHE_ID);
		SCHE_ID = 0;
		ANIMATION_SLOT_POS_X = 0;
		ANIMATION_SLOT_POS_Y = 0;
		ANIMATION_LAST_SLOT = 0;
		try {
			if (onFinish != null) {
				onFinish.run();
			}
		} finally {
			onFinish = null;
			ANIMATION_RUN = false;
		}

	}

	private int getNextSlot(int INV_SIZE, AnimationType type) {

		if (type == AnimationType.DOWN_TO_UP) {
			ANIMATION_SLOT_POS_X++;
			if (ANIMATION_SLOT_POS_X - 1 > INV_SIZE) {
				return -1;
			}
			return INV_SIZE - (ANIMATION_SLOT_POS_X - 1);
		}
		
		if (type == AnimationType.LEFT_TO_RIGHT) {
			int rows = (INV_SIZE + 1) / 9;
			int slot = ((ANIMATION_SLOT_POS_Y * 9) + ANIMATION_SLOT_POS_X);

			ANIMATION_SLOT_POS_Y++;
			if (ANIMATION_SLOT_POS_X == 9) {
				return -1;
			}
			if (divideAble(ANIMATION_SLOT_POS_Y, rows)) {
				ANIMATION_SLOT_POS_X = ANIMATION_SLOT_POS_X + 1;
				ANIMATION_SLOT_POS_Y = 0;
			}

			ANIMATION_LAST_SLOT = slot;
			return slot;
		}

		if (type == AnimationType.RIGHT_TO_LEFT) {
			int rows = (INV_SIZE + 1) / 9;
			int slot = (getLastSlotFromRow(ANIMATION_SLOT_POS_Y) - ANIMATION_SLOT_POS_X);
			ANIMATION_SLOT_POS_Y++;
			if (divideAble(ANIMATION_SLOT_POS_Y, rows)) {
				ANIMATION_SLOT_POS_X = ANIMATION_SLOT_POS_X + 1;
				ANIMATION_SLOT_POS_Y = 0;
			}
			ANIMATION_LAST_SLOT = slot;
			return slot;
		}

		if (type == AnimationType.UP_TO_DOWN) {
			ANIMATION_SLOT_POS_X++;
			if (ANIMATION_SLOT_POS_X - 1 > INV_SIZE) {
				return -1;
			}
			return ANIMATION_SLOT_POS_X - 1;
		}

		return -1;
	}

	private static int getLastSlotFromRow(int row) {
		row++;
		return row * SLOTS_IN_ROW - 1;
	}

	private static int getFirstSlotFromRow(int row) {
		row++;
		return row * SLOTS_IN_ROW - SLOTS_IN_ROW;
	}

	private static boolean divideAble(int fir, int sec) {
		return fir % sec == 0;
	}

	private ArrayList<Integer> getAllNumbers(int first, int second) {
		ArrayList<Integer> list = new ArrayList<>();
		if (first == second) {
			list.add(first);
			return list;
		}

		list.add(first);

		int pos = 0;

		while (true) {
			pos++;
			int cur = first + pos;
			if (cur >= second) {
				break;
			}
			list.add(cur);
		}
		list.add(second);
		return list;
	}
}
