package virtuoel.white_rabbit.item;

import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.white_rabbit.init.ScaleTypeRegistrar;

public class ResizingItem extends Item
{
	final Function<ScaleData, Float> targetScale;
	final Function<ScaleData, Integer> delayTicks;
	final Predicate<ScaleData> useCondition;
	
	public ResizingItem(Item.Settings settings, Function<ScaleData, Float> targetScale, Function<ScaleData, Integer> delayTicks, Predicate<ScaleData> useCondition)
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
			final ScaleData scaleData = ScaleTypeRegistrar.FOOD_TYPE.getScaleData(user);
			
			final int delay = delayTicks.apply(scaleData);
			
			if (delay >= 0)
			{
				scaleData.setScaleTickDelay(delay);
			}
			
			scaleData.setTargetScale(targetScale.apply(scaleData));
		}
		
		return stack;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		if (useCondition.test(ScaleTypeRegistrar.FOOD_TYPE.getScaleData(user)))
		{
			return super.use(world, user, hand);
		}
		
		return new TypedActionResult<>(ActionResult.PASS, user.getStackInHand(hand));
	}
}
