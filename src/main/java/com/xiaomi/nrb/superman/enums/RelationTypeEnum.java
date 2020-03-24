package com.xiaomi.nrb.superman.enums;


/**
 * @author niuruobing@xiaomi.com
 * @since 2019-08-04 09:14
 **/
public enum RelationTypeEnum {

    RELATION_SEE(1, "关注作者"),
    RELATION_UPVOTE(2, "点赞计划"),
    RELATION_COLLECT(3, "收藏感悟");


    private int code;
    private String desc;

    RelationTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
}
