package virtuoel.white_rabbit.init;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import virtuoel.white_rabbit.WhiteRabbit;
import virtuoel.white_rabbit.entity.effect.ResizingStatusEffect;
import virtuoel.white_rabbit.util.ReflectionUtils;

public class StatusEffectRegistrar
{
	public static final StatusEffect RESIZING = register("resizing", new ResizingStatusEffect(StatusEffectCategory.NEUTRAL, 0x00F5F5DC));
	
	private static StatusEffect register(String name, StatusEffect entry)
	{
		return ReflectionUtils.register(ReflectionUtils.STATUS_EFFECT_REGISTRY, WhiteRabbit.id(name), entry);
	}
	
	public static final StatusEffectRegistrar INSTANCE = new StatusEffectRegistrar();
	
	private StatusEffectRegistrar()
	{
		
	}
}
