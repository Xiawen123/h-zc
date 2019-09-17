package com.hp.property.service.impl;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxAssetManagementMapper;
import com.hp.property.mapper.ZxChangeMapper;
import com.hp.property.mapper.ZxDiscardMapper;
import com.hp.property.service.IZxDiscardService;
import com.hp.system.domain.SysDept;
import com.hp.system.mapper.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报废Service的实现类  IZxDiscardService
 */
@Service
public class ZxDiscardServiceImpl implements IZxDiscardService {
    @Autowired
    private ZxDiscardMapper zxDiscardMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;
    /**
     * 查询报废的变更记录信息列表
     *
     * @param zxChange
     * @return
     */
    @Override
    public List<ZxChange> selectDiscardList(ZxChange zxChange) {
        zxChange.setChangeType(4);
        System.out.println(zxChange.toString());
        List<ZxChange> changes = zxDiscardMapper.selectChangeList(zxChange);
        return changes;
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

    @Override
    public ZxAssetManagement selectZxAssetManagementById(Long id) {
        return zxDiscardMapper.selectZxAssetManagementById(id);
    }
}
