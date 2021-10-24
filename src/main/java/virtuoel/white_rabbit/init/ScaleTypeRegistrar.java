package virtuoel.white_rabbit.init;

import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.TypedScaleModifier;
import virtuoel.white_rabbit.WhiteRabbit;
import virtuoel.white_rabbit.api.WhiteRabbitConfig;

public class ScaleTypeRegistrar
{
	public static final ScaleModifier FOOD_MODIFIER = ScaleRegistries.register(
		ScaleRegistries.SCALE_MODIFIERS,
		WhiteRabbit.id("food_multiplier"),
		new TypedScaleModifier(() -> ScaleTypeRegistrar.FOOD_TYPE)
	);
	
	public static final ScaleType FOOD_TYPE = registerFoodType();
	
	private static ScaleType registerFoodType()
	{
		final ScaleType type = ScaleRegistries.register(
			ScaleRegistries.SCALE_TYPES,
			WhiteRabbit.id("food"),
			ScaleType.Builder.create()
				.affectsDimensions()
				.addDependentModifier(FOOD_MODIFIER)
				.build()
		);
		
		if (WhiteRabbitConfig.COMMON.resizeBoundsOnly.get())
		{
			ScaleTypes.WIDTH.getDefaultBaseValueModifiers().add(FOOD_MODIFIER);
			ScaleTypes.HEIGHT.getDefaultBaseValueModifiers().add(FOOD_MODIFIER);
		}
		else
		{
			ScaleTypes.BASE.getDefaultBaseValueModifiers().add(FOOD_MODIFIER);
		}
		
		return type;
	}
	
	public static final ScaleTypeRegistrar INSTANCE = new ScaleTypeRegistrar();
	
	private ScaleTypeRegistrar()
	{
		
	}
}
