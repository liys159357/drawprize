package com.ss.lottery.entity.vo;

import java.util.Date;

public class UserLotteryRecordVO {
    private String prizeName;
    private Date createTime;

    // getters and setters
    public String getPrizeName() { return prizeName; }
    public void setPrizeName(String prizeName) { this.prizeName = prizeName; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
