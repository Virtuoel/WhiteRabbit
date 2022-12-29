package virtuoel.white_rabbit.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import virtuoel.pehkui.util.VersionUtils;
import virtuoel.white_rabbit.WhiteRabbit;

public class ReflectionUtils
{
	public static final MethodHandle FORMATTED, PLAY_SOUND, GROUP, BUILD, REGISTER;
	public static final Registry<Item> ITEM_REGISTRY;
	public static final Registry<StatusEffect> STATUS_EFFECT_REGISTRY;
	
	static
	{
		final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
		final Int2ObjectMap<MethodHandle> h = new Int2ObjectArrayMap<MethodHandle>();
		Object rI, rS = rI = null;
		
		final Lookup lookup = MethodHandles.lookup();
		String mapped = "unset";
		Method m;
		Class<?> clazz;
		Field f;
		
		try
		{
			final boolean is115Minus = VersionUtils.MINOR <= 15;
			final boolean is118Minus = VersionUtils.MINOR <= 18;
			final boolean is1192Minus = VersionUtils.MINOR < 19 || (VersionUtils.MINOR == 19 && VersionUtils.PATCH <= 2);
			
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
			
			mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_1937", is1192Minus ? "method_8465" : "method_47967", "(Lnet/minecraft/class_1657;DDDLnet/minecraft/class_3414;Lnet/minecraft/class_3419;FF" + (is118Minus ? ")V" : "J)V"));
			if (is118Minus)
			{
				m = World.class.getMethod(mapped, PlayerEntity.class, double.class, double.class, double.class, SoundEvent.class, SoundCategory.class, float.class, float.class);
			}
			else
			{
				m = World.class.getMethod(mapped, PlayerEntity.class, double.class, double.class, double.class, SoundEvent.class, SoundCategory.class, float.class, float.class, long.class);
			}
			h.put(1, lookup.unreflect(m));
			
			if (is1192Minus)
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_1792$class_1793", "method_7892", "(Lnet/minecraft/class_1761;)Lnet/minecraft/class_1792$class_1793;");
				m = Item.Settings.class.getMethod(mapped, ItemGroup.class);
				h.put(2, lookup.unreflect(m));
			}
			
			if (FabricLoader.getInstance().isModLoaded("fabric-item-groups-v0"))
			{
				mapped = "net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder";
				clazz = Class.forName(mapped);
				
				mapped = "build";
				m = clazz.getMethod(mapped, Identifier.class, Supplier.class);
				h.put(3, lookup.unreflect(m));
			}
			
			mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2378", "method_10230", "(Lnet/minecraft/class_2378;Lnet/minecraft/class_2960;Ljava/lang/Object;)Ljava/lang/Object;");
			m = Registry.class.getMethod(mapped, Registry.class, Identifier.class, Object.class);
			h.put(4, lookup.unreflect(m));
			
			final String registrar = "net.minecraft.class_" + (is1192Minus ? "2378" : "7923");
			
			mapped = mappingResolver.mapClassName("intermediary", registrar);
			clazz = Class.forName(mapped);
			
			mapped = mappingResolver.mapFieldName("intermediary", registrar, "field_" + (is1192Minus ? "11142" : "41178"), "Lnet/minecraft/class_" + (is1192Minus ? "2348;" : "7922;"));
			f = clazz.getField(mapped);
			rI = f.get(null);
			
			mapped = mappingResolver.mapFieldName("intermediary", registrar, "field_" + (is1192Minus ? "11159" : "41174"), "Lnet/minecraft/class_2378;");
			f = clazz.getField(mapped);
			rS = f.get(null);
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e)
		{
			WhiteRabbit.LOGGER.error("Current name lookup: {}", mapped);
			WhiteRabbit.LOGGER.catching(e);
		}
		
		FORMATTED = h.get(0);
		PLAY_SOUND = h.get(1);
		GROUP = h.get(2);
		BUILD = h.get(3);
		REGISTER = h.get(4);
		ITEM_REGISTRY = castRegistry(rI);
		STATUS_EFFECT_REGISTRY = castRegistry(rS);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> Registry<T> castRegistry(Object obj)
	{
		return (Registry<T>) obj;
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
				throw new RuntimeException(e);
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
				throw new RuntimeException(e);
			}
		}
	}
	
	public static void setItemSettingsGroup(Item.Settings settings, ItemGroup group)
	{
		if (GROUP != null)
		{
			try
			{
				GROUP.invoke(settings, group);
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
	public static ItemGroup buildItemGroup(Identifier id, Supplier<ItemStack> icon, Supplier<Stream<ItemConvertible>> items)
	{
		if (BUILD != null)
		{
			try
			{
				return (ItemGroup) BUILD.invoke(id, icon);
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
		
		if (FabricLoader.getInstance().isModLoaded("fabric-item-group-api-v1"))
		{
			return FabricItemGroup.builder(id)
				.icon(icon)
				.entries((enabledFeatures, entries, operatorEnabled) ->
				{
					items.get().forEach(entries::add);
				})
				.build();
		}
		
		return null;
	}
	
	public static <V, T extends V> T register(Registry<V> registry, Identifier id, T entry)
	{
		try
		{
			return (T) REGISTER.invoke(registry, id, entry);
		}
		catch (Throwable e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private ReflectionUtils()
	{
		
	}
}
