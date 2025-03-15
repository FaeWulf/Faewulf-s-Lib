package xyz.faewulf.lib.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.lib.event.ResourceReloader;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Shadow
    @Final
    private ReloadableResourceManager resourceManager;

    //reload event
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;registerReloadListener(Lnet/minecraft/server/packs/resources/PreparableReloadListener;)V", ordinal = 0))
    private void injectReloadListener(CallbackInfo ci) {
        resourceManager.registerReloadListener(new ResourceReloader());
    }
}
