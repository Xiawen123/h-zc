package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.SnowFlake;
import com.hp.common.utils.poi.ExcelUtil;
import com.hp.framework.util.ShiroUtils;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxChangeService;
import com.hp.system.domain.SysDept;
import com.hp.system.domain.SysUser;
import com.hp.system.service.ISysDeptService;
import com.hp.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/property/management1")
public class ZxAssetManagementControllersXG extends BaseController
{
    private String prefix = "property/management1";

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;
    @Autowired
    private IZxChangeService zxChangeService;
    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private ISysUserService iSysUserService;

    @RequiresPermissions("property:management1:view")
    @GetMapping()
    public String management()
    {
        return prefix + "/management1";
    }

    /**
     * 查询资产信息列表
     */
    @RequiresPermissions("property:management1:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxAssetManagement zxAssetManagement) {

        startPage();
        List<ZxAssetManagement> list = zxAssetManagementService.selectZxAssetManagementList(zxAssetManagement);
        SysDept sysDept = new SysDept();
        List<SysDept> sysDepts = iSysDeptService.selectDeptList(sysDept);
        for (ZxAssetManagement zxAssetManagement1:list){
            for (SysDept sysDept1:sysDepts) {
                String a=zxAssetManagement1.getWarehousingCampus().toString();
                String b=sysDept1.getDeptId().toString();
                if (a.equals(b)) {
                    String c=sysDept1.getDeptName();
                    zxAssetManagement1.setExtend5(c);
                }
            }
        }

        return getDataTable(list);
    }

    /**
     * 导出资产信息列表
     */
    @RequiresPermissions("property:management1:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ZxAssetManagement zxAssetManagement)
    {
        List<ZxAssetManagement> list = zxAssetManagementService.selectZxAssetManagementList(zxAssetManagement);
        ExcelUtil<ZxAssetManagement> util = new ExcelUtil<ZxAssetManagement>(ZxAssetManagement.class);
        return util.exportExcel(list, "management1");
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
    @RequiresPermissions("property:management1:add")
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
        SysDept sysDept = new SysDept();
        List<SysDept> sysDepts = iSysDeptService.selectDeptList(sysDept);
        for (SysDept sysDept1:sysDepts) {
            if (zxAssetManagement.getCampus().equals(sysDept.getDeptId())) {
                zxAssetManagement.setCampus(Integer.parseInt("sysDept.getDeptName()"));
            }
        }

        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/edit";
    }

    /**
     * 修改保存资产信息
     */
    @RequiresPermissions("property:management1:edit")
    @Log(title = "资产信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ZxAssetManagement zxAssetManagement,ZxChange zxChange)
    {
        ZxAssetManagement zxAssetManagement1 = zxAssetManagementService.selectZxAssetManagementById(zxAssetManagement.getId());
        zxChange.setId(SnowFlake.nextId());
        zxChange.setAssetsId(new Long(zxAssetManagement1.getAssetNum()));
        zxChange.setChangeType(5);
        zxChange.setSubmitOne(zxAssetManagement1.getOperator());
        int a=Integer.parseInt(zxAssetManagement1.getExtend1());
        zxChange.setUseDepartment(a);
        zxChange.setUsers(zxAssetManagement1.getExtend2());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        zxChange.setExtend1(sdf.format(new Date()));
        String b=String.valueOf(zxAssetManagement1.getLocation());
        zxChange.setExtend3(b);
        SysUser sysUser = iSysUserService.selectUserByLoginName(ShiroUtils.getLoginName());
        Integer c=sysUser.getDeptId().intValue();
        zxChange.setSubmittedDepartment(c);
        zxChangeService.insertZxChange(zxChange);
        return toAjax(zxAssetManagementService.updateZxAssetManagement(zxAssetManagement));
    }
    /**
     * 删除资产信息
     */
    @RequiresPermissions("property:management1:remove")
    @Log(title = "资产信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(zxAssetManagementService.deleteZxAssetManagementByIds(ids));
    }
}

