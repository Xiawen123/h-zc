package com.hp.property.mapper;

import com.hp.property.domain.ZxAssetManagement;
import tk.mybatis.spring.annotation.MapperScan;
import java.util.List;

@MapperScan
public interface ZxDiscardMapper {
    /**
     * 查询状态为报废的资产信息列表
     *
     * @param zxAssetManagement 资产信息
     * @return 资产信息集合
     */
    public List<ZxAssetManagement> selectDiscardZxAssetList(ZxAssetManagement zxAssetManagement);
}
