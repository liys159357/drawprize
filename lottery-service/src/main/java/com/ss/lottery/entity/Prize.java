package com.ss.lottery.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("prize")
public class Prize {
    @TableId
    private Long id;
    private String name;
    private Integer total;
    private Integer remaining;
    private Boolean enabled;
}