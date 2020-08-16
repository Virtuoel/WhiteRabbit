package virtuoel.white_rabbit.api;

import java.util.function.Supplier;

import com.google.gson.JsonObject;

import virtuoel.white_rabbit.WhiteRabbit;
import virtuoel.white_rabbit.util.JsonConfigHandler;

public class WhiteRabbitConfig
{
	public static final Supplier<JsonObject> HANDLER =
		new JsonConfigHandler(
			WhiteRabbit.MOD_ID,
			WhiteRabbit.MOD_ID + "/config.json",
			WhiteRabbitConfig::createDefaultConfig
		);
	
	public static final JsonObject DATA = HANDLER.get();
	
	private static JsonObject createDefaultConfig()
	{
		final JsonObject config = new JsonObject();
		
		config.addProperty("resizeBoundsOnly", false);
		config.addProperty("minScale", 0.015625F);
		config.addProperty("maxScale", 16.0F);
		config.addProperty("shrinkMultiplier", 0.5F);
		config.addProperty("growthMultiplier", 2.0F);
		config.addProperty("shrinkDelayTicks", 100);
		config.addProperty("growthDelayTicks", 100);
		
		return config;
	}
}
