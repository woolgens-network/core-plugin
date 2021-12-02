package net.woolgens.core.spigot.command;

import net.woolgens.library.spigot.gui.button.event.ClickButtonEvent;
import net.woolgens.library.spigot.gui.template.PlayerGUI;
import net.woolgens.library.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class TestGUI extends PlayerGUI {

    public TestGUI(Player chosen) {
        super("Test", 1, chosen);

        String test = useState("test", 1);


        addStatefulButton(0, () -> new ItemBuilder(Material.ACACIA_LEAVES).setName("Test Button: " + getState(test)).build(), (ClickButtonEvent) (player, event) -> {
            player.sendMessage("Increased!");
            int current = getState(test);
            setState(test, current + 1);
        }, test);

        addStatelessButton(4, new ItemBuilder(Material.IRON_AXE).setName("Stateless Axe").build());

        build();
    }
}
