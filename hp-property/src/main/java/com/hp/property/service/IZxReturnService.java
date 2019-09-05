package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;

import java.util.List;

/**
 * 资产退还Service接口
 * 
 * @author hp
 * @date 2019-09-02
 */
public interface IZxReturnService
{

    List<ZxAssetManagement> selectZxReturnList(ZxAssetManagement zxAssetManagement);

    ZxAssetManagement selectZxAssetManagementById(Long id);
}
