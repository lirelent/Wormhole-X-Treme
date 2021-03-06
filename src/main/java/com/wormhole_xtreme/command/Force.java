/*
 *   Wormhole X-Treme Plugin for Bukkit
 *   Copyright (C) 2011  Ben Echols
 *                       Dean Bailey
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.wormhole_xtreme.command;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wormhole_xtreme.config.ConfigManager;
import com.wormhole_xtreme.config.ConfigManager.StringTypes;
import com.wormhole_xtreme.model.Stargate;
import com.wormhole_xtreme.model.StargateManager;
import com.wormhole_xtreme.permissions.WXPermissions;
import com.wormhole_xtreme.permissions.WXPermissions.PermissionType;
import com.wormhole_xtreme.WormholeXTreme;

// TODO: Auto-generated Javadoc
/**
 * The Class Force.
 *
 * @author alron
 */
public class Force implements CommandExecutor {

    /**
     * Instantiates a new force.
     *
     * @param wormholeXTreme the wormhole x treme
     */
    public Force(WormholeXTreme wormholeXTreme) {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1 && args.length <= 2)
        {
            boolean allowed = false;
            Player player = null;
            if (CommandUtlities.playerCheck(sender))
            {
                player = (Player) sender;
                allowed = WXPermissions.checkWXPermissions(player, PermissionType.CONFIG);
            }
            else 
            {
                allowed = true;
            }
            if (allowed)
            {
                boolean close = false;
                boolean drop = false;
                if (args[0].equals("close"))
                {
                    close = true;
                }
                else if (args[0].equals("drop"))
                {
                    drop = true;
                }
                if (args.length == 2)
                {
                    if (args[1].equals("close"))
                    {
                        close = true;
                    }
                    else if (args[1].equals("drop"))
                    {
                        drop = true;
                    }
                }
                ArrayList<Stargate> gates = StargateManager.GetAllGates();
                ArrayList<String> activelist = new ArrayList<String>();
                ArrayList<String> droplist = new ArrayList<String>();
                for ( Stargate gate : gates )
                {
                    if (gate.Active && close)
                    {
                        activelist.add(gate.Name);
                        gate.ShutdownStargate();
                    }
                    if (gate.IrisActive && drop)
                    {
                        droplist.add(gate.Name);
                        gate.ToggleIrisActive();
                    }
                }
                if (close && !activelist.isEmpty())
                {
                    StringBuilder deactivated = new StringBuilder();
                    sender.sendMessage(ConfigManager.normalheader + "Forced Closed Gate(s)\u00A73::");
                    for ( int i = 0; i < activelist.size(); i++)
                    {
                        deactivated.append("\u00A77" + activelist.get(i) );
                        if ( i != activelist.size() - 1 )
                        {
                            deactivated.append("\u00A78, ");
                        }
                        if (deactivated.toString().length() >= 75)
                        {
                            sender.sendMessage(deactivated.toString());
                            deactivated = new StringBuilder();
                        }
                    }
                    if (!deactivated.toString().equals("")) {
                        sender.sendMessage(deactivated.toString());
                    }
                }
                
                if (drop && !droplist.isEmpty())
                {
                    StringBuilder dropped = new StringBuilder();
                    sender.sendMessage(ConfigManager.normalheader + "Forced Dropped Iris(es)\u00A73::");
                    for ( int i = 0; i < droplist.size(); i++)
                    {
                        dropped.append("\u00A77" + droplist.get(i) );
                        if ( i != droplist.size() - 1 )
                        {
                            dropped.append("\u00A78, ");
                        }
                        if (dropped.toString().length() >= 75)
                        {
                            sender.sendMessage(dropped.toString());
                            dropped = new StringBuilder();
                        }
                    }
                    if (!dropped.toString().equals("")) {
                        sender.sendMessage(dropped.toString());
                    }
                }
                if (player != null)
                {
                    WormholeXTreme.thisPlugin.prettyLog(Level.INFO, false, "Player: \"" + player.getName() + "\" ran wxforce close=\"" + close + "\" drop=\"" + drop + "\"" );
                }
            }
            else 
            {
                sender.sendMessage(ConfigManager.output_strings.get(StringTypes.PERMISSION_NO));
            }
            return true;
        }
        else 
        {
            return false;
        }
    }


}
