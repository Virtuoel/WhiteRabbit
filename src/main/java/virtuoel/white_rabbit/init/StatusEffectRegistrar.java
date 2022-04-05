package virtuoel.white_rabbit.init;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.white_rabbit.WhiteRabbit;
import virtuoel.white_rabbit.entity.effect.ResizingStatusEffect;

public class StatusEffectRegistrar
{
	public static final StatusEffect RESIZING = register(WhiteRabbit.id("resizing"), new ResizingStatusEffect(StatusEffectCategory.NEUTRAL, 0x00F5F5DC));
	
	private static StatusEffect register(Identifier id, StatusEffect entry)
	{
		return Registry.register(Registry.STATUS_EFFECT, id, entry);
	}
	
	public static final StatusEffectRegistrar INSTANCE = new StatusEffectRegistrar();
	
	private StatusEffectRegistrar()
	{
		
	}
}