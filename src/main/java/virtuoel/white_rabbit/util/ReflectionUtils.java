package virtuoel.white_rabbit.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroup.DisplayContext;
import net.minecraft.item.ItemGroup.Entries;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.pehkui.util.VersionUtils;
import virtuoel.white_rabbit.WhiteRabbit;

public class ReflectionUtils
{
	public static final MethodHandle FORMATTED, PLAY_SOUND, GROUP, BUILD, REGISTER, GET_BLOCK_STATE, GET_BLOCK_ENTITY, GET_BLOCK_POS;
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
			final boolean is1201Minus = VersionUtils.MINOR < 20 || (VersionUtils.MINOR == 20 && VersionUtils.PATCH <= 1);
			
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
			
			if (is1201Minus)
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2342", "method_10120", "()Lnet/minecraft/class_2680;");
				m = BlockPointer.class.getMethod(mapped);
				h.put(5, lookup.unreflect(m));
				
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2342", "method_10121", "()Lnet/minecraft/class_2586;");
				m = BlockPointer.class.getMethod(mapped);
				h.put(6, lookup.unreflect(m));
				
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2342", "method_10122", "()Lnet/minecraft/class_2338;");
				m = BlockPointer.class.getMethod(mapped);
				h.put(7, lookup.unreflect(m));
			}
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
		GET_BLOCK_STATE = h.get(5);
		GET_BLOCK_ENTITY = h.get(6);
		GET_BLOCK_POS = h.get(7);
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
	
	public static BlockState getBlockState(final BlockPointer pointer)
	{
		if (GET_BLOCK_STATE != null)
		{
			try
			{
				return (BlockState) GET_BLOCK_STATE.invoke(pointer);
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return pointer.state();
	}
	
	public static BlockEntity getBlockEntity(final BlockPointer pointer)
	{
		if (GET_BLOCK_ENTITY != null)
		{
			try
			{
				return (BlockEntity) GET_BLOCK_ENTITY.invoke(pointer);
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return pointer.blockEntity();
	}
	
	public static BlockPos getBlockPos(final BlockPointer pointer)
	{
		if (GET_BLOCK_POS != null)
		{
			try
			{
				return (BlockPos) GET_BLOCK_POS.invoke(pointer);
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return pointer.pos();
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
		
		final Supplier<ItemGroup.Builder> builder;
		
		if (VersionUtils.MINOR == 19 && VersionUtils.PATCH >= 3)
		{
			try
			{
				final Method m = FabricItemGroup.class.getMethod("builder", Identifier.class);
				builder = () -> {
					try
					{
						return (ItemGroup.Builder) m.invoke(null, id);
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
					{
						throw new RuntimeException(e);
					}
				};
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			builder = () -> FabricItemGroup.builder()
				.displayName(Text.translatable("itemGroup." + WhiteRabbit.MOD_ID + ".general"));
		}
		
		final ItemGroup group = Classloading1193Plus.addEntries(builder.get().icon(icon), items).build();
		
		if (VersionUtils.MINOR > 19)
		{
			return Registry.register(Registries.ITEM_GROUP, id, group);
		}
		
		return group;
	}
	
	public static final class Classloading1193Plus
	{
		public static ItemGroup.Builder addEntries(ItemGroup.Builder builder, Supplier<Stream<ItemConvertible>> items)
		{
			return builder.entries(new ItemGroup.EntryCollector()
			{
				@Override
				public void accept(DisplayContext displayContext, Entries entries)
				{
					items.get().forEach(entries::add);
				}
				
				@SuppressWarnings("unused")
				public void accept(FeatureSet enabledFeatures, Entries entries, boolean operatorEnabled)
				{
					items.get().forEach(entries::add);
				}
			});
		}
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
	
	public static Optional<Field> getField(final Optional<Class<?>> classObj, final String fieldName)
	{
		return classObj.map(c ->
		{
			try
			{
				final Field f = c.getDeclaredField(fieldName);
				f.setAccessible(true);
				return f;
			}
			catch (SecurityException | NoSuchFieldException e)
			{
				
			}
			return null;
		});
	}
	
	public static void setField(final Optional<Class<?>> classObj, final String fieldName, Object object, Object value)
	{
		ReflectionUtils.getField(classObj, fieldName).ifPresent(f ->
		{
			try
			{
				f.set(object, value);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				
			}
		});
	}
	
	public static Optional<Method> getMethod(final Optional<Class<?>> classObj, final String methodName, Class<?>... args)
	{
		return classObj.map(c ->
		{
			try
			{
				final Method m = c.getMethod(methodName, args);
				m.setAccessible(true);
				return m;
			}
			catch (SecurityException | NoSuchMethodException e)
			{
				
			}
			return null;
		});
	}
	
	public static <T> Optional<Constructor<T>> getConstructor(final Optional<Class<T>> clazz, final Class<?>... params)
	{
		return clazz.map(c ->
		{
			try
			{
				return c.getConstructor(params);
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				return null;
			}
		});
	}
	
	public static Optional<Class<?>> getClass(final String className, final String... classNames)
	{
		Optional<Class<?>> ret = getClass(className);
		
		for (final String name : classNames)
		{
			if (ret.isPresent())
			{
				return ret;
			}
			
			ret = getClass(name);
		}
		
		return ret;
	}
	
	public static Optional<Class<?>> getClass(final String className)
	{
		try
		{
			return Optional.of(Class.forName(className));
		}
		catch (ClassNotFoundException e)
		{
			
		}
		
		return Optional.empty();
	}
	
	private ReflectionUtils()
	{
		
	}
}
