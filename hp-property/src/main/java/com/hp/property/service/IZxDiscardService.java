package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;

import java.util.List;

/**
 * 报废报废Service的接口  IZxDiscardService
 */
public interface IZxDiscardService {

    /**
     * 查询报废信息列表
     *
     * @param zxAssetManagement
     * @return
     */
    List<ZxAssetManagement> selectZxDiscardList(ZxAssetManagement zxAssetManagement);

    /**
     * 根据资产id查询详情
     * @param id
     * @return
     */
    ZxAssetManagement selectDiscardById(Long id);
}
