package virtuoel.white_rabbit.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;

@Mixin(DispenserBlock.class)
public interface DispenserBlockAccessor
{
	@Accessor("BEHAVIORS")
	public static Map<Item, DispenserBehavior> getBehaviors()
	{
		throw new UnsupportedOperationException();
	}
}
