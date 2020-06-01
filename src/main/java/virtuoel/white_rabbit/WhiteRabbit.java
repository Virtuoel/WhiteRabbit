package virtuoel.white_rabbit;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.white_rabbit.api.WhiteRabbitConfig;
import virtuoel.white_rabbit.init.ItemRegistrar;

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
	}
	
	public static Identifier id(String name)
	{
		return new Identifier(MOD_ID, name);
	}
	
	public static int getShrinkDelayTicks(LivingEntity entity, ScaleData scaleData)
	{
		return getDelayTicks(
			getShrinkDelayTicks(),
			WhiteRabbit::getShrinkTargetScale,
			entity, scaleData
		);
	}
	
	public static int getShrinkDelayTicks()
	{
		return getConfigInt("shrinkDelayTicks", 100);
	}
	
	public static float getShrinkTargetScale(LivingEntity entity, ScaleData scaleData)
	{
		return scaleData.getTargetScale() * getShrinkMultiplier();
	}
	
	public static float getShrinkMultiplier()
	{
		return getConfigFloat("shrinkMultiplier", 0.5F);
	}
	
	public static boolean canShrink(LivingEntity entity, ScaleData scaleData)
	{
		return getShrinkTargetScale(entity, scaleData) > getMinScale() - Float.MIN_NORMAL;
	}
	
	public static float getMinScale()
	{
		return getConfigFloat("minScale", 0.015625F);
	}
	
	public static int getGrowthDelayTicks(LivingEntity entity, ScaleData scaleData)
	{
		return getDelayTicks(
			getGrowthDelayTicks(),
			WhiteRabbit::getGrowthTargetScale,
			entity, scaleData
		);
	}
	
	public static int getGrowthDelayTicks()
	{
		return getConfigInt("growthDelayTicks", 100);
	}
	
	public static float getGrowthTargetScale(LivingEntity entity, ScaleData scaleData)
	{
		return scaleData.getTargetScale() * getGrowthMultiplier();
	}
	
	public static float getGrowthMultiplier()
	{
		return getConfigFloat("growthMultiplier", 2.0F);
	}
	
	public static boolean canGrow(LivingEntity entity, ScaleData scaleData)
	{
		return getGrowthTargetScale(entity, scaleData) < getMaxScale() + Float.MIN_NORMAL;
	}
	
	public static float getMaxScale()
	{
		return getConfigFloat("maxScale", 16.0F);
	}
	
	public static int getDelayTicks(int delay, BiFunction<LivingEntity, ScaleData, Float> targetSupplier, LivingEntity entity, ScaleData scaleData)
	{
		if (scaleData.getScale() != scaleData.getTargetScale())
		{
			final float target = targetSupplier.apply(entity, scaleData);
			final float distance = scaleData.getInitialScale() - target;
			final float remaining = scaleData.getScale() - target;
			
			return (int) (Math.abs(remaining / distance) * delay);
		}
		
		return delay;
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
}
