package virtuoel.white_rabbit.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.white_rabbit.init.ScaleTypeRegistrar;
import virtuoel.white_rabbit.util.ReflectionUtils;

public class ResizingStatusEffect extends StatusEffect
{
	public ResizingStatusEffect(StatusEffectCategory statusEffectCategory, int color)
	{
		super(statusEffectCategory, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier)
	{
		if (entity.world.isClient)
		{
			return;
		}
		
		final ScaleType type = ScaleTypeRegistrar.FOOD_TYPE;
		final ScaleData data = type.getScaleData(entity);
		final float scale = data.getBaseScale();
		
		if (scale == type.getDefaultBaseScale() && scale == data.getTargetScale())
		{
			entity.removeStatusEffect(this);
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier)
	{
		return duration % 20 == 0;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier)
	{
		super.onApplied(entity, attributes, amplifier);
		
		final Vec3d pos = entity.getPos();
		
		if (!entity.isSilent())
		{
			ReflectionUtils.playSound(entity.world, null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_EVOKER_CAST_SPELL, entity.getSoundCategory(), 1.0F, 1.0F);
		}
	}
	
	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier)
	{
		super.onRemoved(entity, attributes, amplifier);
		
		final Vec3d pos = entity.getPos();
		
		if (!entity.isSilent())
		{
			ReflectionUtils.playSound(entity.world, null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, entity.getSoundCategory(), 1.0F, 1.0F);
		}
		
		final ScaleType type = ScaleTypeRegistrar.FOOD_TYPE;
		
		final float defaultScale = type.getDefaultBaseScale();
		
		type.getScaleData(entity).setTargetScale(defaultScale);
	}
}
