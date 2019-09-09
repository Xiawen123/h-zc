package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;

import java.util.List;

/**
 * 报废报废Service的接口  IZxDiscardService
 */
public interface IZxDiscardService {

    /**
     * 查询报废信息列表
     *
     * @param zxChange
     * @return
     */
    List<ZxChange> selectZxDiscardList(ZxChange zxChange);

    /**
     * 根据资产id查询详情
     * @param id
     * @return
     */
    ZxAssetManagement selectDiscardById(Long id);

    /**
     * 查询未报废的资产
     *
     * @param zxAssetManagement
     * @return
     */
    List<ZxAssetManagement> selectZxNoDiscardList(ZxAssetManagement zxAssetManagement);

    /**
     * 新增变更记录（报废的）
     *
     * @param zxChange
     * @return
     */
    int insertZxDiscardChange(ZxChange zxChange);

    /**
     * 根据assetid修改资产的状态为报废
     *
     * @param management
     * @return
     */
    int updateZxDiscardAsset(ZxAssetManagement management);
}
