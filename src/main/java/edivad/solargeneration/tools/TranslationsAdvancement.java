package edivad.solargeneration.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TranslationsAdvancement {
    private final String title, desc;
    public TranslationsAdvancement(String title) {
        this.title = title;
        desc = title + ".desc";
    }

    public String title() {
        return title;
    }

    public String desc() {
        return desc;
    }

    public MutableComponent translateTitle() {
        return Component.translatable(title);
    }

    public MutableComponent translateDescription() {
        return Component.translatable(desc);
    }
}
