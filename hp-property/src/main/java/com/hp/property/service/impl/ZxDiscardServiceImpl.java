package com.hp.property.service.impl;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxAssetManagementMapper;
import com.hp.property.mapper.ZxChangeMapper;
import com.hp.property.mapper.ZxDiscardMapper;
import com.hp.property.service.IZxDiscardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报废Service的实现类  IZxDiscardService
 */
@Service
public class ZxDiscardServiceImpl implements IZxDiscardService {

    @Autowired
    private ZxAssetManagementMapper zxAssetManagementMapper;

    @Autowired
    private ZxChangeMapper zxChangeMapper;

    @Autowired
    private ZxDiscardMapper zxDiscardMapper;
    /**
     * 查询报废的变更记录信息列表
     *
     * @param zxChange
     * @return
     */
    @Override
    public List<ZxChange> selectZxDiscardList(ZxChange zxChange) {
        zxChange.setChangeType(4);
        System.out.println(zxChange.toString());
        List<ZxChange> changes = zxDiscardMapper.selectZxChangeList(zxChange);
        return changes;
    }

    /**
     * 根据资产id查询详情
     * @param id
     * @return
     */
    @Override
    public ZxChange selectDiscardChangeById(Long id) {
        return zxDiscardMapper.selectDiscardChangeById(id);
    }

    /**
     * 查询未报废的资产
     *
     * @param zxAssetManagement
     * @return
     */
    @Override
    public List<ZxAssetManagement> selectZxNoDiscardList(ZxAssetManagement zxAssetManagement) {
        return zxDiscardMapper.selectNoDiscardZxAssetList(zxAssetManagement);
    }
}
