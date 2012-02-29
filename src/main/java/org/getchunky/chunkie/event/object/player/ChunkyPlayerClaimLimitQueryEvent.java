package org.getchunky.chunkie.event.object.player;

import org.getchunky.chunkie.object.IChunkyPlayer;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerClaimLimitQueryEvent extends ChunkyPlayerEvent {

    private Integer limit;

    public ChunkyPlayerClaimLimitQueryEvent(IChunkyPlayer chunkyPlayer, Integer limit) {
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
