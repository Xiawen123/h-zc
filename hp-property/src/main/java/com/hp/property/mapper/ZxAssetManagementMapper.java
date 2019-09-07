package com.hp.property.mapper;

import com.hp.common.base.BaseMapper;
import com.hp.property.domain.ZxAssetManagement;
import tk.mybatis.spring.annotation.MapperScan;

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
    public int insertZxAssetManagement(ZxAssetManagement insertZxChangeByAssert);

    /**
     * 查看数据表是否有值
     * @return
     */
    public int allZxAssetManagement();
    /**
     * 获取数据库中id最大值
     * @return
     */
    public ZxAssetManagement getMaxId();
    /**
     * 修改资产信息
     * 
     * @param zxAssetManagement 资产信息
     * @return 结果
     */
    public int updateZxAssetManagement(ZxAssetManagement zxAssetManagement);

    public int updateZxAssetManagementByAssetNum(ZxAssetManagement zxAssetManagement);

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



    /**
     * 查询所有资产为闲置状态的信息
     * @param zxAssetManagement
     * @return
     */
    List<ZxAssetManagement> selectAllStateOne(ZxAssetManagement zxAssetManagement);

    /**
     * 根据id修改资产状态为领用
     * @param ids
     * @return
     */
    Integer updateZxAssertManagement(String[] ids);

    /**
     * 查询变更表对应资产信息列表中领用的记录
     * @param zxAssetManagement 资产信息
     * @return 资产信息集合
     */
    List<ZxAssetManagement> selectZxAssetManagementListById(ZxAssetManagement zxAssetManagement);

}
