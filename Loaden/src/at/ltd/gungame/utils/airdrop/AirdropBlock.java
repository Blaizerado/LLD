package at.ltd.gungame.utils.airdrop;

import org.bukkit.inventory.ItemStack;

public class AirdropBlock {

	public int xoffset;
	public int yoffset;
	public int zoffset;
	public ItemStack block;

	public AirdropBlock(int x, int y, int z, ItemStack block) {
		this.xoffset = x;
		this.yoffset = y;
		this.zoffset = z;
		this.block = block;
	}

}
