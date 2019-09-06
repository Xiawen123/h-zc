package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.DateString;
import com.hp.common.utils.SnowFlake;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxChangeService;
import com.hp.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
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
        ZxChange zxChange=new ZxChange();
        zxChange.setChangeType(1);
        List<ZxChange> zxChanges = zxChangeService.selectZxChangeList(zxChange);
        startPage();
        List<ZxAssetManagement> list =new ArrayList<>();
        for (ZxChange z:zxChanges){
            Long id = z.getAssetsId();
            ZxAssetManagement zxAssetManagement1 = zxAssetManagementService.selectZxAssetManagementById(id);
            list.add(zxAssetManagement1);
        }
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
        String ids = zxAssetManagement.getIds();
        int i1=0;
        if (ids!=null&&!ids.equals("")){
            String[] split = ids.split(",");
            ZxChange zxChange=new ZxChange();
            for (int i=0;i<split.length;i++){
                ZxAssetManagement zxone = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(split[i]));
                zxone.setState(2);
                zxChange.setAssetsId(Long.parseLong(split[i]));
                long l = SnowFlake.nextId();
                zxChange.setId(l);
                zxChange.setChangeType(1);
                zxChange.setUseDepartment(zxAssetManagement.getDepartment());
                zxChange.setUsers(zxAssetManagement.getExtend2());
                zxChange.setExtend1(DateString.getString(new Date(),"yyyy-MM-dd HH:mm:ss"));
                zxChangeService.insertZxChange(zxChange);
                i1 = zxAssetManagementService.updateZxAssetManagement(zxone);
            }
            return toAjax(i1);
        }
        return toAjax(zxChangeService.insertZxChange(null));
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

