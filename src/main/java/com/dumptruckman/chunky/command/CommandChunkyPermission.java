package com.dumptruckman.chunky.command;

import java.util.*;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.exceptions.ChunkyException;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.module.ChunkyCommandExecutor;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPermissions;
import com.dumptruckman.chunky.object.ChunkyPlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author dumptruckman, SwearWord
 */
public class CommandChunkyPermission implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)){
        	Language.IN_GAME_ONLY.bad(sender);
        	return;
        }

        if (args.length == 0){
            // TODO: Maybe this should show a readout of permissions given or something.
        	Language.CMD_CHUNKY_PERMISSION_HELP.help(sender);
        	return;
        }
        
        setPerms(ChunkyManager.getChunkyPlayer((Player) sender), args);
    }
        
    public void setPerms(ChunkyPlayer cPlayer, String[] args){

        String permissions = "";
        String[] tokens = args[0].split(":", 2);
        ChunkyObject target;
        
        if (tokens.length == 1){
        	if (!cPlayer.getCurrentChunk().isDirectlyOwnedBy(cPlayer)){
        		Language.CHUNK_NOT_OWNED.bad(cPlayer);
        		return;
        	}
        	target = cPlayer.getCurrentChunk();
        	permissions = tokens[0];
        } else if (tokens.length == 2){
        	if (tokens[0].equals("*")){
        		HashMap<Integer, HashSet<ChunkyObject>> ownables = cPlayer.getOwnables();
        		if (!ownables.containsKey(ChunkyChunk.class.getName().hashCode())){
        			Language.CHUNK_NONE_OWNED.bad(cPlayer);
        			return;
        		}
        		target = cPlayer;
        	} else {
        		Language.FEATURE_NYI.bad(cPlayer);
        		return;
        	}
        	permissions = tokens[1];
        }
        
        // No player or group defined
        if (args.length == 1){
            // TODO fix when objects have their own flags.
        	Language.ERROR.bad(cPlayer, "That feature does not exist yet.");
    		return;
        } else if (args.length == 2) {
        	if (args[1].startsWith("g:")){
        		Language.FEATURE_NYI.bad(cPlayer);
        		return;
        	} else {
                ChunkyPlayer permPlayer = ChunkyManager.getChunkyPlayer(args[1]);
                int state = 0;
                if (permissions.startsWith("-")) state = -1;
                if (permissions.startsWith("+")) state = 1;
                for (char perm : permissions.toCharArray()){
                    ChunkyPermissions.Flags flag = ChunkyPermissions.Flags.get(perm);
                    if (flag == null) continue;
                    switch (state) {
                        case -1:
                            //TODO can't finish, too late.  Realized i have a strange situation when i have state == 0;
                    }
                }
        	}
        } else if (args.length > 2){
        	Language.CMD_CHUNKY_PERMISSION_HELP.bad(cPlayer);
        	return;
        }
        
        
    }
    
    private ChunkyPermissions.Flags stringToPerm(String flag){
    	if(flag.equalsIgnoreCase("build"))
    		return ChunkyPermissions.Flags.BUILD;
    	else if (flag.equalsIgnoreCase("destroy"))
    		return ChunkyPermissions.Flags.DESTROY;
    	else if (flag.equalsIgnoreCase("itemuse"))
    		return ChunkyPermissions.Flags.ITEMUSE;
    	else if (flag.equalsIgnoreCase("switch"))
    		return ChunkyPermissions.Flags.SWITCH;
    	//TODO: Add exception?
    	return null;
    }
}