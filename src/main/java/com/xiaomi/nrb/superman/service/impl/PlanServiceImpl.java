package com.xiaomi.nrb.superman.service.impl;

import com.xiaomi.nrb.superman.common.PageInfo;
import com.xiaomi.nrb.superman.dao.PlanMapper;
import com.xiaomi.nrb.superman.dao.RelationMapper;
import com.xiaomi.nrb.superman.dao.UserMapper;
import com.xiaomi.nrb.superman.dao.quary.ListPlanQuaryParam;
import com.xiaomi.nrb.superman.domain.Plan;
import com.xiaomi.nrb.superman.domain.Relation;
import com.xiaomi.nrb.superman.domain.User;
import com.xiaomi.nrb.superman.enums.PlanStatusEnum;
import com.xiaomi.nrb.superman.enums.PlanTypeEnum;
import com.xiaomi.nrb.superman.enums.RelationTypeEnum;
import com.xiaomi.nrb.superman.request.BaseRequest;
import com.xiaomi.nrb.superman.request.ListPlanReq;
import com.xiaomi.nrb.superman.response.PlanInfo;
import com.xiaomi.nrb.superman.response.PlanListInfo;
import com.xiaomi.nrb.superman.service.PlanService;
import com.xiaomi.nrb.superman.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author niuruobing@xiaomi.com
 * @since 2019-08-04 21:28
 **/
@Slf4j
@Service
public class PlanServiceImpl implements PlanService {

    @Resource
    private PlanMapper planMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RelationMapper relationMapper;

    /**
     * 点赞数量要求
     */
    private static final int YOU_PLAN_ZAN_NUM = 1;
    /**
     * 围观数量要求
     */
    private static final int YOU_PLAN_SEE_NUM = 5;

    @Override
    public Plan addPlan(Plan plan) {
        planMapper.insertSelective(plan);
        return (planMapper.selectByPrimaryKey(plan.getId()));
    }
    //对日期字符串进行解析和格式化输出
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");

    @Override
    public PageInfo<PlanListInfo> listPlan(ListPlanReq request) {

        List<Plan> list = null;
        int total = 0;
        if ("1".equals(request.getUpvote())) {
            Relation relation = new Relation();
            relation.setUserId(request.getUserId());
            relation.setType(RelationTypeEnum.RELATION_UPVOTE.getCode());
            List<Relation> relations = relationMapper.listBySelective(relation);
            if (CollectionUtils.isEmpty(relations)) {
                return PageInfo.<PlanListInfo>builder().list(new ArrayList<>()).total(0).build();
            }

            List<Long> planIds = new ArrayList<>();
            relations.forEach(k -> {
                planIds.add(k.getPlanId());
            });
            ListPlanQuaryParam quaryParam = new ListPlanQuaryParam();
            quaryParam.setPlanIds(planIds);
            list = planMapper.listBySelective(quaryParam);
            total = planIds.size();
        } else {
            ListPlanQuaryParam quaryParam = new ListPlanQuaryParam();
            quaryParam.setTypes(request.getTypes());
            quaryParam.setPageNo((request.getPageNo() - 1) * request.getPageSize());
            quaryParam.setPageSize(request.getPageSize());
            /*if ("personal".equals(request.getSource())) {
                parseUserPlanStatus(request.getUserId());
                quaryParam.setUserId(request.getUserId());
            }*/

            list = planMapper.listBySelective(quaryParam);
            total = planMapper.countBySelective(quaryParam);
        }


        if (CollectionUtils.isEmpty(list)) {
            return PageInfo.<PlanListInfo>builder().list(new ArrayList<>()).total(total).build();
        }
        List<PlanListInfo> listInfos = new ArrayList<>();

        list.forEach(k -> {
            User user = userService.getUserByUserId(k.getUserId());
            PlanListInfo planListInfo = new PlanListInfo();
            planListInfo.setId(k.getId());
            planListInfo.setUserId(k.getUserId());
            planListInfo.setBookType(k.getBookType());
            planListInfo.setTitle(k.getTitle());
            planListInfo.setContent(k.getContent());
            planListInfo.setCtime(simpleDateFormat.format(k.getCtime()));
            planListInfo.setType(k.getType());
            planListInfo.setNickName(user.getNickName());
            planListInfo.setAvartarUrl(user.getAvartarUrl());
            planListInfo.setGender(user.getGender());
            Relation relation = new Relation();
            //relation.setUserId(request.getUserId());
            relation.setPlanId(k.getId());
            List<Relation> relations = relationMapper.listBySelective(relation);
            if (relations == null) {
                relations = new ArrayList<>();
            }
            Integer seeNum = 0;
            Integer zanNum = 0;
            Integer challengeNum = 0;
            for (Relation relation1 : relations) {
                if (RelationTypeEnum.RELATION_SEE.getCode() == relation1.getType()) {
                    seeNum++;
                } else if (RelationTypeEnum.RELATION_UPVOTE.getCode() == relation1.getType()) {
                    zanNum++;
                } else if (RelationTypeEnum.RELATION_CHALLEGE.getCode() == relation1.getType()) {
                    challengeNum++;
                }
            }
            planListInfo.setSeeNum(seeNum);
            planListInfo.setZanNum(zanNum);
            planListInfo.setChallengeNum(challengeNum);

            //是否为挑战计划
            /*if (k.getType() == PlanTypeEnum.PLAN_CHALLENGE.getCode()) {
                String message = "用户:";
                User user1 = userMapper.selectByPrimaryKey(k.getUserId());
                message += planListInfo.getNickName();
                message += " 在 ";
                message += planListInfo.getStartTime();
                message += "挑战了该计划";

                planListInfo.setMessage(message);
            }*/
            listInfos.add(planListInfo);
        });


        return PageInfo.<PlanListInfo>builder().list(listInfos).total(total).build();
    }

