package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;

import java.util.List;

/**
 * 资产退还Service接口
 * 
 * @author hp
 * @date 2019-09-02
 */
public interface IZxReturnService
{

    List<ZxChange> selectZxReturnList(ZxChange zxChange,String campus);

    ZxAssetManagement selectZxAssetManagementById(Long id);

    /*List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement);*/


    int insertManagementAndChange(ZxChange zxChange, Long[] Ids);

    List<ZxChange> selectZxChangeById(Long id);

    List<ZxAssetManagement> selectManagementList(ZxChange zxChange2, ZxAssetManagement zxAssetManagement);

    /*    List<ZxAssetManagement> selectZxAssetManagementsById(String ids);*/
}
