package com.hp.property.service.impl;

import com.hp.common.core.text.Convert;
import com.hp.common.utils.SnowFlake;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxAssetManagementMapper;
import com.hp.property.mapper.ZxChangeMapper;
import com.hp.property.mapper.ZxReturnMapper;
import com.hp.property.service.IZxReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资产退还Service业务层处理
 * 
 * @author hp
 * @date 2019-09-02
 */
@Service
public class ZxReturnServiceImpl implements IZxReturnService
{

    @Autowired
    private ZxAssetManagementMapper zxAssetManagementMapper;

    @Autowired
    private ZxChangeMapper zxChangeMapper;

    @Autowired
    private ZxReturnMapper zxReturnMapper;

    @Override
    public List<ZxAssetManagement> selectZxReturnList(ZxAssetManagement zxAssetManagement) {
        List<ZxAssetManagement> zxAssetManagements = zxReturnMapper.selectZxAssetManagementList(zxAssetManagement);
       /* ZxChange zxChange1 = new ZxChange();
        List<ZxChange> zxChanges = zxChangeMapper.selectZxChangeList(zxChange1);
        for(ZxChange zx:zxChanges) {
            if(zx.getAssetsId()!=null){
                for(ZxAssetManagement assets:zxAssetManagements){
                    List<ZxChange> zxChange = zxReturnMapper.selectZxChangeByAssetsId(assets.getId());
                    for(ZxChange change:zxChange){
                        String extend1=assets.getExtend1()+"/";
                        String extend2=assets.getExtend2().toString()+"/";
                        assets.setExtend1(extend1);
                        assets.setExtend2(extend2);
                    }


                }
            }else{
                return zxAssetManagements;
            }
        }
*/
     return zxAssetManagements;
    }

    @Override
    public ZxAssetManagement selectZxAssetManagementById(Long id) {
        return zxReturnMapper.selectZxAssetManagementById(id);
    }

    @Override
    public List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement) {
        return zxReturnMapper.selectZxAssetManagementsList(zxAssetManagement);
    }

    @Override
    public int insertManagementAndChange(ZxChange zxChange,Long[] Ids) {
        for(int a=0;a<Ids.length;a++){
           Long id= Ids[a];
           zxChange.setAssetsId(id);
            Long cid = SnowFlake.nextId();
            zxChange.setId(cid);
            zxChange.setChangeType(5);
            int i= zxReturnMapper.updateManagementStateById(id);
            if(i>0){
                zxReturnMapper.insertChange(zxChange);

            }else{
                return 0;
            }
        }
        return 1;

    }

   /* @Override
    public List<ZxAssetManagement> selectZxAssetManagementsById(String ids) {
        Long[] Ids = Convert.toLongArray(ids);
        return zxReturnMapper.selectZxAssetManagementsByIds(Ids);
    }*/

}
