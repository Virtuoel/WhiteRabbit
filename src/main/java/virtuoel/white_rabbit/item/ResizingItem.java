package virtuoel.white_rabbit.item;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;

public class ResizingItem extends Item
{
	final BiFunction<LivingEntity, ScaleData, Float> targetScale;
	final BiFunction<LivingEntity, ScaleData, Integer> delayTicks;
	final BiPredicate<LivingEntity, ScaleData> useCondition;
	
	public ResizingItem(Item.Settings settings, BiFunction<LivingEntity, ScaleData, Float> targetScale, BiFunction<LivingEntity, ScaleData, Integer> delayTicks, BiPredicate<LivingEntity, ScaleData> useCondition)
	{
		super(settings);
		this.targetScale = targetScale;
		this.delayTicks = delayTicks;
		this.useCondition = useCondition;
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
	{
		stack = super.finishUsing(stack, world, user);
		
		if (!world.isClient)
		{
			ScaleData scaleData = ScaleData.of(user);
			float target = targetScale.apply(user, scaleData);
			
			if (Float.compare(target, scaleData.getScale()) != 0)
			{
				scaleData.setScaleTickDelay(delayTicks.apply(user, scaleData));
				scaleData.setTargetScale(target);
				scaleData.markForSync();
			}
		}
		
		return stack;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		if (useCondition.test(user, ScaleData.of(user)))
		{
			return super.use(world, user, hand);
		}
		
		return TypedActionResult.pass(user.getStackInHand(hand));
	}
}
