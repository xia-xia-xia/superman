package com.xiaomi.nrb.superman.response;

import lombok.Data;

@Data
public class RelationListInfo {
    /**
     * 发布感悟的用户id
     */
    private Long planUserId;
    /**
     * 微信昵称
     */
    private String nickName;
    /**
     * 微信头像
     */
    private String avartarUrl;
}
