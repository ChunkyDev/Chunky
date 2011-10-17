package org.getchunky.chunky.event;

import java.io.Serializable;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyEvent implements Serializable {

    private final Type type;
    private final String name;

    protected ChunkyEvent(final Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type is null");
        }
        if (type == Type.CUSTOM_EVENT) {
            throw new IllegalArgumentException("Use ChunkyEvent(String) to make custom events");
        }
        this.type = type;
        this.name = null;
    }

    protected ChunkyEvent(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
        this.type = Type.CUSTOM_EVENT;
        this.name = name;
    }

    /**
     * Gets the Type of this event
     *
     * @return Event type that this object represents
     */
    public final Type getType() {
        return type;
    }

    /**
     * Gets the event's name. Should only be used if getType() == Type.CUSTOM
     *
     * @return Name of this event
     */
    protected final String getName() {
        return (type != Type.CUSTOM_EVENT) ? type.toString() : name;
    }

    /**
     * Represents an events priority in execution
     */
    public enum Priority {
        /**
         * Event call is of very low importance and should be ran first, to allow
         * other plugins to further customise the outcome
         */
        Lowest,
        /**
         * Event call is of low importance
         */
        Low,
        /**
         * Event call is neither important or unimportant, and may be ran normally
         */
        Normal,
        /**
         * Event call is of high importance
         */
        High,
        /**
         * Event call is critical and must have the final say in what happens
         * to the event
         */
        Highest,
        /**
         * Event is listened to purely for monitoring the outcome of an event.
         * <p/>
         * No modifications to the event should be made under this priority
         */
        Monitor
    }

    /**
     * Represents a category used by Type
     */
    public enum Category {

        /**
         * Represents an Object Event
         */
        OBJECT,

        /**
         * Represents a chunkyPlayer Event
         */
        PLAYER,

        /**
         * Represents a chunky command event
         */
        COMMAND,

        /**
         * Represents any miscellaneous events
         */
        MISCELLANEOUS;
    }

    /**
     * Provides a lookup for all core events
     */
    public enum Type {

        /**
         * OBJECT EVENTS
         */

        /**
         * Represents an object's name change event
         */
        OBJECT_NAME(Category.OBJECT),
        //OBJECT_ADD_OWNER (Category.OBJECT),
        //OBJECT_REMOVE_OWNER (Category.OBJECT),
        OBJECT_SET_OWNER(Category.OBJECT),


        /**
         * PLAYER EVENTS
         */

        /**
         * Represents a player switching chunks
         */
        PLAYER_CHUNK_CHANGE(Category.PLAYER),

        PLAYER_BUILD(Category.PLAYER),

        PLAYER_DESTROY(Category.PLAYER),

        PLAYER_ITEM_USE(Category.PLAYER),

        PLAYER_SWITCH(Category.PLAYER),

        PLAYER_CHUNK_CLAIM(Category.PLAYER),

        PLAYER_CHUNK_UNCLAIM(Category.PLAYER),

        PLAYER_CLAIM_LIMIT_QUERY(Category.PLAYER),

        /**
         * COMMAND EVENTS
         */

        /**
         * Represents the command event
         */
        COMMAND_PROCESS(Category.COMMAND),
        COMMAND_HELP(Category.COMMAND),
        COMMAND_LIST(Category.COMMAND),

        /**
         * MISCELLANEOUS EVENTS
         */

        /**
         * Represents a custom event
         */
        CUSTOM_EVENT(Category.MISCELLANEOUS);

        private final Category category;

        private Type(Category category) {
            this.category = category;
        }

        /**
         * Gets the Category assigned to this event
         *
         * @return Category of this Event.Type
         */
        public Category getCategory() {
            return category;
        }
    }

    public enum Result {

        /**
         * Deny the event.
         * Depending on the event, the action indicated by the event will either not take place or will be reverted.
         * Some actions may not be denied.
         */
        DENY,
        /**
         * Neither deny nor allow the event.
         * The server will proceed with its normal handling.
         */
        DEFAULT,
        /**
         * Allow / Force the event.
         * The action indicated by the event will take place if possible, even if the server would not normally allow the action.
         * Some actions may not be allowed.
         */
        ALLOW;
    }
}
