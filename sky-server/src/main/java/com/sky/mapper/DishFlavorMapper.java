package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     */
    //这里要用到动态sql，就不再写注释去xml文件里写了
    void insertBatch(List<DishFlavor> flavors);

}