    @Override
    public PlanInfo detailPlan(BaseRequest request) {
        Plan plan = planMapper.selectByPrimaryKey(request.getPlanId());
        if (plan == null) return null;
        User user = userService.getUserByUserId(plan.getUserId());
        PlanInfo planInfo = new PlanInfo();
        BeanUtils.copyProperties(plan, planInfo);
        planInfo.setYouTag(isYouPlan(plan));
        planInfo.setCtimeForShow(simpleDateFormat.format(plan.getCtime()));
        planInfo.setNickName(user.getNickName());
        planInfo.setAvartarUrl(user.getAvartarUrl());
        planInfo.setGender(user.getGender());
        if (request.getUserId() == plan.getUserId()) {
            planInfo.setTag(true);
        } else {
            planInfo.setTag(false);
        }
        planInfo.setChallengeTag(false);
        planInfo.setZanTag(false);
        planInfo.setSeeTag(false);
        //围观、点赞、挑战
        Integer seeNum = 0;
        Integer zanNum = 0;
        Integer challengeNum = 0;
        List<Long> zanUserIds = new ArrayList<>();
        List<Long> challengeUserIds = new ArrayList<>();
        Relation relation = new Relation();
        //relation.setUserId(request.getUserId());
        relation.setPlanId(request.getPlanId());
        List<Relation> relations = relationMapper.listBySelective(relation);
        if (relations == null) {
            relations = new ArrayList<>();
        }
        for (Relation k : relations) {
            if (RelationTypeEnum.RELATION_SEE.getCode() == k.getType()) {
                planInfo.setSeeTag(true);
                seeNum++;
                zanUserIds.add(k.getUserId());
            } else if (RelationTypeEnum.RELATION_UPVOTE.getCode() == k.getType()) {
                planInfo.setZanTag(true);
                zanNum++;
            } else if (RelationTypeEnum.RELATION_CHALLEGE.getCode() == k.getType()) {
                planInfo.setChallengeTag(true);
                challengeUserIds.add(k.getUserId());
                challengeNum++;
            }
        }
        planInfo.setSeeNum(seeNum);
        planInfo.setZanNum(zanNum);
        planInfo.setChallengeNum(challengeNum);
        if (!CollectionUtils.isEmpty(zanUserIds)) {
            List<String> strings = userMapper.selectAvartarUrls(zanUserIds);
            planInfo.setZanAvartarUrls(strings);
        }
        if (!CollectionUtils.isEmpty(challengeUserIds)) {
            List<String> strings = userMapper.selectAvartarUrls(challengeUserIds);
            planInfo.setChallengeAvartarUrls(strings);
        }
        return planInfo;
    }

    /**
     * 判断是否为you计划
     *
     * @author niuruobing@xiaomi.com
     * @since 2019-08-12 11:03
     */
    public boolean isYouPlan(Plan plan) {
        //私密计划
        if (plan.getType() == PlanTypeEnum.PLAN_PRIVITE.getCode()) {
            return false;
        }
        Relation relation = new Relation();
        relation.setPlanId(plan.getId());
        //1、计划已完成、点赞数量满足
        //if (plan.getStatus() == PlanStatusEnum.COMPLETE.getCode()) {
            relation.setType(RelationTypeEnum.RELATION_UPVOTE.getCode());
            int zanNum = relationMapper.countBySelective(relation);
            if (zanNum >= YOU_PLAN_ZAN_NUM) {
                return true;
            }
        //}
        //2、计划进行中、围观数量满足
        //if (plan.getStatus() == PlanStatusEnum.ONGOING.getCode()) {
            relation.setType(RelationTypeEnum.RELATION_SEE.getCode());
            int seeNum = relationMapper.countBySelective(relation);
            if (seeNum >= YOU_PLAN_SEE_NUM) {
                return true;
            }
        //}

        return false;
    }

}