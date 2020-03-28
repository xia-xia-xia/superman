package com.xiaomi.nrb.superman.dao.quary;

import com.xiaomi.nrb.superman.domain.BaseQuaryParam;
import lombok.Data;

import java.util.List;

/**
 * @author niuruobing@xiaomi.com
 * @since 2019-08-05 15:16
 **/
@Data
public class ListPlanQuaryParam extends BaseQuaryParam {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 计划ids
     */
    private List<Long> planIds;
    /**
     * 计划类型，1私密计划、2公开计划、3挑战计划、4YOU计划
     */
    private Integer type;

    private List<Integer> types;

    /**
     * 搜索
     */
    private String searchKey;
}
