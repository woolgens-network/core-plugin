package net.woolgens.core.spigot.command;

import net.woolgens.library.spigot.command.exception.CommandException;
import net.woolgens.library.spigot.command.spigot.SpigotArguments;
import net.woolgens.library.spigot.command.spigot.template.player.PlayerCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class TestCommand extends PlayerCommand {

    public TestCommand() {
        super("test");
    }

    @Override
    public boolean send(Player player, SpigotArguments spigotArguments) throws CommandException {
        TestGUI gui = new TestGUI(player);
        gui.openInventory();
        return true;
    }
}
