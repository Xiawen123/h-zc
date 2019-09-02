package com.hp.web.controller.property;

import java.util.List;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.service.IZxAssetManagementService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hp.common.annotation.Log;
import com.hp.common.enums.BusinessType;

import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.utils.poi.ExcelUtil;
import com.hp.common.core.page.TableDataInfo;

/**
 * 资产信息Controller
 * 
 * @author hp
 * @date 2019-09-02
 */
@Controller
@RequestMapping("/system/management")
public class ZxAssetManagementController extends BaseController
{
    private String prefix = "system/management";

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;

    @RequiresPermissions("property:management:view")
    @GetMapping()
    public String management()
    {
        return prefix + "/management";
    }

    /**
     * 查询资产信息列表
     */
    @RequiresPermissions("system:management:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxAssetManagement zxAssetManagement)
    {
        startPage();
        List<ZxAssetManagement> list = zxAssetManagementService.selectZxAssetManagementList(zxAssetManagement);
        return getDataTable(list);
    }

    /**
     * 导出资产信息列表
     */
    @RequiresPermissions("system:management:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ZxAssetManagement zxAssetManagement)
    {
        List<ZxAssetManagement> list = zxAssetManagementService.selectZxAssetManagementList(zxAssetManagement);
        ExcelUtil<ZxAssetManagement> util = new ExcelUtil<ZxAssetManagement>(ZxAssetManagement.class);
        return util.exportExcel(list, "management");
    }

    /**
     * 新增资产信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存资产信息
     */
    @RequiresPermissions("system:management:add")
    @Log(title = "资产信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxAssetManagement zxAssetManagement)
    {
        return toAjax(zxAssetManagementService.insertZxAssetManagement(zxAssetManagement));
    }

    /**
     * 修改资产信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxAssetManagementService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/edit";
    }

    /**
     * 修改保存资产信息
     */
    @RequiresPermissions("system:management:edit")
    @Log(title = "资产信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ZxAssetManagement zxAssetManagement)
    {
        return toAjax(zxAssetManagementService.updateZxAssetManagement(zxAssetManagement));
    }

    /**
     * 删除资产信息
     */
    @RequiresPermissions("system:management:remove")
    @Log(title = "资产信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(zxAssetManagementService.deleteZxAssetManagementByIds(ids));
    }
}
