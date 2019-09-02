package com.hp.property.mapper;

import com.hp.property.domain.ZxAssetManagement;
import tk.mybatis.spring.annotation.MapperScan;
import com.hp.common.base.BaseMapper;
import java.util.List;

/**
 * 资产信息Mapper接口
 * 
 * @author hp
 * @date 2019-09-02
 */
@MapperScan
public interface ZxAssetManagementMapper extends BaseMapper<ZxAssetManagement>
{
    /**
     * 查询资产信息
     * 
     * @param id 资产信息ID
     * @return 资产信息
     */
    public ZxAssetManagement selectZxAssetManagementById(Long id);

    /**
     * 查询资产信息列表
     * 
     * @param zxAssetManagement 资产信息
     * @return 资产信息集合
     */
    public List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement);

    /**
     * 新增资产信息
     * 
     * @param zxAssetManagement 资产信息
     * @return 结果
     */
    public int insertZxAssetManagement(ZxAssetManagement zxAssetManagement);

    /**
     * 修改资产信息
     * 
     * @param zxAssetManagement 资产信息
     * @return 结果
     */
    public int updateZxAssetManagement(ZxAssetManagement zxAssetManagement);

    /**
     * 删除资产信息
     * 
     * @param id 资产信息ID
     * @return 结果
     */
    public int deleteZxAssetManagementById(Long id);

    /**
     * 批量删除资产信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteZxAssetManagementByIds(String[] ids);
}
