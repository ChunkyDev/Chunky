package com.dumptruckman.chunky.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
        	Language.CMD_CHUNKY_PERMISSION_HELP.good(sender);
        	return;
        }
        
        
        
        Player player = (Player) sender;

        String[] tokens = args[0].split(":");
        String[] permissions;
        
        ArrayList<ChunkyChunk> chunks = new ArrayList<ChunkyChunk>();
        ChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer(player);
        
        if (tokens.length == 1){
        	if (!cPlayer.getCurrentChunk().isDirectlyOwnedBy(cPlayer)){
        		Language.CHUNK_NOT_OWNED.bad(sender);
        		return;
        	}
        	chunks.add(cPlayer.getCurrentChunk());
        	permissions = tokens[0].split(",");
        } else if (tokens.length == 2){
        	if (tokens[0].equals("*")){
        		HashMap<Integer, HashSet<ChunkyObject>> ownables = cPlayer.getOwnables();
        		if (!ownables.containsKey(ChunkyChunk.class.getName().hashCode())){
        			Language.CHUNK_NONE_OWNED.bad(sender);
        			return;
        		}
        		HashSet<ChunkyObject> ownedChunks = ownables.get(ChunkyChunk.class.getName().hashCode());
        		for (ChunkyObject chunkyObject : ownedChunks){
        			chunks.add((ChunkyChunk) chunkyObject);
        		}
        	} else {
        		Language.ERROR.bad(sender, "That feature does not exist yet.");
        		return;
        	}
        	permissions = tokens[1].split(",");
        } else {
        	Language.CMD_CHUNKY_PERMISSION_HELP.bad(sender);
        	return;
        }
        
        
        // No player or group defined
        if (args.length == 1){
        	Language.ERROR.bad(sender, "That feature does not exist yet.");
    		return;
        } else if (args.length == 2) {
        	if (args[1].startsWith("g:")){
        		Language.ERROR.bad(sender, "That feature does not exist yet.");
        		return;
        	} else {
        		ChunkyPlayer target = ChunkyManager.getChunkyPlayer(args[1]);
        		for (ChunkyChunk chunk : chunks){
        			for (String perm : permissions){
        				boolean status = false;
        				if(perm.startsWith("+") || perm.startsWith("-")){
        					if(perm.startsWith("+")){
            					status = true;
            				} else if (perm.startsWith("-")){
            					status = false;
            				}
        					target.setPerm(chunk, stringToPerm(perm.substring(1)), status);
        				} else {
        					status = !target.hasPerm(chunk, stringToPerm(perm));
        					target.setPerm(chunk, stringToPerm(perm), status);
        				}
        			}
        		}
        	}
        } else if (args.length > 2){
        	Language.CMD_CHUNKY_PERMISSION_HELP.bad(sender);
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