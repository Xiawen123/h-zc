package com.hp.property.mapper;

import com.hp.common.base.BaseMapper;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * 资产退还Mapper接口
 * 
 * @author hp
 * @date 2019-09-02
 */
@MapperScan
public interface ZxReturnMapper
{

    List<ZxChange> selectZxChangeByAssetsId(Long id);

    List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement);

    ZxAssetManagement selectZxAssetManagementById(Long id);
}
