package com.hp.property.service.impl;

import java.util.List;

import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxChangeMapper;
import com.hp.property.service.IZxChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hp.common.core.text.Convert;

/**
 * 资产变更Service业务层处理
 * 
 * @author hp
 * @date 2019-09-02
 */
@Service
public class ZxChangeServiceImpl implements IZxChangeService
{
    @Autowired
    private ZxChangeMapper zxChangeMapper;

    /**
     * 查询资产变更
     * 
     * @param id 资产变更ID
     * @return 资产变更
     */
    @Override
    public ZxChange selectZxChangeById(Long id)
    {
        return zxChangeMapper.selectZxChangeById(id);
    }

    /**
     * 查询资产变更列表
     * 
     * @param zxChange 资产变更
     * @return 资产变更
     */
    @Override
    public List<ZxChange> selectZxChangeList(ZxChange zxChange)
    {
        return zxChangeMapper.selectZxChangeList(zxChange);
    }

    /**
     * 新增资产变更
     * 
     * @param zxChange 资产变更
     * @return 结果
     */
    @Override
    public int insertZxChange(ZxChange zxChange)
    {
        return zxChangeMapper.insertZxChange(zxChange);
    }

    /**
     * 修改资产变更
     * 
     * @param zxChange 资产变更
     * @return 结果
     */
    @Override
    public int updateZxChange(ZxChange zxChange)
    {
        return zxChangeMapper.updateZxChange(zxChange);
    }

    /**
     * 删除资产变更对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteZxChangeByIds(String ids)
    {
        return zxChangeMapper.deleteZxChangeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除资产变更信息
     * 
     * @param id 资产变更ID
     * @return 结果
     */
    public int deleteZxChangeById(Long id)
    {
        return zxChangeMapper.deleteZxChangeById(id);
    }
}
