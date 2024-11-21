package website.interactivechat.amwp.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ChatMixin {
    @Inject(method = "handleDecoratedMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(SignedMessage message, CallbackInfo ci) {
        String msg = message.getContent().getString().toLowerCase();
        if (msg.contains("[item]") || msg.contains("[inv]") || msg.contains("[ender]")) {
            ci.cancel();
        }
    }
} 