package com.hp.property.service.impl;

import com.hp.common.core.text.Convert;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.mapper.ZxAssetManagementMapper;
import com.hp.property.service.IZxAssetManagementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资产信息Service业务层处理
 *
 * @author hp
 * @date 2019-09-02
 */
@Service
public class ZxAssetManagementServiceImpl implements IZxAssetManagementService {

    @Resource
    private ZxAssetManagementMapper zxAssetManagementMapper;

    /**
     * 查询资产信息
     *
     * @param id 资产信息ID
     * @return 资产信息
     */
    @Override
    public ZxAssetManagement selectZxAssetManagementById(Long id) {
        return zxAssetManagementMapper.selectZxAssetManagementById(id);
    }

    /**
     * 查询资产信息列表
     *
     * @param zxAssetManagement 资产信息
     * @return 资产信息
     */
    @Override
    public List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.selectZxAssetManagementList(zxAssetManagement);
    }

    /**
     * 新增资产信息
     *
     * @param zxAssetManagement 资产信息
     * @return 结果
     */
    @Override
    public int insertZxAssetManagement(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.insertZxAssetManagement(zxAssetManagement);
    }



    /**
     * 修改资产信息
     *
     * @param zxAssetManagement 资产信息
     * @return 结果
     */
    @Override
    public int updateZxAssetManagement(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.updateZxAssetManagement(zxAssetManagement);
    }

    /**
     * 删除资产信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteZxAssetManagementByIds(String ids) {
        return zxAssetManagementMapper.deleteZxAssetManagementByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除资产信息信息
     *
     * @param id 资产信息ID
     * @return 结果
     */
    public int deleteZxAssetManagementById(Long id) {
        return zxAssetManagementMapper.deleteZxAssetManagementById(id);
    }



    @Override
    public List<ZxAssetManagement> findAllStateOne(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.selectAllStateOne(zxAssetManagement);
    }

    @Override
    public List<ZxAssetManagement> seleAll(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.selectAAAStateTwo(zxAssetManagement);
    }

    @Override
    public Integer modifyZxAssertManagement(String ids) {
        return zxAssetManagementMapper.updateZxAssertManagement(Convert.toStrArray(ids));
    }

    // 查询变更表对应资产信息列表中领用的记录
    @Override
    public List<ZxAssetManagement> selectZxAssetManagementListById(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.selectZxAssetManagementListById(zxAssetManagement);
    }

    @Override
    public String getMaxNum(ZxAssetManagement zxAssetManagement) {

        return zxAssetManagementMapper.getMaxNum(zxAssetManagement);
    }
}
