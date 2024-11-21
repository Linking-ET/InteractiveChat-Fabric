package website.interactivechat.amwp;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class InteractiveChatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        throw new RuntimeException(Text.literal("")
                .append(Text.literal("\n\n⚠ WARNING ⚠\n\n")
                        .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)))
                .append(Text.literal("Interactive Chat is a SERVER-SIDE ONLY mod!\n")
                        .setStyle(Style.EMPTY.withColor(Formatting.RED)))
                .append(Text.literal("Please remove it from your client mods folder.\n")
                        .setStyle(Style.EMPTY.withColor(Formatting.GOLD)))
                .getString());
    }
}
