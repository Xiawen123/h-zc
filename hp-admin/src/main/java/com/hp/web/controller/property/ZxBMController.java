package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.SnowFlake;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxChangeService;
import com.hp.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/property/department")
public class ZxBMController extends BaseController {
    private String prefix="/property/department";
    @Autowired(required = false)
    private IZxAssetManagementService zxAssetManagementService;
    @Autowired(required = false)
    private IZxChangeService zxChangeService;
    @Autowired(required = false)
    private ISysDeptService iSysDeptService;
    @RequiresPermissions("property:department:view")
    @GetMapping()
    public String department(){
        return prefix+"/campusrecive";
    }
/**
 * 部门修改首页
 */

    /**
     * 查询资产信息列表
     */
    @RequiresPermissions("property:department:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxAssetManagement zxAssetManagement)
    {
        zxAssetManagement.setState(2);

        startPage();
        List<ZxAssetManagement> list = zxAssetManagementService.selectZxAssetManagementList(zxAssetManagement);
        return getDataTable(list);
    }

    /**
     * 新增资产信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/addone";
    }

    /**
     * 新增保存资产信息
     */
    @RequiresPermissions("property:department:add")
    @Log(title = "资产信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxAssetManagement zxAssetManagement)
    {
        Long id = SnowFlake.nextId();
        zxAssetManagement.setId(id);
        System.out.println("zxAssetManagement:________________________" + zxAssetManagement.toString());

        return toAjax(zxAssetManagementService.insertZxAssetManagement(zxAssetManagement));
    }

    /**
     * 修改资产信息
     */
    @GetMapping("/detail/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxAssetManagementService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/detail";
    }
    /**
     * 添加资产信息
     */
    @GetMapping("/zcdd")
    public String zcdd(){
        return prefix+"/zcdd";
    }

    @PostMapping("/xuan")
    @ResponseBody
    public AjaxResult xuan(String ids){
        try {
            return toAjax(0);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 打开小窗口页面
     *
     * @return
     */
    @GetMapping("/parent")
    public String parent()
    {
        return prefix + "/zcdd";
    }
    /**
     * 资产详情
     */
    @RequiresPermissions("property:department:listooo")
    @PostMapping("/listooo")
    @ResponseBody
    public TableDataInfo listooo(ZxAssetManagement zxAssetManagement)
    {
            zxAssetManagement.setState(1);
            startPage();
            List<ZxAssetManagement> list = zxAssetManagementService.selectZxAssetManagementList(zxAssetManagement);
            return getDataTable(list);
    }

    @RequiresPermissions("property:department:listsan")
    @PostMapping("/listsan")
    @ResponseBody
    public TableDataInfo listsan(ZxAssetManagement zxAssetManagement)
    {
       if (zxAssetManagement.getIds()!=null&&!zxAssetManagement.getIds().equals("")){
           List<ZxAssetManagement> list=new LinkedList<>();
           String s=zxAssetManagement.getIds();
           String[] split = s.split(",");
           for (int i=0;i<split.length;i++){
               ZxAssetManagement ls = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(split[i]));
               list.add(ls);
           }
           return getDataTable(list);
       }else {
           List<ZxAssetManagement> list=new LinkedList<>();
           return getDataTable(list);
       }
    }
    }

