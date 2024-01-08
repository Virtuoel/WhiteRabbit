package virtuoel.white_rabbit.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
		final World world = entity.getEntityWorld();
		if (world.isClient)
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
	
	public static void doApplication(final Entity entity)
	{
		playApplicationSound(entity);
	}
	
	public static void playApplicationSound(final Entity entity)
	{
		final Vec3d pos = entity.getPos();
		
		if (!entity.isSilent())
		{
			ReflectionUtils.playSound(entity.getEntityWorld(), null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_EVOKER_CAST_SPELL, entity.getSoundCategory(), 1.0F, 1.0F);
		}
	}
	
	public static void doRemoval(final Entity entity)
	{
		playRemovalSound(entity);
		
		final ScaleType type = ScaleTypeRegistrar.FOOD_TYPE;
		
		final float defaultScale = type.getDefaultBaseScale();
		
		type.getScaleData(entity).setTargetScale(defaultScale);
	}
	
	public static void playRemovalSound(final Entity entity)
	{
		final Vec3d pos = entity.getPos();
		
		if (!entity.isSilent())
		{
			ReflectionUtils.playSound(entity.getEntityWorld(), null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, entity.getSoundCategory(), 1.0F, 1.0F);
		}
	}
}
