package org.getchunky.chunky.permission;

import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.util.Logging;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPermissions extends JSONObject {

    private HashMap<PermissionFlag, Boolean> flagsMap = new HashMap<PermissionFlag, Boolean>();

    /**
     * Checks to see if this permission set has a certain flag.
     *
     * @param flag Flag to check for
     * @return true if contains, false if not contains, null if no permissions set at all
     */
    public Boolean hasFlag(PermissionFlag flag) {
        return flagsMap.get(flag);
    }

    /**
     * Returns the set of flags.  It would not be wise to change this HashMap.
     *
     * @return Flag Map
     */
    public HashMap<PermissionFlag, Boolean> getFlags() {
        return flagsMap;
    }

    /**
     * Wipes the flag set
     */
    public void clearFlags() {
        flagsMap.clear();
        save();
    }

    /**
     * Sets the status of a certain flag in this permission set
     *
     * @param flag   Flag to set
     * @param status Status of the flag
     */
    public void setFlag(PermissionFlag flag, Boolean status) {
        if (status != null) {
            flagsMap.put(flag, status);
        } else {
            flagsMap.remove(flag);
        }
        save();
    }

    /**
     * Sets all the flags at once
     *
     * @param flags Set of new flags
     */
    public void setFlags(HashMap<PermissionFlag, Boolean> flags) {
        this.flagsMap = flags;
        save();
    }

    /**
     * Returns a string representation of the permissions flags
     *
     * @return String representation of permission flags
     */
    public String toString() {
        if (flagsMap.isEmpty()) return Language.NO_PERMISSIONS_SET.getString();
        String sFlags = "";
        for (Map.Entry<PermissionFlag, Boolean> flag : flagsMap.entrySet()) {
            if (!sFlags.isEmpty()) sFlags += " | ";
            sFlags += flag.getKey().getName() + ": " + (flag.getValue() ? "T" : "F");
        }
        return sFlags;
    }

    /**
     * Returns a string representation of the permissions flags that is shorter than the normal toString()
     *
     * @return Small string representation of permission flags
     */
    public String toSmallString() {
        if (flagsMap.isEmpty()) return Language.NO_PERMISSIONS_SET.getString();
        String sFlags = "";
        for (Map.Entry<PermissionFlag, Boolean> flag : flagsMap.entrySet()) {
            if (!sFlags.isEmpty()) sFlags += ", ";
            sFlags += flag.getKey().getTag() + ": " + (flag.getValue() ? "T" : "F");
        }
        return sFlags;
    }

    public void load(String json) {
        super.load(json);
        JSONObject flags = this.getJSONObject("flags");
        if (flags == null) {
            flags = new JSONObject();
            this.put("flags", flags);
        }
        if (flags.names() != null) {
            for(int i = 0; i < flags.names().length(); i++) {
                String flagName = flags.names().get(i).toString();
                PermissionFlag flag = PermissionFlags.getFlag(flagName);
                if (flag == null) {
                    Logging.warning("Permission flag \"" + flagName + "\" is missing.  Registering it as new permission flag.");
                    flag = new PermissionFlag(flagName, flagName);
                    PermissionFlags.registerPermissionFlag(flag);
                }
                try {
                    boolean hasPerm = flags.getBoolean(flagName);
                    this.flagsMap.put(flag, hasPerm);
                } catch (JSONException e) {
                    Logging.warning("Faulty permission flag data for " + flagName + ". Must be boolean value.");
                    Logging.warning(e.getMessage());
                }
            }
        }
    }

    private void save() {
        JSONObject flags = this.getJSONObject("flags");
        if (flags == null) {
            flags = new JSONObject();
            this.put("flags", flags);
        }
        for (Map.Entry<PermissionFlag, Boolean> flag : this.flagsMap.entrySet()) {
            flags.put(flag.getKey().getName(), flag.getValue());
        }
    }
}
