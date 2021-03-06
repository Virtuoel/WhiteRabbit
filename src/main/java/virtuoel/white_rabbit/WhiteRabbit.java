package virtuoel.white_rabbit;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.white_rabbit.api.WhiteRabbitConfig;
import virtuoel.white_rabbit.init.ItemRegistrar;
import virtuoel.white_rabbit.init.ScaleTypeRegistrar;
import virtuoel.white_rabbit.mixin.DispenserBlockAccessor;
import virtuoel.white_rabbit.mixin.FallibleItemDispenserBehaviorAccessor;

public class WhiteRabbit implements ModInitializer
{
	public static final String MOD_ID = "white_rabbit";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public WhiteRabbit()
	{
		WhiteRabbitConfig.DATA.getClass();
	}
	
	@Override
	public void onInitialize()
	{
		ItemRegistrar.INSTANCE.getClass();
		ScaleTypeRegistrar.INSTANCE.getClass();
		
		DispenserBlock.registerBehavior(ItemRegistrar.PISHSALVER, new FallibleItemDispenserBehavior()
		{
			private final ItemDispenserBehavior ejectItem = new ItemDispenserBehavior();
			
			@Override
			protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack)
			{
				final BlockEntity blockEntity = pointer.getBlockEntity();
				final BlockPos pos = pointer.getBlockPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
				final List<Entity> entities = blockEntity.getWorld().getOtherEntities(null, new Box(pos));
				
				((FallibleItemDispenserBehaviorAccessor) (Object) this).setSuccess(false);
				
				for (final Entity target : entities)
				{
					boolean success = false;
					
					final ScaleData scaleData = ScaleTypeRegistrar.FOOD_TYPE.getScaleData(target);
					if (canShrink(scaleData))
					{
						final int delay = getShrinkDelayTicks(scaleData);
						
						if (delay >= 0)
						{
							scaleData.setScaleTickDelay(delay);
						}
						
						scaleData.setTargetScale(getShrinkTargetScale(scaleData));
						success = true;
					}
					
					if (success)
					{
						((FallibleItemDispenserBehaviorAccessor) (Object) this).setSuccess(true);
						stack.decrement(1);
						final ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
						
						if (stack.isEmpty())
						{
							return bottle.copy();
						}
						else
						{
							if (((DispenserBlockEntity) blockEntity).addToFirstFreeSlot(bottle.copy()) < 0)
							{
								final ItemStack result = DispenserBlockAccessor.getBehaviors().get(bottle.getItem()).dispense(pointer, bottle.copy());
								
								if (!result.isEmpty())
								{
									ejectItem.dispense(pointer, result);
								}
							}
							
							break;
						}
					}
				}
				
				return stack;
			}
		});
		
		DispenserBlock.registerBehavior(ItemRegistrar.UPELKUCHEN, new FallibleItemDispenserBehavior()
		{
			@Override
			protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack)
			{
				final BlockPos pos = pointer.getBlockPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
				final List<Entity> entities = pointer.getBlockEntity().getWorld().getOtherEntities(null, new Box(pos));
				
				((FallibleItemDispenserBehaviorAccessor) (Object) this).setSuccess(false);
				
				for (final Entity target : entities)
				{
					boolean success = false;
					
					final ScaleData scaleData = ScaleTypeRegistrar.FOOD_TYPE.getScaleData(target);
					if (canGrow(scaleData))
					{
						final int delay = getGrowthDelayTicks(scaleData);
						
						if (delay >= 0)
						{
							scaleData.setScaleTickDelay(delay);
						}
						
						scaleData.setTargetScale(getGrowthTargetScale(scaleData));
						success = true;
					}
					
					if (success)
					{
						((FallibleItemDispenserBehaviorAccessor) (Object) this).setSuccess(true);
						stack.decrement(1);
						
						break;
					}
				}
				
				return stack;
			}
		});
	}
	
	public static Identifier id(String name)
	{
		return new Identifier(MOD_ID, name);
	}
	
	public static int getShrinkDelayTicks(ScaleData scaleData)
	{
		return getShrinkDelayTicks(
			WhiteRabbit::getShrinkTargetScale,
			scaleData
		);
	}
	
	public static int getShrinkDelayTicks(Function<ScaleData, Float> targetSupplier, ScaleData scaleData)
	{
		return getDelayTicks(
			getShrinkDelayTicks(),
			targetSupplier,
			scaleData
		);
	}
	
	public static int getShrinkDelayTicks()
	{
		return getConfigInt("shrinkDelayTicks", 100);
	}
	
	public static float getShrinkTargetScale(ScaleData scaleData)
	{
		return scaleData.getTargetScale() * getShrinkMultiplier();
	}
	
	public static float getShrinkMultiplier()
	{
		return getConfigFloat("shrinkMultiplier", 0.5F);
	}
	
	public static boolean canShrink(ScaleData scaleData)
	{
		return !isResizing(scaleData) && getShrinkTargetScale(scaleData) >= getMinScale();
	}
	
	public static float getMinScale()
	{
		return getConfigFloat("minScale", 0.015625F);
	}
	
	public static int getGrowthDelayTicks(ScaleData scaleData)
	{
		return getGrowthDelayTicks(
			WhiteRabbit::getGrowthTargetScale,
			scaleData
		);
	}
	
	public static int getGrowthDelayTicks(Function<ScaleData, Float> targetSupplier, ScaleData scaleData)
	{
		return getDelayTicks(
			getGrowthDelayTicks(),
			targetSupplier,
			scaleData
		);
	}
	
	public static int getGrowthDelayTicks()
	{
		return getConfigInt("growthDelayTicks", 100);
	}
	
	public static float getGrowthTargetScale(ScaleData scaleData)
	{
		return scaleData.getTargetScale() * getGrowthMultiplier();
	}
	
	public static float getGrowthMultiplier()
	{
		return getConfigFloat("growthMultiplier", 2.0F);
	}
	
	public static boolean canGrow(ScaleData scaleData)
	{
		return !isResizing(scaleData) && getGrowthTargetScale(scaleData) <= getMaxScale();
	}
	
	public static float getMaxScale()
	{
		return getConfigFloat("maxScale", 16.0F);
	}
	
	public static int getDelayTicks(int delay, Function<ScaleData, Float> targetSupplier, ScaleData scaleData)
	{
		if (Float.compare(scaleData.getScale(), scaleData.getTargetScale()) != 0)
		{
			final float target = targetSupplier.apply(scaleData);
			final float distance = scaleData.getInitialScale() - target;
			final float remaining = scaleData.getScale() - target;
			
			return (int) (Math.abs(remaining / distance) * delay);
		}
		
		return delay;
	}
	
	public static boolean isResizing(ScaleData scaleData)
	{
		return Float.compare(scaleData.getBaseScale(), scaleData.getTargetScale()) != 0;
	}
	
	public static float getConfigFloat(String name, float defaultValue)
	{
		return getConfigNumber(name, Number::floatValue, defaultValue);
	}
	
	public static int getConfigInt(String name, int defaultValue)
	{
		return getConfigNumber(name, Number::intValue, defaultValue);
	}
	
	public static <T> T getConfigNumber(String name, Function<Number, T> valueFunc, T defaultValue)
	{
		return Optional.ofNullable(WhiteRabbitConfig.DATA.get(name))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isNumber).map(JsonPrimitive::getAsNumber)
			.map(valueFunc)
			.orElse(defaultValue);
	}
	
	public static boolean getConfigBoolean(String name, boolean defaultValue)
	{
		return Optional.ofNullable(WhiteRabbitConfig.DATA.get(name))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(defaultValue);
	}
}
