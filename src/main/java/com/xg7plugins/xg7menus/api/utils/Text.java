package com.xg7plugins.xg7menus.api.utils;

import lombok.Getter;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Text {

    private static final Pattern GRADIENT_PATTERN = Pattern.compile("\\[g#([0-9a-fA-F]{6})](.*?)\\[/g#([0-9a-fA-F]{6})]");
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private String text;
    private ComponentBuilder componentBuilder;

    public Text(String text) {

        this.componentBuilder = new ComponentBuilder();

        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) >= 16) {
            this.text = applyGradients(text);
            Matcher matcher = HEX_PATTERN.matcher(text);
            while (matcher.find()) {
                String color = text.substring(matcher.start(), matcher.end());
                this.text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color.substring(1)) + "");
                matcher = HEX_PATTERN.matcher(text);
            }
        }

        this.text = ChatColor.translateAlternateColorCodes('&', text);

    }
    public Text(String text, PixelsSize centerSize) {

        this.componentBuilder = new ComponentBuilder();

        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) >= 16) {
            this.text = applyGradients(text);
            Matcher matcher = HEX_PATTERN.matcher(text);
            while (matcher.find()) {
                String color = text.substring(matcher.start(), matcher.end());
                this.text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color.substring(1)) + "");
                matcher = HEX_PATTERN.matcher(text);
            }
        }

        this.text = ChatColor.translateAlternateColorCodes('&', text);

        if (text.startsWith("[CENTER] ")) this.text = getCentralizedText(centerSize.getPixels(), text);

    }

    @Contract("_ -> new")
    public static @NotNull Text format(String text) {
        return new Text(text);
    }
    @Contract("_, _ -> new")
    public static @NotNull Text formatAndCenter(String text, PixelsSize size) {
        return new Text(text, size);
    }

    public void send(CommandSender sender) {

        if (sender instanceof Player) {
            text = text.replace("[PLAYER]", sender.getName());
            if (text.startsWith("[ACTION] ")) {
                text = text.substring(9);
                sendActionBar(((Player) sender));
                return;
            }

            sender.sendMessage(text);
            return;
        }
        sender.sendMessage(text);
    }
    public Text setPlaceholders(Player player) {
        this.text = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ? PlaceholderAPI.setPlaceholders(player, text) : text;
        return this;
    }
    @SneakyThrows
    private void sendActionBar(Player player) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) >= 9) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
            return;
        }

        Class<?> craftPlayerClass = NMSUtil.getCraftBukkitClass("entity.CraftPlayer");
        Object craftPlayer = craftPlayerClass.cast(player);

        Class<?> packetPlayOutChatClass = NMSUtil.getNMSClass("PacketPlayOutChat");
        Class<?> iChatBaseComponentClass = NMSUtil.getNMSClass("IChatBaseComponent");
        Class<?> chatComponentTextClass = NMSUtil.getNMSClass("ChatComponentText");

        Object chatComponent = chatComponentTextClass.getConstructor(String.class).newInstance(text);
        Object packet = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, byte.class)
                .newInstance(chatComponent, (byte) 2);

        Object craftPlayerHandle = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
        Object playerConnection = craftPlayerHandle.getClass().getField("playerConnection").get(craftPlayerHandle);
        playerConnection.getClass().getMethod("sendPacket", NMSUtil.getNMSClass("Packet")).invoke(playerConnection, packet);

    }

    public static String getCentralizedText(int pixels, @NotNull String text) {

        int textWidht = 0;
        boolean cCode = false;
        boolean isBold = false;
        boolean isrgb = false;
        int rgbCount = 0;
        int cCodeCount = 0;
        int rgbToAdd = 0;
        for (char c : text.toCharArray()) {
            if (isrgb) {
                if (rgbCount == 6) {
                    isrgb = false;
                    continue;
                }
                if ("0123456789aAbBcCdDeEfF".contains(String.valueOf(c))) {
                    rgbToAdd = getCharSize(c, isBold);
                    rgbCount++;
                    continue;
                }
                rgbCount = 0;
                textWidht += rgbToAdd;
                continue;
            }
            if (c == '&') {
                cCode = true;
                cCodeCount++;
                continue;
            }
            if (cCode && net.md_5.bungee.api.ChatColor.ALL_CODES.contains(c + "")) {
                cCode = false;
                cCodeCount = 0;
                isBold = c == 'l' || c == 'L';
                continue;
            }
            if (cCode) {
                if (c == '#') {
                    cCode = false;
                    isrgb = true;
                    continue;
                }
                while (cCodeCount != 0) {
                    cCodeCount--;
                    textWidht += getCharSize('&', isBold);
                }
            }
            textWidht += getCharSize(c, isBold);
        }

        textWidht /= 2;

        if (textWidht > pixels) return text;

        StringBuilder builder = new StringBuilder();

        int compensated = 0;

        while (compensated < pixels - textWidht) {
            builder.append(ChatColor.COLOR_CHAR + "r ");
            compensated += 4;
        }

        return builder + text;

    }
    private static double[] linear(double from, double to, int max) {
        double[] res = new double[max];
        for (int i = 0; i < max; i++) res[i] = from + i * ((to - from) / (max - 1));
        return res;
    }

    public static String applyGradients(String text) {

        Matcher matcher = GRADIENT_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            Color from = Color.decode("#" + matcher.group(1));
            Color to = Color.decode("#" + matcher.group(3));
            String textHex = matcher.group(2);

            double[] red = linear(from.getRed(), to.getRed(), textHex.length());
            double[] green = linear(from.getGreen(), to.getGreen(), textHex.length());
            double[] blue = linear(from.getBlue(), to.getBlue(), textHex.length());

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < textHex.length(); i++) {
                builder.append(net.md_5.bungee.api.ChatColor.of(new Color(
                                (int) Math.round(red[i]),
                                (int) Math.round(green[i]),
                                (int) Math.round(blue[i]))))
                        .append(textHex.charAt(i));
            }
            matcher.appendReplacement(result, builder.toString() + net.md_5.bungee.api.ChatColor.RESET);
        }
        matcher.appendTail(result);

        return result.toString();
    }
    private static int getCharSize(char c, boolean isBold) {
        String[] chars = new String[]{"~@", "1234567890ABCDEFGHJKLMNOPQRSTUVWXYZabcedjhmnopqrsuvxwyz/\\+=-_^?&%$#", "{}fk*\"<>()", "It[] ", "'l`", "!|:;,.i", "¨´"};
        for (int i = 0; i < chars.length; i++) {
            if (chars[i].contains(String.valueOf(c))) {
                return isBold && c != ' ' ? 8 - i : 7 - i;
            }
        }

        return 4;
    }

    public static long convertToMilliseconds(String timeStr) {
        long milliseconds = 0;
        Pattern pattern = Pattern.compile("(\\d+)([SMHD])");
        Matcher matcher = pattern.matcher(timeStr.toUpperCase());

        while (matcher.find()) {
            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "S":
                    milliseconds += value * 1000;
                    break;
                case "M":
                    milliseconds += value * 60000;
                    break;
                case "H":
                    milliseconds += value * 3600000;
                    break;
                case "D":
                    milliseconds += value * 86400000;
                    break;
                default:
                    Log.severe("Invalid time unit: " + unit);
            }
        }

        return milliseconds;
    }

    @Getter
    public enum PixelsSize {

        CHAT(157),
        MOTD(127),
        INV(75);

        private final int pixels;

        PixelsSize (int pixels) {
            this.pixels = pixels;
        }


    }

}
