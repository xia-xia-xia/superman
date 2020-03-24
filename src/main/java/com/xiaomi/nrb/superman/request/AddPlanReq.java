package com.xiaomi.nrb.superman.request;

import lombok.Data;

import java.util.Date;

/**
 * @author niuruobing@xiaomi.com
 * @since 2019-08-04 21:25
 **/
@Data
public class AddPlanReq extends BaseRequest {
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
}
