package com.xiaomi.nrb.superman.response;

import com.xiaomi.nrb.superman.domain.Plan;
import lombok.Data;

import java.util.List;

/**
 * @author niuruobing@xiaomi.com
 * @since 2019-08-05 22:26
 **/
@Data
public class PlanInfo extends Plan {
    /**
     * 是否是自己的计划
     */
    private boolean tag;
    /**
     * 是否已经关注过
     */
    private boolean seePeopleTag;
    /**
     * 是否关注
     */
    private boolean seeTag;
    /**
     * 是否点赞
     */
    private boolean zanTag;
    /**
     * 是否收藏
     */
    private boolean collectTag;
    /**
     * 是否为you计划
     */
    private boolean youTag;
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
    private String ctimeForShow;
    /**
     * 关注用户头像
     */
    private List<String> seeAvartarUrls;
    /**
     * 收藏用户头像
     */
    private List<String> collectAvartarUrls;
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
