package virtuoel.white_rabbit.api;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import virtuoel.white_rabbit.WhiteRabbit;
import virtuoel.white_rabbit.util.JsonConfigHandler;

public class WhiteRabbitConfig
{
	public static final Client CLIENT = new Client();
	public static final Common COMMON = new Common();
	public static final Server SERVER = new Server();
	
	public static class Client
	{
		Client()
		{
			
		}
	}
	
	public static class Common
	{
		public final Supplier<Boolean> resizeBoundsOnly;
		public final Supplier<Double> minScale;
		public final Supplier<Double> maxScale;
		public final Supplier<Double> shrinkMultiplier;
		public final Supplier<Double> growthMultiplier;
		public final Supplier<Integer> shrinkDelayTicks;
		public final Supplier<Integer> growthDelayTicks;
		
		Common()
		{
			this.resizeBoundsOnly = booleanConfig("resizeBoundsOnly", false);
			this.minScale = doubleConfig("minScale", 0.015625D);
			this.maxScale = doubleConfig("maxScale", 16.0D);
			this.shrinkMultiplier = doubleConfig("shrinkMultiplier", 0.5D);
			this.growthMultiplier = doubleConfig("growthMultiplier", 2.0D);
			this.shrinkDelayTicks = intConfig("shrinkDelayTicks", 100);
			this.growthDelayTicks = intConfig("growthDelayTicks", 100);
		}
	}
	
	public static class Server
	{
		Server()
		{
			
		}
	}
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public static final Supplier<JsonObject> HANDLER = createConfig();
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public static final JsonObject DATA = HANDLER.get();
	
	private static Supplier<JsonObject> createConfig()
	{
		return new JsonConfigHandler(
			WhiteRabbit.MOD_ID,
			"config.json",
			WhiteRabbitConfig::createDefaultConfig
		);
	}
	
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
	
	private static Supplier<Double> doubleConfig(String config, double defaultValue)
	{
		return numberConfig(config, Number::doubleValue, defaultValue);
	}
	
	private static Supplier<Integer> intConfig(String config, int defaultValue)
	{
		return numberConfig(config, Number::intValue, defaultValue);
	}
	
	private static <T> Supplier<T> numberConfig(String config, Function<Number, T> mapper, T defaultValue)
	{
		return () -> Optional.ofNullable(DATA.get(config))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isNumber).map(JsonPrimitive::getAsNumber)
			.map(mapper).orElse(defaultValue);
	}
	
	private static Supplier<Boolean> booleanConfig(String config, boolean defaultValue)
	{
		return () -> Optional.ofNullable(DATA.get(config))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(defaultValue);
	}
}
