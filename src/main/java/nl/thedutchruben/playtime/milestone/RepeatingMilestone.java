package nl.thedutchruben.playtime.milestone;

import com.google.gson.annotations.SerializedName;
import nl.thedutchruben.mccore.utils.firework.FireworkUtil;
import nl.thedutchruben.mccore.utils.message.MessageUtil;
import nl.thedutchruben.playtime.Playtime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ruben
 * @version 1.0
 */
public class RepeatingMilestone {

    /**
     * The list of items to give the player.
     */
    private transient List<ItemStack> itemStackObjects;

    /**
     * The name of the milestone.
     */
    @SerializedName("_id")
    private String milestoneName;
    @SerializedName("online_time")
    private long onlineTime;
    @SerializedName("item_stacks")
    private List<Map<String, Object>> itemStacks;
    @SerializedName("commands")
    private List<String> commands;
    @SerializedName("firework_show")
    private boolean fireworkShow = false;
    @SerializedName("firework_show_amount")
    private int fireworkShowAmount = 1;
    @SerializedName("firework_show_seconds_between_firework")
    private int fireworkShowSecondsBetween = 0;
    @SerializedName("normal_milestone_override_me")
    private boolean overrideMe = false;
    @SerializedName("messages")
    private List<String> messages;


    /**
     * Apply the milestone on the player
     *
     * @param player The player to apply the milestone to
     */
    public void apply(Player player) {
        if (itemStacks != null) {
            if (itemStackObjects == null) {
                itemStackObjects = new ArrayList<>();
                for (Map<String, Object> itemStack : itemStacks) {
                    itemStackObjects.add(ItemStack.deserialize(itemStack));
                }
            }

            for (ItemStack itemStack : itemStackObjects) {
                player.getInventory().addItem(itemStack);
            }
        }

        if (commands != null) {
            Bukkit.getScheduler().runTask(Playtime.getPluginInstance(), () -> {
                for (String command : commands) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            command.replaceAll("%playername%", player.getName())
                                    .replaceAll("%player_name%", player.getName())
                                    .replaceAll("%playeruuid%", player.getUniqueId().toString())
                                    .replaceAll("%player_uuid%", player.getUniqueId().toString()));
                }
            });

        }
        if (messages != null) {
            messages.forEach(s -> {
                String formattedString = MessageUtil.translateHexColorCodes("<", ">", ChatColor.translateAlternateColorCodes('&', s));
                player.sendMessage(formattedString);
            });
        }
        if (fireworkShow) {
            Bukkit.getScheduler().runTaskAsynchronously(Playtime.getPluginInstance(), () -> {
                for (int i = 0; i < fireworkShowAmount; i++) {
                    Bukkit.getScheduler().runTask(Playtime.getPluginInstance(), () -> {
                        FireworkUtil.spawnRandomFirework(player.getLocation());
                    });
                    try {
                        Thread.sleep(fireworkShowSecondsBetween * 1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public List<Map<String, Object>> getItemStacks() {
        if (itemStacks == null) {
            itemStacks = new ArrayList<>();
        }
        return itemStacks;
    }

    public void setItemStacks(List<Map<String, Object>> itemStacks) {
        this.itemStacks = itemStacks;
    }

    public List<String> getCommands() {
        if (commands == null) {
            commands = new ArrayList<>();
        }
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public List<String> getMessages() {
        if (messages == null)
            messages = new ArrayList<>();

        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<ItemStack> getItemStackObjects() {
        return itemStackObjects;
    }

    public void setItemStackObjects(List<ItemStack> itemStackObjects) {
        this.itemStackObjects = itemStackObjects;
    }

    public boolean isFireworkShow() {
        return fireworkShow;
    }

    public void setFireworkShow(boolean fireworkShow) {
        this.fireworkShow = fireworkShow;
    }

    public int getFireworkShowAmount() {
        return fireworkShowAmount;
    }

    public void setFireworkShowAmount(int fireworkShowAmount) {
        this.fireworkShowAmount = fireworkShowAmount;
    }

    public int getFireworkShowSecondsBetween() {
        return fireworkShowSecondsBetween;
    }

    public void setFireworkShowSecondsBetween(int fireworkShowSecondsBetween) {
        this.fireworkShowSecondsBetween = fireworkShowSecondsBetween;
    }

    public boolean isOverrideMe() {
        return overrideMe;
    }

    public void setOverrideMe(boolean overrideMe) {
        this.overrideMe = overrideMe;
    }
}
