package com.xg7plugins.xg7menus.api.menus.builders.item;

import com.xg7plugins.xg7menus.api.menus.MenuException;
import com.xg7plugins.xg7menus.api.menus.builders.BaseItemBuilder;
import com.xg7plugins.xg7menus.api.utils.NMSUtil;
import com.xg7plugins.xg7menus.api.utils.Text;
import com.xg7plugins.xg7menus.api.utils.XSeries.XMaterial;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class BookItemBuilder extends BaseItemBuilder<BookItemBuilder> {
    public BookItemBuilder() {
        super(XMaterial.WRITABLE_BOOK.parseItem());
    }
    public BookItemBuilder(ItemStack book) {
        super(book);
    }

    @Contract("_ -> new")
    public static @NotNull BookItemBuilder from(@NotNull ItemStack book) {
        if (!book.getType().equals(XMaterial.WRITABLE_BOOK.parseMaterial())) throw new MenuException("This item isn't a writable book!");
        return new BookItemBuilder(book);
    }
    @Contract(" -> new")
    public static @NotNull BookItemBuilder builder() {
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

    @SneakyThrows
    public void openBook(Player player) {
        int slot = player.getInventory().getHeldItemSlot();
        ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, this.itemStack);

        Class<?> craftPlayerClass = NMSUtil.getCraftBukkitClass("entity.CraftPlayer");
        Class<?> entityPlayerClass = NMSUtil.getNMSClass("EntityPlayer");

        Class<?> nmsItemStackClass = NMSUtil.getNMSClass("ItemStack");

        Method getHandle = craftPlayerClass.getMethod("getHandle");

        Object entityPlayer = getHandle.invoke(player);

        Method asNMSCopy = NMSUtil.getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
        Object nmsItemStack = asNMSCopy.invoke(null, this.itemStack);

        Class<?> packetPlayOutOpenBookClass = NMSUtil.getNMSClass("PacketPlayOutOpenBook");

        Class<?> enumHandClass = NMSUtil.getNMSClass("EnumHand");

        Object mainHand = enumHandClass.getField("MAIN_HAND").get(null);

        Constructor<?> packetConstructor = packetPlayOutOpenBookClass.getConstructor(enumHandClass);
        Object packet = packetConstructor.newInstance(mainHand);

        Method sendPacket = entityPlayerClass.getField("playerConnection").getType().getMethod("sendPacket", NMSUtil.getNMSClass("Packet"));

        sendPacket.invoke(entityPlayerClass.getField("playerConnection").get(entityPlayer), packet);

        player.getInventory().setItem(slot,old);
    }
}
