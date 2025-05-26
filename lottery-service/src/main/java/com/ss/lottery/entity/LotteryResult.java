package com.ss.lottery.entity;
import java.io.Serializable;

public class LotteryResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private Long prizeId;
    private String prizeName;
    private Boolean win;
    private Long timestamp;

    public LotteryResult() {}

    public LotteryResult(String userId, Long prizeId, String prizeName, Boolean win, Long timestamp) {
        this.userId = userId;
        this.prizeId = prizeId;
        this.prizeName = prizeName;
        this.win = win;
        this.timestamp = timestamp;
    }

    // getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Long getPrizeId() { return prizeId; }
    public void setPrizeId(Long prizeId) { this.prizeId = prizeId; }
    public String getPrizeName() { return prizeName; }
    public void setPrizeName(String prizeName) { this.prizeName = prizeName; }
    public Boolean getWin() { return win; }
    public void setWin(Boolean win) { this.win = win; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}