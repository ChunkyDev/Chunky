package org.getchunky.chunky.object;

public enum ChunkFace {
    NORTH(-1, 0),
    EAST(0, -1),
    SOUTH(1, 0),
    WEST(0, 1),
    NORTH_EAST(NORTH, EAST),
    NORTH_WEST(NORTH, WEST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),
    WEST_NORTH_WEST(WEST, NORTH_WEST),
    NORTH_NORTH_WEST(NORTH, NORTH_WEST),
    NORTH_NORTH_EAST(NORTH, NORTH_EAST),
    EAST_NORTH_EAST(EAST, NORTH_EAST),
    EAST_SOUTH_EAST(EAST, SOUTH_EAST),
    SOUTH_SOUTH_EAST(SOUTH, SOUTH_EAST),
    SOUTH_SOUTH_WEST(SOUTH, SOUTH_WEST),
    WEST_SOUTH_WEST(WEST, SOUTH_WEST),
    SELF(0, 0);

    private final int modX;
    private final int modZ;

    private ChunkFace(final int modX, final int modZ) {
        this.modX = modX;
        this.modZ = modZ;
    }

    private ChunkFace(final ChunkFace face1, final ChunkFace face2) {
        this.modX = face1.getModX() + face2.getModX();
        this.modZ = face1.getModZ() + face2.getModZ();
    }

    /**
     * Get the amount of CHUNK X-coordinates to modify to get the represented chunk
     * @return Amount of CHUNK X-coordinates to modify
     */
    public int getModX() {
        return modX;
    }

    /**
     * Get the amount of CHUNK Z-coordinates to modify to get the represented chunk
     * @return Amount of CHUNK Z-coordinates to modify
     */
    public int getModZ() {
        return modZ;
    }

    public ChunkFace getOppositeFace() {
        switch (this) {
        case NORTH:
            return ChunkFace.SOUTH;

        case SOUTH:
            return ChunkFace.NORTH;

        case EAST:
            return ChunkFace.WEST;

        case WEST:
            return ChunkFace.EAST;

        case NORTH_EAST:
            return ChunkFace.SOUTH_WEST;

        case NORTH_WEST:
            return ChunkFace.SOUTH_EAST;

        case SOUTH_EAST:
            return ChunkFace.NORTH_WEST;

        case SOUTH_WEST:
            return ChunkFace.NORTH_EAST;

        case WEST_NORTH_WEST:
            return ChunkFace.EAST_SOUTH_EAST;

        case NORTH_NORTH_WEST:
            return ChunkFace.SOUTH_SOUTH_EAST;

        case NORTH_NORTH_EAST:
            return ChunkFace.SOUTH_SOUTH_WEST;

        case EAST_NORTH_EAST:
            return ChunkFace.WEST_SOUTH_WEST;

        case EAST_SOUTH_EAST:
            return ChunkFace.WEST_NORTH_WEST;

        case SOUTH_SOUTH_EAST:
            return ChunkFace.NORTH_NORTH_WEST;

        case SOUTH_SOUTH_WEST:
            return ChunkFace.NORTH_NORTH_EAST;

        case WEST_SOUTH_WEST:
            return ChunkFace.EAST_NORTH_EAST;

        case SELF:
            return ChunkFace.SELF;
        }

        return ChunkFace.SELF;
    }
}
