package virtuoel.white_rabbit.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.white_rabbit.init.ScaleTypeRegistrar;

public class ResizingStatusEffect extends StatusEffect
{
	public ResizingStatusEffect(StatusEffectCategory statusEffectCategory, int color)
	{
		super(statusEffectCategory, color);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier)
	{
		return false;
	}
	
	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier)
	{
		super.onRemoved(entity, attributes, amplifier);
		
		final ScaleType type = ScaleTypeRegistrar.FOOD_TYPE;
		
		final float defaultScale = type.getDefaultBaseScale();
		
		type.getScaleData(entity).setTargetScale(defaultScale);
	}
}
