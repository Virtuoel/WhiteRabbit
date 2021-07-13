package virtuoel.white_rabbit;

import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		WhiteRabbitConfig.BUILDER.config.get();
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
	
	public static Identifier id(String path)
	{
		return new Identifier(MOD_ID, path);
	}
	
	public static Identifier id(String path, String... paths)
	{
		return id(paths.length == 0 ? path : path + "/" + String.join("/", paths));
	}
	
	public static int getShrinkDelayTicks(ScaleData scaleData)
	{
		return getDelayTicks(
			WhiteRabbitConfig.COMMON.shrinkDelayTicks.get(),
			WhiteRabbit::getShrinkTargetScale,
			scaleData
		);
	}
	
	public static float getShrinkTargetScale(ScaleData scaleData)
	{
		return (float) (scaleData.getTargetScale() * WhiteRabbitConfig.COMMON.shrinkMultiplier.get());
	}
	
	public static boolean canShrink(ScaleData scaleData)
	{
		return !isResizing(scaleData) && getShrinkTargetScale(scaleData) >= WhiteRabbitConfig.COMMON.minScale.get();
	}
	
	public static int getGrowthDelayTicks(ScaleData scaleData)
	{
		return getDelayTicks(
			WhiteRabbitConfig.COMMON.growthDelayTicks.get(),
			WhiteRabbit::getGrowthTargetScale,
			scaleData
		);
	}
	
	public static float getGrowthTargetScale(ScaleData scaleData)
	{
		return (float) (scaleData.getTargetScale() * WhiteRabbitConfig.COMMON.growthMultiplier.get());
	}
	
	public static boolean canGrow(ScaleData scaleData)
	{
		return !isResizing(scaleData) && getGrowthTargetScale(scaleData) <= WhiteRabbitConfig.COMMON.maxScale.get();
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
}
