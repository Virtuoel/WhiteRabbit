package virtuoel.white_rabbit.mixin.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import virtuoel.white_rabbit.entity.effect.ResizingStatusEffect;
import virtuoel.white_rabbit.init.StatusEffectRegistrar;
import virtuoel.white_rabbit.util.MixinConstants;

@Mixin(StatusEffect.class)
public class StatusEffectMixin
{
	@Inject(at = @At("HEAD"), method = MixinConstants.ON_APPLIED, remap = false)
	private void white_rabbit$onApplied(LivingEntity entity, @Coerce Object attributes, int amplifier, CallbackInfo info)
	{
		if ((Object) this == StatusEffectRegistrar.RESIZING)
		{
			ResizingStatusEffect.doApplication(entity);
		}
	}
	
	@Inject(at = @At("HEAD"), method = MixinConstants.ON_REMOVED, remap = false)
	private void white_rabbit$onRemoved(LivingEntity entity, @Coerce Object attributes, int amplifier, CallbackInfo info)
	{
		if ((Object) this == StatusEffectRegistrar.RESIZING)
		{
			ResizingStatusEffect.doRemoval(entity);
		}
	}
}
