package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.poi.ExcelUtil;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxChangeService;
import com.hp.property.service.IZxInfoService;
import com.hp.system.domain.SysDept;
import com.hp.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产信息Controller
 * 
 * @author hp
 * @date 2019-09-02
 */
@Controller
@RequestMapping("/property/management")
public class ZxAssetManagementController extends BaseController
{
    private String prefix = "property/management";

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;
    @Autowired
    private IZxChangeService zxChangeService;
    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private IZxInfoService zxInfoService;

    @RequiresPermissions("property:management:view")
    @GetMapping()
    public String management()
    {
        return prefix + "/management";
    }

    /**
     * 查询资产信息列表
     */
    @RequiresPermissions("property:management:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxAssetManagement zxAssetManagement)
    {
//        ZxChange zxChange=new ZxChange();
//       if (zxAssetManagement.getExtend1()!=null&&!zxAssetManagement.getExtend1().equals("")){
//           zxChange.setUseDepartment(Integer.parseInt(zxAssetManagement.getExtend1()));
//       }
//        if (zxAssetManagement.getExtend2()!=null&&!zxAssetManagement.getExtend2().equals("")){
//            zxChange.setUsers(zxAssetManagement.getExtend2());
//        }
     //   List<ZxChange> zxChangess=zxChangeService.selectZxChangeList(zxChange);

        startPage();
        List<ZxAssetManagement> list = zxInfoService.selectZxAssetManagementList(zxAssetManagement);
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
        }
       /* for(ZxAssetManagement assetManagement:list){
            Long id = assetManagement.getId();
            ZxChange zxChange = zxInfoService.selectZxChangeById(id);
            assetManagement.setExtend5(zxChange.getDeptName());
        }*/
//        for (ZxAssetManagement z:list){
//            zxChange.setAssetsId(new Long((long)z.getId()));
//            List<ZxChange> zxChanges = zxChangeService.selectZxChangeList(zxChange);
//           if (zxChanges!=null&&zxChanges.equals("")){
//               for (int i=0;i<zxChanges.size()-1;i++){
//                   for (int j=0;j<zxChanges.size()-1-i;j++){
//                       if (DateString.getDate("yy-MM-dd HH:mm:ss",zxChanges.get(j).getExtend1()).compareTo(DateString.getDate("yy-MM-dd HH:mm:ss",zxChanges.get(j+1).getExtend1()))<0){
//                           String temp=zxChanges.get(j).getExtend1();
//                           zxChanges.get(j).setExtend1(zxChanges.get(j+1).getExtend1());
//                           zxChanges.get(j+1).setExtend1(temp);
//                       }
//                   }
//               }
//               z.setExtend1(zxChanges.get(0).getUseDepartment()+"");
//               z.setExtend2(zxChanges.get(0).getUsers());
//           }
//        }

      /*  for (ZxAssetManagement z:list){
            if (z.getCampus()!=null) {
                 int z1=z.getCampus();
               SysDept s= iSysDeptService.selectDeptById(new Long((long)z1));
                z.setExtend5(s.getDeptName());
            }
        }*/
        return getDataTable(list);
    }

    /**
     * 导出资产信息列表
     */
    @RequiresPermissions("property:management:export")
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
    @GetMapping("/addition")
    public String add()
    {
        return prefix + "/addition";
    }

    /**
     * 新增保存资产信息
     */
    @RequiresPermissions("property:management:addition")
    @Log(title = "资产信息", businessType = BusinessType.INSERT)
    @PostMapping("/addition")
    @ResponseBody
    public AjaxResult addSave(ZxAssetManagement zxAssetManagement)
    {
        return toAjax(zxAssetManagementService.insertZxAssetManagement(zxAssetManagement));
    }

    /**
     * 修改资产信息1.2
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
    @RequiresPermissions("property:management:edit")
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
    @RequiresPermissions("property:management:remove")
    @Log(title = "资产信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(zxAssetManagementService.deleteZxAssetManagementByIds(ids));
    }
//    /**
//     * 详情
//     */
//    @RequiresPermissions("property:management:onee")
//    @GetMapping("/onee/{id}")
//    public String onee(@PathVariable("id") Integer id, ModelMap mmap)
//    {
//        mmap.addAttribute("id",id);
//        return prefix + "/one";
//    }


    /**
     * 详情
     */
    @GetMapping("/one/{id}")
    public String one(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxInfoService.selectZxAssetManagementById(id);
            if (zxAssetManagement.getWarehousingCampus()!=null) {
                int z1=zxAssetManagement.getWarehousingCampus();
                SysDept s= iSysDeptService.selectDeptById(new Long((long)z1));
                zxAssetManagement.setExtend5(s.getDeptName());
            }

        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/one";
    }
    /**
     * 变更表
     */

    @GetMapping("/oneChangee/{id}")
    public String oneChangee(@PathVariable("id") Long id, ModelMap mmap)
    {

        mmap.put("id",id);
        return prefix + "/oneChange";
    }
    @RequiresPermissions("property:management:oneChange")
    @PostMapping("/oneChange")
    @ResponseBody
    public TableDataInfo oneChange(ZxChange zxChange){
        List<ZxChange> zxChanges = zxChangeService.getTimeChange(zxChange);
       // mmap.addAttribute("zxChanges",zxChanges);
        //mmap.addAttribute("id",id);
        return getDataTable(zxChanges);
    }
}
