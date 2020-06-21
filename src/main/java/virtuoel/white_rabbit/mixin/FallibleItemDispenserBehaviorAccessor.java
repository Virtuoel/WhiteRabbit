package virtuoel.white_rabbit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;

@Mixin(FallibleItemDispenserBehavior.class)
public interface FallibleItemDispenserBehaviorAccessor
{
	@Accessor
	void setSuccess(boolean value);
}
