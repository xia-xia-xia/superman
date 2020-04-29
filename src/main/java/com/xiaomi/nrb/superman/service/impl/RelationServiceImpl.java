package com.xiaomi.nrb.superman.service.impl;

import com.xiaomi.nrb.superman.common.PageInfo;
import com.xiaomi.nrb.superman.dao.PlanMapper;
import com.xiaomi.nrb.superman.dao.RelationMapper;
import com.xiaomi.nrb.superman.dao.quary.ListRelationQuaryParam;
import com.xiaomi.nrb.superman.domain.Plan;
import com.xiaomi.nrb.superman.domain.Relation;
import com.xiaomi.nrb.superman.domain.User;
import com.xiaomi.nrb.superman.enums.PlanTypeEnum;
import com.xiaomi.nrb.superman.enums.RelationTypeEnum;
import com.xiaomi.nrb.superman.request.AddRelationReq;
import com.xiaomi.nrb.superman.request.BaseRequest;
import com.xiaomi.nrb.superman.response.CommentListInfo;
import com.xiaomi.nrb.superman.response.PlanInfo;
import com.xiaomi.nrb.superman.response.RelationListInfo;
import com.xiaomi.nrb.superman.service.PlanService;
import com.xiaomi.nrb.superman.service.RelationService;
import com.xiaomi.nrb.superman.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author niuruobing@xiaomi.com
 * @since 2019-08-08 08:05
 **/
@Service
public class RelationServiceImpl implements RelationService {

    @Resource
    private PlanService planService;

    @Resource
    private UserService userService;

    @Resource
    private RelationMapper relationMapper;

    @Resource
    private PlanMapper planMapper;

    @Override
    public PlanInfo addRelation(AddRelationReq request) {
        PlanInfo planInfo = planService.detailPlan(request);
        if (planInfo == null) {
            return planInfo;
        }
        //自己可以收藏、点赞自己的感悟，但不可以关注自己
        Integer type = null;

        if (RelationTypeEnum.RELATION_SEE.getCode() == request.getType() && !planInfo.isTag()) {
            type = RelationTypeEnum.RELATION_SEE.getCode();
        } else if (RelationTypeEnum.RELATION_UPVOTE.getCode() == request.getType() && !planInfo.isZanTag()) {
            type = RelationTypeEnum.RELATION_UPVOTE.getCode();
        } else if (RelationTypeEnum.RELATION_COLLECT.getCode() == request.getType() && !planInfo.isCollectTag()) {
            type = RelationTypeEnum.RELATION_COLLECT.getCode();
        }

        if (null == type) {
            return planInfo;
        }
        Relation relation = new Relation();
        relation.setUserId(request.getUserId());
        relation.setPlanId(request.getPlanId());
        relation.setPlanUserId(planInfo.getUserId());
        relation.setType(type);
        relation.setCtime(new Date());
        relationMapper.insertSelective(relation);

        //是否更新为YOU计划
        if (planInfo.getType() != PlanTypeEnum.PLAN_YOU.getCode() && planService.isYouPlan(planInfo)) {
            planInfo.setYouTag(true);
            Plan plan = new Plan();
            plan.setId(planInfo.getId());
            plan.setType(PlanTypeEnum.PLAN_YOU.getCode());
            planMapper.updateByPrimaryKeySelective(plan);

        }
        return  planService.detailPlan(request);
    }

    @Override
    public PageInfo<RelationListInfo> listRelation(BaseRequest request) {
        List<Relation> relationList=null;
        int relationTotal=0;
        ListRelationQuaryParam listRelationQuaryParam=new ListRelationQuaryParam();
        listRelationQuaryParam.setUserId(request.getUserId());
        listRelationQuaryParam.setPageNo((request.getPageNo() - 1) * request.getPageSize());
        listRelationQuaryParam.setPageSize(request.getPageSize());
        //relationList=relationMapper.listBySelective(listRelationQuaryParam);
        //relationTotal=relationMapper.countBySelective(listRelationQuaryParam);
        relationList.forEach(k->{
                    User user=userService.getUserByUserId(k.getPlanUserId());
                    RelationListInfo relationListInfo=new RelationListInfo();
                    relationListInfo.setAvartarUrl(user.getAvartarUrl());
                    relationListInfo.setAvartarUrl(user.getNickName());
                }
                );
        return null;
        //return PageInfo.<RelationListInfo>builder().list(relationList).total(relationTotal).build();
    }
}
