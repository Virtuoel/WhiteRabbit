package virtuoel.white_rabbit.api;

import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import com.google.gson.JsonObject;

import virtuoel.pehkui.util.JsonConfigBuilder;
import virtuoel.white_rabbit.WhiteRabbit;

public class WhiteRabbitConfig
{
	@ApiStatus.Internal
	public static final JsonConfigBuilder BUILDER = new JsonConfigBuilder(
		WhiteRabbit.MOD_ID,
		"config.json"
	);
	
	public static final Client CLIENT = new Client(BUILDER);
	public static final Common COMMON = new Common(BUILDER);
	public static final Server SERVER = new Server(BUILDER);
	
	public static final class Client
	{
		private Client(final JsonConfigBuilder builder)
		{
			
		}
	}
	
	public static final class Common
	{
		public final Supplier<Boolean> resizeBoundsOnly;
		public final Supplier<Double> minScale;
		public final Supplier<Double> maxScale;
		public final Supplier<Double> shrinkMultiplier;
		public final Supplier<Double> growthMultiplier;
		public final Supplier<Integer> shrinkDelayTicks;
		public final Supplier<Integer> growthDelayTicks;
		
		private Common(final JsonConfigBuilder builder)
		{
			this.resizeBoundsOnly = builder.booleanConfig("resizeBoundsOnly", false);
			this.minScale = builder.doubleConfig("minScale", 0.015625D);
			this.maxScale = builder.doubleConfig("maxScale", 16.0D);
			this.shrinkMultiplier = builder.doubleConfig("shrinkMultiplier", 0.5D);
			this.growthMultiplier = builder.doubleConfig("growthMultiplier", 2.0D);
			this.shrinkDelayTicks = builder.numberConfig("shrinkDelayTicks", Number::intValue, 100);
			this.growthDelayTicks = builder.numberConfig("growthDelayTicks", Number::intValue, 100);
		}
	}
	
	public static final class Server
	{
		private Server(final JsonConfigBuilder builder)
		{
			
		}
	}
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public static final Supplier<JsonObject> HANDLER = BUILDER.config;
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public static final JsonObject DATA = BUILDER.config.get();
	
	private WhiteRabbitConfig()
	{
		
	}
}
