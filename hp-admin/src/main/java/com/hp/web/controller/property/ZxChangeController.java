package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.poi.ExcelUtil;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxChangeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产变更Controller
 *
 * @author hp
 * @date 2019-09-02
 */
@Controller
@RequestMapping("/property/transfer")
public class ZxChangeController extends BaseController
{
    private String prefix = "property/transfer";

    @Autowired
    private IZxChangeService zxChangeService;

    @RequiresPermissions("property:transfer:view")
    @GetMapping()
    public String change()
    {
        return prefix + "/transfer";
    }



//    @RequiresPermissions("property:transfer:transfer")
//    @GetMapping("/transfer")
//    @ResponseBody
//    public TableDataInfo transfer(ZxChange zxChange)
//    {
//        startPage();
//        List<ZxChange> list = zxChangeService.selectZxChangeList(zxChange);
//        return getDataTable(list);
//    }

    /**
     * 新增资产变更
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存资产变更
     */
    @RequiresPermissions("property:transfer:add")
    @Log(title = "资产变更", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxChange zxChange)
    {
        return toAjax(zxChangeService.insertZxChange(zxChange));
    }

//    /**
//     * 修改资产变更
//     */
//    @GetMapping("/edit/{id}")
//    public String edit(@PathVariable("id") Long id, ModelMap mmap)
//    {
//        ZxChange zxChange = zxChangeService.selectZxChangeById(id);
//        mmap.put("zxChange", zxChange);
//        return prefix + "/edit";
//    }
//
//    /**
//     * 修改保存资产变更
//     */
//    @RequiresPermissions("property:transfer:edit")
//    @Log(title = "资产变更", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(ZxChange zxChange)
//    {
//        return toAjax(zxChangeService.updateZxChange(zxChange));
//    }
//
//    /**
//     * 删除资产变更
//     */
//    @RequiresPermissions("property:transfer:remove")
//    @Log(title = "资产变更", businessType = BusinessType.DELETE)
//    @PostMapping( "/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids)
//    {
//        return toAjax(zxChangeService.deleteZxChangeByIds(ids));
//    }
    /**
     *@description:  转移查询
     *@author:  CaiYan
     *@createTime:  2019/9/3
     *@return:
     *@param:
     *
     */
    @RequiresPermissions("property:transfer:transferList")
    @PostMapping("/transferList")
    @ResponseBody
    public TableDataInfo transferList(ZxChange zxChange)
    {
        startPage();
        List<ZxChange> list = zxChangeService.selectZxChangeTransferList(zxChange);
        return getDataTable(list);
    }

}
