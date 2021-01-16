package virtuoel.white_rabbit.item;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.white_rabbit.init.ScaleTypeRegistrar;
import virtuoel.white_rabbit.mixin.PlayerEntityAccessor;

public class ResizingDrinkItem extends ResizingItem
{
	final Supplier<ItemStack> resultStack;
	
	public ResizingDrinkItem(Item.Settings settings, Function<ScaleData, Float> targetScale, Function<ScaleData, Integer> delayTicks, Predicate<ScaleData> useCondition, Supplier<ItemStack> resultStack)
	{
		super(settings, targetScale, delayTicks, useCondition);
		this.resultStack = resultStack;
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
	{
		stack = super.finishUsing(stack, world, user);
		
		if (user instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) user;
			Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		
		if (stack.isEmpty())
		{
			return resultStack.get();
		}
		else if (user instanceof PlayerEntity)
		{
			final PlayerEntityAccessor playerEntity = (PlayerEntityAccessor) user;
			
			if (!playerEntity.getAbilities().creativeMode)
			{
				final ItemStack result = resultStack.get();
				if (!playerEntity.getInventory().insertStack(result))
				{
					((PlayerEntity) user).dropItem(result, false);
				}
			}
		}
		
		return stack;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.DRINK;
	}
	
	@Override
	public SoundEvent getEatSound()
	{
		return SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK;
	}
	
	@Override
	public SoundEvent getDrinkSound()
	{
		return SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		if (useCondition.test(ScaleTypeRegistrar.FOOD_TYPE.getScaleData(user)))
		{
			user.setCurrentHand(hand);
			return new TypedActionResult<>(ActionResult.SUCCESS, user.getStackInHand(hand));
		}
		
		return new TypedActionResult<>(ActionResult.PASS, user.getStackInHand(hand));
	}
}
