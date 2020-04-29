package com.xiaomi.nrb.superman.service;

import com.xiaomi.nrb.superman.common.PageInfo;
import com.xiaomi.nrb.superman.request.AddRelationReq;
import com.xiaomi.nrb.superman.request.BaseRequest;
import com.xiaomi.nrb.superman.response.PlanInfo;
import com.xiaomi.nrb.superman.response.RelationListInfo;

import java.util.List;

/**
 * @author niuruobing@xiaomi.com
 * @since 2019-08-08 08:05
 **/
public interface RelationService {

    /**
     * 围观计划
     *
     * @author niuruobing@xiaomi.com
     * @since 2019-08-08 08:07
     */
    PlanInfo addRelation(AddRelationReq addRelationReq);
    /**
     * 关注用户列表
     */
    List<RelationListInfo> listRelation(BaseRequest request);
}
