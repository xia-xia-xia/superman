package com.xiaomi.nrb.superman.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Plan implements Serializable {
    /**
     * 计划id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 书籍名称
     */
    private String book;
    /**
     * 书籍类型
     */
    private String bookType;
    /**
     * 计划标题
     */
    private String title;
    /**
     * 计划内容
     */
    private String content;
    /**
     * 计划类型，1私密计划、2公开计划、3挑战计划
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date ctime;
    /**
     * 修改时间
     */
    private Date utime;
}