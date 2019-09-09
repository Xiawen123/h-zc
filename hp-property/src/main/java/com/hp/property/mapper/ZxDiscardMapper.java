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
     * 新增变更记录（报废的）
     *
     * @param zxChange
     * @return
     */
    int insertZxDiscardChange(ZxChange zxChange);

    /**
     * 更改资产的状态为报废（4）
     *
     * @param assetsId
     * @return
     */
    int updateDiscardManagement(Long assetsId);


    /**
     * 查询所有资产信息
     *
     * @param assetsId
     * @return
     */
    ZxAssetManagement selectZxAssetManagementList(Long assetsId);
}
