package lizcraft.tinychest.common;

import net.minecraft.entity.player.PlayerEntity;

public interface ITinyChestContainer 
{
	boolean canBeUsed(PlayerEntity player);

	void openContainer(PlayerEntity player);

	void closeContainer(PlayerEntity player);
}
