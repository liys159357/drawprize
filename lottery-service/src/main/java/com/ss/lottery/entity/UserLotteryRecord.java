package com.ss.lottery.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_lottery_record")
public class UserLotteryRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private Long prizeId;
    private Boolean result;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
