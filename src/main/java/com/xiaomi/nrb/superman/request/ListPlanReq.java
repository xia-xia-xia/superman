package com.xiaomi.nrb.superman.request;

import lombok.Data;

import java.util.List;

/**
 * @author niuruobing@xiaomi.com
 * @since 2019-08-05 10:13
 **/
@Data
public class ListPlanReq extends BaseRequest {
    /**
     * 计划类型，1私密计划、2公开计划、3挑战计划、4YOU计划
     */
    private List<Integer> types;
    /**
     * 页面来源
     */
    private String source;
    /**
     * 点赞标记
     */
    private String upvote;
    /**
     * 收藏标记
     */
    private String collect;
    /**
     * 搜索
     */
    private String searchKey;
    /**
     * 书籍类型
     */
    private String bookType;
    /**
     * 所关注用户id
     */
    private Long seeUserId;
}
