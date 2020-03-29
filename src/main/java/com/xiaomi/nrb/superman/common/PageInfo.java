package com.xiaomi.nrb.superman.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author niuruobing@xiaomi.com
 * @since 2019-08-05 10:16
 **/
@Data
@Builder
public class PageInfo<T> {
    private Integer total;
    //private Integer commentTotal;
    //private List<T> commentList;
    private List<T> list;
}
