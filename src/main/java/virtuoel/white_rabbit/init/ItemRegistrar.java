package virtuoel.white_rabbit.init;

import java.util.stream.Stream;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import virtuoel.white_rabbit.WhiteRabbit;
import virtuoel.white_rabbit.item.ResizingDrinkItem;
import virtuoel.white_rabbit.item.ResizingItem;
import virtuoel.white_rabbit.util.ReflectionUtils;

public final class ItemRegistrar
{
	public final static class WhiteRabbitFoodComponents
	{
		public static final FoodComponent PISHSALVER = new FoodComponent.Builder()
			.hunger(8)
			.saturationModifier(0.416F)
			.alwaysEdible()
			.build();
		
		public static final FoodComponent UPELKUCHEN = new FoodComponent.Builder()
			.hunger(7)
			.saturationModifier(117.0F / 670.0F)
			.alwaysEdible()
			.build();
	}
	
	public static final ItemGroup ITEM_GROUP = ReflectionUtils.buildItemGroup(
		WhiteRabbit.id("general"),
		() -> new ItemStack(ItemRegistrar.UPELKUCHEN),
		() -> Stream.of(
				ItemRegistrar.PISHSALVER,
				ItemRegistrar.UPELKUCHEN
			)
	);
	
	public static Item.Settings commonItemSettings()
	{
		final Item.Settings settings = new Item.Settings();
		
		ReflectionUtils.setItemSettingsGroup(settings, ITEM_GROUP);
		
		return settings;
	}
	
	public static final Item PISHSALVER = register(
		"pishsalver",
		new ResizingDrinkItem(
			commonItemSettings().maxCount(16)
			.food(WhiteRabbitFoodComponents.PISHSALVER),
			WhiteRabbit::getShrinkTargetScale,
			WhiteRabbit::getShrinkDelayTicks,
			WhiteRabbit::canShrink,
			() -> new ItemStack(Items.GLASS_BOTTLE)
		)
	);
	
	public static final Item UPELKUCHEN = register(
		"upelkuchen",
		new ResizingItem(
			commonItemSettings().maxCount(16)
			.food(WhiteRabbitFoodComponents.UPELKUCHEN),
			WhiteRabbit::getGrowthTargetScale,
			WhiteRabbit::getGrowthDelayTicks,
			WhiteRabbit::canGrow
		)
	);
	
	public static Item register(String name, Item entry)
	{
		return ReflectionUtils.register(ReflectionUtils.ITEM_REGISTRY, WhiteRabbit.id(name), entry);
	}
	
	public static final ItemRegistrar INSTANCE = new ItemRegistrar();
	
	private ItemRegistrar()
	{
		
	}
}
