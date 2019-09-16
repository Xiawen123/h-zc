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
    public List<ZxChange> selectZxDiscardList(ZxChange zxChange) {
        zxChange.setChangeType(4);
        //zxChange.setExtend5(campus);
        System.out.println(zxChange.toString());
        List<ZxChange> changes = zxDiscardMapper.selectZxChangeList(zxChange);
        SysDept sysDept = new SysDept();
        List<SysDept> sysDepts = sysDeptMapper.selectDeptList(sysDept);
        //循环存入校区名，存入备用字段5
        for (ZxChange change:changes){
            for (SysDept sysDept1:sysDepts) {
                if (zxAssetManagement1.getWarehousingCampus()!=null){
                    String a=zxAssetManagement1.getWarehousingCampus().toString();
                    String b=sysDept1.getDeptId().toString();
                    if (a.equals(b)) {
                        String c=sysDept1.getDeptName();
                        zxAssetManagement1.setExtend5(c);}
                }
            }
        }
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
