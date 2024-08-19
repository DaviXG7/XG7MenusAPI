package com.xg7plugins.xg7menus.api.menus.builders.item;

import com.xg7plugins.xg7menus.api.menus.builders.BaseItemBuilder;
import com.xg7plugins.xg7menus.api.utils.Text;
import com.xg7plugins.xg7menus.api.utils.XSeries.XMaterial;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.inventory.meta.BookMeta;

public class BookItemBuilder extends BaseItemBuilder<BookItemBuilder> {
    public BookItemBuilder() {
        super(XMaterial.WRITABLE_BOOK.parseItem());
    }
    public static BookItemBuilder builder() {
        return new BookItemBuilder();
    }
    public BookItemBuilder title(String title) {
        BookMeta meta = (BookMeta) this.itemStack.getItemMeta();
        meta.setTitle(Text.format(title).getText());
        super.meta(meta);
        return this;
    }
    public BookItemBuilder author(String author) {
        BookMeta meta = (BookMeta) this.itemStack.getItemMeta();
        meta.setAuthor(author);
        super.meta(meta);
        return this;
    }
    public BookItemBuilder addPage(String text) {
        BookMeta meta = (BookMeta) this.itemStack.getItemMeta();
        meta.addPage(Text.format(text).getText());
        super.meta(meta);
        return this;
    }
    public BookItemBuilder addPage(BaseComponent[] components) {
        BookMeta meta = (BookMeta) this.itemStack.getItemMeta();
        meta.spigot().addPage(components);
        super.meta(meta);
        return this;
    }
}
