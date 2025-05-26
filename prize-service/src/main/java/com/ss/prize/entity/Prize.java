package com.ss.prize.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("prize")
public class Prize {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer total;
    private Integer remaining;
    private Boolean enabled;
}
