package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;

import java.util.List;

/**
 * 资产报修Service接口
 * 
 * @author hp
 * @date 2019-09-02
 */
public interface IZxRepairsService
{

    List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement);

    List<ZxChange> selectZxChangeByAssetId(Long id);
}
