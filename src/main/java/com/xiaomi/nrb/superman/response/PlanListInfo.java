package com.xiaomi.nrb.superman.response;

import lombok.Data;


/**
 * 计划列表实体
 *
 * @author niuruobing@xiaomi.com
 * @since 2019-08-05 11:10
 **/
@Data
public class PlanListInfo {
    /**
     * 计划id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
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
     * 书籍名称
     */
    private String book;
    /**
     * 关注数量
     */
    private Integer seeNum;
    /**
     * 点赞数量
     */
    private Integer zanNum;
    /**
     * 收藏数量
     */
    private Integer collectNum;
    /**
     * 创建时间
     */
    private String ctime;
    /**
     * 计划类型，1私密计划、2公开计划、3挑战计划
     */
    private Integer type;
    /**
     * 动态消息
     */
    private String message;
    /**
     * 微信昵称
     */
    private String nickName;
    /**
     * 微信头像
     */
    private String avartarUrl;
    /**
     * 性别 0未知、1男、2女
     */
    private Integer gender;
}
