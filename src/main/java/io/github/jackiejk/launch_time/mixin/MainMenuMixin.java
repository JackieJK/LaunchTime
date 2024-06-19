package io.github.jackiejk.launch_time.mixin;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.management.ManagementFactory;

@OnlyIn(Dist.CLIENT)
@Mixin(TitleScreen.class)
public class MainMenuMixin {

  @Unique
  private static boolean launchTime$wasShow;

  @Inject(method = "init", at = @At("RETURN"))
  void inject(CallbackInfo ci) {
    if (launchTime$wasShow) {
      return;
    }
    launchTime$wasShow = true;
    long timeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
    Component title = Component.translatable("launch_time.title");
    Component message = Component.translatable("launch_time.message", timeMillis / 1000.0);

    Minecraft mc = Minecraft.getInstance();
    mc.execute(() -> {
      mc.getToasts().addToast(new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE, title, message));
    });
  }

}
