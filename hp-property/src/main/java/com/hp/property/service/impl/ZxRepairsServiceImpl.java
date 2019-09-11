package com.hp.property.service.impl;

import com.hp.common.utils.SnowFlake;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxAssetManagementMapper;
import com.hp.property.mapper.ZxChangeMapper;
import com.hp.property.mapper.ZxRepairsMapper;
import com.hp.property.mapper.ZxReturnMapper;
import com.hp.property.service.IZxRepairsService;
import com.hp.property.service.IZxReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 资产报修Service业务层处理
 * 
 * @author hp
 * @date 2019-09-02
 */
@Service
public class ZxRepairsServiceImpl implements IZxRepairsService
{

    @Autowired
    private ZxRepairsMapper zxRepairsMapper;

    @Override
    public List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement) {
       /* ZxChange zxChange = new ZxChange();
        Date repairTime = zxAssetManagement.getRepairTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String repairs = sdf.format(repairTime);
        String useDepartMent = zxAssetManagement.getExtend1();
        String users = zxAssetManagement.getExtend2();
        zxChange.setExtend1(repairs);
        zxChange.setUseDepartment(Integer.valueOf(useDepartMent));
        zxChange.setUsers(users);
        List<ZxChange> zxChanges = zxChangeMapper.selectZxChangeList(zxChange);*/
        return zxRepairsMapper.selectZxAssetManagementList(zxAssetManagement);
    }

    @Override
    public List<ZxChange> selectZxChangeByAssetId(Long id) {
        return zxRepairsMapper.selectZxChangeByAssetId(id);
    }
}
