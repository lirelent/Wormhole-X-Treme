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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wormhole_xtreme.WormholeXTreme;
import com.wormhole_xtreme.config.ConfigManager;
import com.wormhole_xtreme.config.ConfigManager.StringTypes;
import com.wormhole_xtreme.model.Stargate;
import com.wormhole_xtreme.model.StargateManager;
import com.wormhole_xtreme.permissions.WXPermissions;
import com.wormhole_xtreme.permissions.WXPermissions.PermissionType;

// TODO: Auto-generated Javadoc
/**
 * The Class WXIDC.
 *
 * @author alron
 */
public class WXIDC implements CommandExecutor {

    /**
     * Instantiates a new wXIDC.
     *
     * @param wormholeXTreme the wormhole x treme
     */
    public WXIDC(WormholeXTreme wormholeXTreme) {
        // TODO Auto-generated constructor stub
    }


    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = CommandUtlities.commandEscaper(args);  
        Player p = null;
        if ( CommandUtlities.playerCheck(sender) )
        {
            p = (Player) sender;
        }
        if ( args.length >= 1 )
        {
            Stargate s = StargateManager.GetStargate(args[0]);
            if ( s != null )
            {
                if ((p != null && (WXPermissions.checkWXPermissions(p, PermissionType.CONFIG) || (s.Owner != null && s.Owner.equals(p.getName())))) || !CommandUtlities.playerCheck(sender))
                {
                    // 2. if args other than name - do a set                
                    if ( args.length >= 2 )
                    {
                        if ( args[1].equals("-clear") )
                        {
                            // Remove from big list of all blocks
                            StargateManager.RemoveBlockIndex(s.IrisActivationBlock);
                            // Set code to "" and then remove it from stargates block list
                            s.SetIrisDeactivationCode("");
                        }
                        else
                        {
                            // Set code
                            s.SetIrisDeactivationCode(args[1]);
                            // Make sure that block is in index
                            StargateManager.AddBlockIndex(s.IrisActivationBlock, s);
                        }
                    }
                        
                    // 3. always display current value at end.
                    sender.sendMessage(ConfigManager.normalheader + "IDC for gate: " + s.Name + " is:" + s.IrisDeactivationCode);
                }
                else
                {
                    sender.sendMessage(ConfigManager.output_strings.get(StringTypes.PERMISSION_NO));
                }
            }
            else
            {
                sender.sendMessage(ConfigManager.errorheader + "Invalid Stargate: " + args[0]);

            }
            return true;
        }
        return false;
    }

}
