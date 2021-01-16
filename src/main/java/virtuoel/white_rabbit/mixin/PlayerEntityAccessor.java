package virtuoel.white_rabbit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor
{
	@Accessor("inventory")
	PlayerInventory getInventory();
	
	@Accessor("abilities")
	PlayerAbilities getAbilities();
}
