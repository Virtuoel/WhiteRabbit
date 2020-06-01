package virtuoel.white_rabbit.init;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import virtuoel.white_rabbit.WhiteRabbit;
import virtuoel.white_rabbit.item.ResizingDrinkItem;
import virtuoel.white_rabbit.item.ResizingItem;

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
	
	public static final ItemGroup ITEM_GROUP = !FabricLoader.getInstance().isModLoaded("fabric-item-groups-v0") ? null : FabricItemGroupBuilder.build(WhiteRabbit.id("item_group"), () -> new ItemStack(ItemRegistrar.PISHSALVER));
	
	public static final Item PISHSALVER = register(
		"pishsalver",
		new ResizingDrinkItem(
			new Item.Settings().group(ITEM_GROUP).maxCount(16)
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
			new Item.Settings().group(ITEM_GROUP).maxCount(16)
			.food(WhiteRabbitFoodComponents.UPELKUCHEN),
			WhiteRabbit::getGrowthTargetScale,
			WhiteRabbit::getGrowthDelayTicks,
			WhiteRabbit::canGrow
		)
	);
	
	public static Item register(String name, Item item)
	{
		return Registry.register(Registry.ITEM, WhiteRabbit.id(name), item);
	}
	
	public static final ItemRegistrar INSTANCE = new ItemRegistrar();
	
	private ItemRegistrar()
	{
		
	}
}
