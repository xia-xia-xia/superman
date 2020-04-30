package com.xiaomi.nrb.superman.controller;

import com.xiaomi.nrb.superman.annotation.CheckLogin;
import com.xiaomi.nrb.superman.common.ApiEnum;
import com.xiaomi.nrb.superman.common.Result;
import com.xiaomi.nrb.superman.domain.Plan;
import com.xiaomi.nrb.superman.request.AddPlanReq;
import com.xiaomi.nrb.superman.request.BaseRequest;
import com.xiaomi.nrb.superman.request.ListPlanReq;
import com.xiaomi.nrb.superman.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author niuruobing@xiaomi.com
 * @since 2019-07-28 08:29
 **/
@Slf4j
@RestController
@RequestMapping("/superman/plan/apple")
public class PlanController {

    @Resource
    private PlanService planService;

    @RequestMapping("/addPlan")
    @CheckLogin
    public Result addPlan(@RequestBody AddPlanReq request) {

        try {
            Plan plan = new Plan();
            plan.setBook(request.getBook());
            plan.setBookType(request.getBookType());
            plan.setTitle(request.getTitle());
            plan.setContent(request.getContent());
            plan.setUserId(request.getUserId());
            plan.setCtime(new Date());
            plan.setType(request.getType());
            return Result.ok(planService.addPlan(plan));
        } catch (Exception e) {
            log.error("PlanController.addPlan.error:", e);
            return Result.fail(ApiEnum.ERROR.getCode());
        }

    }

    @RequestMapping("/listPlan")
    @CheckLogin
    public Result listPlan(@RequestBody ListPlanReq request) {
        try {
            if(StringUtils.isBlank(request.getSearchKey())){
                request.setSearchKey(null);
            }
            if(StringUtils.isBlank(request.getBookType())){
                request.setBookType(null);
            }
            if(null==request.getSeeUserId()){
                request.setSeeUserId(null);
            }
            return Result.ok(planService.listPlan(request));
        } catch (Exception e) {
            log.error("PlanController.listPlan.error:", e);
            return Result.fail(ApiEnum.ERROR.getCode());
        }
    }

    @RequestMapping("/detailPlan")
    @CheckLogin
    public Result detailPlan(@RequestBody BaseRequest request) {
        try {
            return Result.ok(planService.detailPlan(request));
        } catch (Exception e) {
            log.error("PlanController.listPlan.error:", e);
            return Result.fail(ApiEnum.ERROR.getCode());
        }
    }

}
