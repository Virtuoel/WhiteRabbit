package virtuoel.white_rabbit.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import virtuoel.pehkui.util.VersionUtils;
import virtuoel.white_rabbit.WhiteRabbit;

public class ReflectionUtils
{
	public static final MethodHandle FORMATTED, PLAY_SOUND;
	
	static
	{
		final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
		final Int2ObjectMap<MethodHandle> h = new Int2ObjectArrayMap<MethodHandle>();
		
		final Lookup lookup = MethodHandles.lookup();
		String mapped = "unset";
		Method m;
		
		try
		{
			final boolean is115Minus = VersionUtils.MINOR <= 15;
			final boolean is118Minus = VersionUtils.MINOR <= 18;
			
			if (is115Minus)
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2561", "method_10856", "([Lnet/minecraft/class_124;)Lnet/minecraft/class_2561;");
				m = Text.class.getMethod(mapped, Formatting[].class);
			}
			else
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_5250", "method_27695", "([Lnet/minecraft/class_124;)Lnet/minecraft/class_5250;");
				m = MutableText.class.getMethod(mapped, Formatting[].class);
			}
			h.put(0, lookup.unreflect(m));
			
			mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_1937", "method_8465", "(Lnet/minecraft/class_1657;DDDLnet/minecraft/class_3414;Lnet/minecraft/class_3419;FF" + (is118Minus ? ")V" : "J)V"));
			if (is118Minus)
			{
				m = World.class.getMethod(mapped, PlayerEntity.class, double.class, double.class, double.class, SoundEvent.class, SoundCategory.class, float.class, float.class);
			}
			else
			{
				m = World.class.getMethod(mapped, PlayerEntity.class, double.class, double.class, double.class, SoundEvent.class, SoundCategory.class, float.class, float.class, long.class);
			}
			h.put(1, lookup.unreflect(m));
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException e)
		{
			WhiteRabbit.LOGGER.error("Last method lookup: {}", mapped);
			WhiteRabbit.LOGGER.catching(e);
		}
		
		FORMATTED = h.get(0);
		PLAY_SOUND = h.get(1);
	}
	
	public static Text formatted(Object input, Formatting... formatting)
	{
		if (FORMATTED != null)
		{
			try
			{
				if (VersionUtils.MINOR <= 15)
				{
					return (Text) FORMATTED.invokeExact((Text) input, formatting);
				}
				else
				{
					return (MutableText) FORMATTED.invokeExact((MutableText) input, formatting);
				}
			}
			catch (Throwable e)
			{
				
			}
		}
		
		return (Text) input;
	}
	
	public static void playSound(World world, @Nullable PlayerEntity except, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch)
	{
		if (PLAY_SOUND != null)
		{
			try
			{
				if (VersionUtils.MINOR <= 18)
				{
					PLAY_SOUND.invokeExact(world, except, x, y, z, sound, category, volume, pitch);
				}
				else
				{
					PLAY_SOUND.invokeExact(world, except, x, y, z, sound, category, volume, pitch, (long) world.random.nextLong());
				}
			}
			catch (Throwable e)
			{
				
			}
		}
	}
	
	public static void init()
	{
		
	}
}
