package virtuoel.white_rabbit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;

@Mixin(ItemDispenserBehavior.class)
public interface ItemDispenserBehaviorInvoker
{
	@Invoker
	ItemStack callDispenseSilently(BlockPointer pointer, ItemStack stack);
}
