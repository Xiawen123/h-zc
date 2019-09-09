package com.hp.property.mapper;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import tk.mybatis.spring.annotation.MapperScan;
import java.util.List;

@MapperScan
public interface ZxDiscardMapper {
    /**
     * 查询状态为报废的资产信息列表
     *
     * @param zxChange 资产信息
     * @return 资产信息集合
     */
    public List<ZxChange> selectZxChangeList(ZxChange zxChange);

    /**
     * 查询未报废的资产信息
     *
     * @param zxAssetManagement
     * @return
     */
    List<ZxAssetManagement> selectNoDiscardZxAssetList(ZxAssetManagement zxAssetManagement);

    /**
     * 查询所有资产信息
     *
     * @param assetsId
     * @return
     */
    ZxAssetManagement selectZxAssetManagementList(Long assetsId);


    /**
     * 根据变更记录id查询对应的详情
     *
     * @param id
     * @return
     */
    ZxChange selectDiscardChangeById(Long id);
}
