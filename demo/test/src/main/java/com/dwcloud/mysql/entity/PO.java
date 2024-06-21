package com.dwcloud.mysql.entity;

//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableLogic;
//import com.dddframework.data.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
//@OrderBy("create_time")
public class PO {
    private String id;
    private String tenantId;
    private String systemId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean delFlag;
}
