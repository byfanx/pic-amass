package com.byfan.picamass.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: fby
 * @Description 用于封装返回的分页数据
 * @Version 1.0
 * @Date: 2021/11/23 14:14
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageData<T> {
    Integer currentPage = 0;
    Integer pageSize = 20;
    Integer total = 0;
    Integer totalPage = 0;
    List<T> list = new ArrayList<>();

    public Integer getTotalPage(){
        return total % pageSize == 0 ? (total / pageSize) : ((totalPage / pageSize) + 1);
    }
}
