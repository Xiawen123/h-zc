package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.SnowFlake;
import com.hp.framework.util.ShiroUtils;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxChangeService;
import com.hp.property.service.IZxRepairsService;
import com.hp.property.service.IZxReturnService;
import com.hp.system.domain.SysDept;
import com.hp.system.domain.SysUser;
import com.hp.system.service.ISysDeptService;
import com.hp.system.service.ISysDictDataService;
import com.hp.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资产报修控制层
 */
@Controller
@RequestMapping("/property/repairs")
public class ZxRepairsController extends BaseController {
    private String prefix = "property/repairs";
    @Autowired
    private IZxRepairsService zxRepairsService;
    @Autowired
    private IZxAssetManagementService zxAssetManagementService;
    @Autowired
    private IZxChangeService zxChangeService;
    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private ISysDictDataService iSysDictDataService;


    @Autowired
    private ISysUserService iSysUserService;
    /**
     * 页面展示
     * @return
     */
    @RequiresPermissions("property:repairs:view")
    @GetMapping()
    public String repairs()
    {


        return prefix + "/repairs";
    }

    /**
     * 数据展示
     * @param zxAssetManagement
     * @return
     */

    @RequiresPermissions("property:repairs:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxAssetManagement zxAssetManagement){
        startPage();
        List<ZxAssetManagement> list = zxRepairsService.selectZxAssetManagementList(zxAssetManagement);
        SysDept sysDept = new SysDept();
        List<SysDept> sysDepts = iSysDeptService.selectDeptList(sysDept);
        //循环存入校区名，存入备用字段5
        for (ZxAssetManagement zxAssetManagement1:list){
            for (SysDept sysDept1:sysDepts) {
                if (zxAssetManagement1.getWarehousingCampus()!=null){
                    String a=zxAssetManagement1.getWarehousingCampus().toString();
                    String b=sysDept1.getDeptId().toString();
                    if (a.equals(b)) {
                        String c=sysDept1.getDeptName();
                        zxAssetManagement1.setExtend5(c);}
                }
            }
            Long id = zxAssetManagement1.getId();
            List<ZxChange> zxChanges= zxRepairsService.selectZxChangeByAssetId(id);
            for(ZxChange zxChange:zxChanges){
               if(zxChange.getExtend1()!=null){
                  zxAssetManagement1.setExtend4(zxChange.getExtend1());
               }
               if(zxChange.getUseDepartment()!=null){
                   zxAssetManagement1.setExtend1(zxChange.getUseDepartment().toString());

               }
               if(zxChange.getUsers()!=null){
                   zxAssetManagement1.setExtend2(zxChange.getUsers());
               }

            }
        }

        return getDataTable(list);
    }

    /**
     * 资产报修详情页
     */
    @GetMapping("/select/{id}")
    public String select(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxRepairsService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/select";
    }

    /**
     * 更改状态
     */
    @RequiresPermissions("property:repairs:remove")
    @Log(title = "更改状态", businessType = BusinessType.DELETE)
    @PostMapping("/xgstate")
    @ResponseBody
    public AjaxResult remove(Long id) {
        ZxAssetManagement management = zxRepairsService.selectZxAssetManagementById(id);
        String extend3 = management.getExtend3();
        int i;
        if(extend3.equals("1")){
            management.setExtend3("0");
           i= zxRepairsService.updateExtend3(management);
        }else if(extend3.equals("0")){
            management.setExtend3("1");
           i=zxRepairsService.updateExtend3(management);
        }else{
            i=0;
        }

        return toAjax(i);
    }


    /**
     * 新增页面展示
     * @return
     */

    @RequiresPermissions("property:repairs:add")
    @GetMapping("/add")
    public String add()
    {

        return prefix + "/add";
    }


}
