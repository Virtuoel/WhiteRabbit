package virtuoel.white_rabbit.mixin.compat1202plus;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import virtuoel.white_rabbit.entity.effect.ResizingStatusEffect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
	@Inject(method = "onStatusEffectApplied", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onApplied(Lnet/minecraft/entity/attribute/AttributeContainer;I)V"))
	private void white_rabbit$onStatusEffectApplied(StatusEffectInstance effect, @Nullable Entity source, CallbackInfo ci)
	{
		ResizingStatusEffect.doApplication((Entity) (Object) this);
	}
	
	@Inject(method = "onStatusEffectUpgraded", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/attribute/AttributeContainer;)V"))
	private void white_rabbit$onStatusEffectUpgraded$onRemoved(StatusEffectInstance effect, boolean reapplyEffect, @Nullable Entity source, CallbackInfo ci)
	{
		ResizingStatusEffect.doRemoval((Entity) (Object) this);
	}
	
	@Inject(method = "onStatusEffectUpgraded", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onApplied(Lnet/minecraft/entity/attribute/AttributeContainer;I)V"))
	private void white_rabbit$onStatusEffectUpgraded$onApplied(StatusEffectInstance effect, boolean reapplyEffect, @Nullable Entity source, CallbackInfo ci)
	{
		ResizingStatusEffect.doApplication((Entity) (Object) this);
	}
	
	@Inject(method = "onStatusEffectRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/attribute/AttributeContainer;)V"))
	private void white_rabbit$onStatusEffectApplied(StatusEffectInstance effect, CallbackInfo ci)
	{
		ResizingStatusEffect.doRemoval((Entity) (Object) this);
	}
}
