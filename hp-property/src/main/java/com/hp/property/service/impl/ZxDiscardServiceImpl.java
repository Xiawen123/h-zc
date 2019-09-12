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

import javax.annotation.Resource;
import java.util.List;

/**
 * 报废Service的实现类  IZxDiscardService
 */
@Service
public class ZxDiscardServiceImpl implements IZxDiscardService {
    @Autowired
    private ZxDiscardMapper zxDiscardMapper;

    @Autowired
    private SysDeptMapper deptMapper;
    @Resource
    private ZxAssetManagementMapper zxAssetManagementMapper;
    /**
     * 查询报废的变更记录信息列表
     *
     * @param zxChange
     * @return
     */
    @Override
    public List<ZxChange> selectZxDiscardList(ZxChange zxChange/*,String campus*/) {

        zxChange.setChangeType(4);
        /*zxChange.setExtend5(campus);*/
        List<ZxChange> changes = zxDiscardMapper.selectZxChangeList(zxChange);
        for (ZxChange change:changes){
            if(change.getAssetsId()!=null){
                ZxAssetManagement zxAssetManagement = zxAssetManagementMapper.selectZxAssetManagementById(change.getAssetsId());
                System.out.println(zxAssetManagement.getWarehousingCampus());
                if (zxAssetManagement.getWarehousingCampus()!=null) {
                    int z1=zxAssetManagement.getWarehousingCampus();
                    SysDept s= deptMapper.selectDeptById(new Long((long)z1));
                    change.setExtend5(s.getDeptName());
                }
            }
        }
        System.out.println(changes);
        return changes;
    }

    /**
     * 根据  资产id查询详情
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
