package org.getchunky.chunky.event.object.player;

import org.getchunky.chunky.object.ChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerClaimLimitQueryEvent extends ChunkyPlayerEvent {

    private Integer limit;

    public ChunkyPlayerClaimLimitQueryEvent(ChunkyPlayer chunkyPlayer, Integer limit) {
        super(Type.PLAYER_CLAIM_LIMIT_QUERY, chunkyPlayer);
        this.chunkyPlayer = chunkyPlayer;
        this.limit = limit;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
