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
     * 查询报废信息列表
     *
     * @param zxAssetManagement
     * @return
     */
    @Override
    public List<ZxAssetManagement> selectZxDiscardList(ZxAssetManagement zxAssetManagement) {
        List<ZxAssetManagement> managements = zxDiscardMapper.selectDiscardZxAssetList(zxAssetManagement);
        List<ZxChange> changes = zxChangeMapper.selectZxChangeList(new ZxChange());
        for (ZxChange change:changes){
            if (change.getAssetsId()!=null){
                for (ZxAssetManagement management:managements){
                    if (change.getSubmittedDepartment()!=null){
                        management.setExtend1(Integer.toString(change.getSubmittedDepartment()));
                    }
                    if (change.getSubmitOne()!=null){
                        management.setExtend2(change.getSubmitOne());
                    }
                }
            }
        }
        return managements;
    }

    /**
     * 根据资产id查询详情
     * @param id
     * @return
     */
    @Override
    public ZxAssetManagement selectDiscardById(Long id) {
        return zxAssetManagementMapper.selectZxAssetManagementById(id);
    }


}
