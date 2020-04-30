package com.xiaomi.nrb.superman.service.impl;

import com.xiaomi.nrb.superman.common.PageInfo;
import com.xiaomi.nrb.superman.dao.PlanMapper;
import com.xiaomi.nrb.superman.dao.RelationMapper;
import com.xiaomi.nrb.superman.dao.UserMapper;
import com.xiaomi.nrb.superman.dao.quary.ListPlanQuaryParam;
import com.xiaomi.nrb.superman.domain.Plan;
import com.xiaomi.nrb.superman.domain.Relation;
import com.xiaomi.nrb.superman.domain.User;
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
     * 收藏数量要求
     */
    private static final int YOU_PLAN_COLLECT_NUM = 1;

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
        if ("1".equals(request.getCollect())) {
            Relation relation = new Relation();
            relation.setUserId(request.getUserId());
            relation.setType(RelationTypeEnum.RELATION_COLLECT.getCode());
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
        }else if ("1".equals(request.getUpvote())) {
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
            quaryParam.setSearchKey(request.getSearchKey());
            quaryParam.setBookType(request.getBookType());
            quaryParam.setUserId(request.getUserId());
            if ("personal".equals(request.getSource())) {
                quaryParam.setUserId(request.getUserId());
            }

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
            planListInfo.setBook(k.getBook());
            planListInfo.setTitle(k.getTitle());
            planListInfo.setContent(k.getContent());
            planListInfo.setCtime(simpleDateFormat.format(k.getCtime()));
            planListInfo.setType(k.getType());
            planListInfo.setNickName(user.getNickName());
            planListInfo.setAvartarUrl(user.getAvartarUrl());
            planListInfo.setGender(user.getGender());
            //关注数量
            Integer seeNum =0;
            Relation relation00=new Relation();
            relation00.setType(RelationTypeEnum.RELATION_SEE.getCode());
            relation00.setPlanUserId(k.getUserId());
            seeNum=relationMapper.countBySelective(relation00);

            Relation relation = new Relation();
            relation.setPlanId(k.getId());
            List<Relation> relations = relationMapper.listBySelective(relation);
            if (relations == null) {
                relations = new ArrayList<>();
            }
            Integer zanNum = 0;
            Integer collectNum=0;
            for (Relation relation1 : relations) {
                if (RelationTypeEnum.RELATION_UPVOTE.getCode() == relation1.getType()) {
                    zanNum++;
                } else if (RelationTypeEnum.RELATION_COLLECT.getCode() == relation1.getType()) {
                    collectNum++;
                }
            }
            planListInfo.setSeeNum(seeNum);
            planListInfo.setZanNum(zanNum);
            planListInfo.setCollectNum(collectNum);
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

        //每个作者只能关注一次
        Relation relation0=new Relation();
        relation0.setType(RelationTypeEnum.RELATION_SEE.getCode());
        relation0.setUserId(request.getUserId());
        relation0.setPlanUserId(plan.getUserId());
        List<Relation> relationSee=relationMapper.listBySelective(relation0);
        if(!CollectionUtils.isEmpty(relationSee)){
            planInfo.setSeePeopleTag(true);
        }else{
            planInfo.setSeePeopleTag(false);
        }

        if (request.getUserId() == plan.getUserId()) {
            planInfo.setTag(true);
        } else {
            planInfo.setTag(false);
        }
        planInfo.setCollectTag(false);
        planInfo.setZanTag(false);
        planInfo.setSeeTag(false);

        //关注数量
        Integer seeNum =0;
        Relation relation00=new Relation();
        relation00.setType(RelationTypeEnum.RELATION_SEE.getCode());
        relation00.setPlanUserId(plan.getUserId());
        seeNum=relationMapper.countBySelective(relation00);
        //关注用户头像展示
        List<Long> seeUserIds = new ArrayList<>();
        List<Relation> avartarList=relationMapper.listBySelective(relation00);
        if (avartarList == null) {
            avartarList = new ArrayList<>();
        }
        for (Relation k : avartarList){
            seeUserIds.add(k.getUserId());
        }
        if (!CollectionUtils.isEmpty(seeUserIds)) {
            List<String> strings = userMapper.selectAvartarUrls(seeUserIds);
            planInfo.setSeeAvartarUrls(strings);
        }

        //点赞、收藏
        Integer zanNum = 0;
        Integer collectNum = 0;
        List<Long> collectUserIds = new ArrayList<>();
        Relation relation = new Relation();
        relation.setPlanId(request.getPlanId());
        List<Relation> relations = relationMapper.listBySelective(relation);
        if (relations == null) {
            relations = new ArrayList<>();
        }
        for (Relation k : relations) {
            if (RelationTypeEnum.RELATION_UPVOTE.getCode() == k.getType()) {
                planInfo.setZanTag(true);
                zanNum++;
            } else if (RelationTypeEnum.RELATION_COLLECT.getCode() == k.getType()) {
                planInfo.setCollectTag(true);
                collectUserIds.add(k.getUserId());
                collectNum++;
            }
        }
        planInfo.setSeeNum(seeNum);
        planInfo.setZanNum(zanNum);
        planInfo.setCollectNum(collectNum);
        if (!CollectionUtils.isEmpty(seeUserIds)) {
            List<String> strings = userMapper.selectAvartarUrls(seeUserIds);
            planInfo.setSeeAvartarUrls(strings);
        }
        if (!CollectionUtils.isEmpty(collectUserIds)) {
            List<String> strings = userMapper.selectAvartarUrls(collectUserIds);
            planInfo.setCollectAvartarUrls(strings);
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
        relation.setType(RelationTypeEnum.RELATION_UPVOTE.getCode());
        int zanNum = relationMapper.countBySelective(relation);
        if (zanNum >= YOU_PLAN_ZAN_NUM) {
            return true;
        }else {
            relation.setType(RelationTypeEnum.RELATION_COLLECT.getCode());
            int collectNum = relationMapper.countBySelective(relation);
            if (collectNum >= YOU_PLAN_COLLECT_NUM) {
                return true;
            }
        }
        return false;
    }

}
