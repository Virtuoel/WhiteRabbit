package virtuoel.white_rabbit;

import java.util.Optional;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.fabricmc.api.ModInitializer;
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
		return getShrinkTargetScale(scaleData) >= getMinScale();
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
		return getGrowthTargetScale(scaleData) <= getMaxScale();
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
