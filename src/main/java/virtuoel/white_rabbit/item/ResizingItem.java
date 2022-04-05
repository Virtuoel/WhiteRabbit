package virtuoel.white_rabbit.item;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.white_rabbit.api.WhiteRabbitConfig;
import virtuoel.white_rabbit.init.ScaleTypeRegistrar;
import virtuoel.white_rabbit.init.StatusEffectRegistrar;

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
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText(getTranslationKey() + ".tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
	{
		if (!useCondition.test(ScaleTypeRegistrar.FOOD_TYPE.getScaleData(user)))
		{
			return stack;
		}
		
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
			
			final int effectDuration = WhiteRabbitConfig.COMMON.resizingEffectDuration.get();
			
			if (effectDuration > 0)
			{
				user.removeStatusEffectInternal(StatusEffectRegistrar.RESIZING);
				user.addStatusEffect(new StatusEffectInstance(StatusEffectRegistrar.RESIZING, delay + effectDuration, 0, false, false, true));
			}
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
