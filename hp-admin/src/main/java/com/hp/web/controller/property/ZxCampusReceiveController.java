package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.impl.ZxChangeServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Liushun
 * @date Created in 2019/9/2 18:00
 * @description
 */

@Controller
@RequestMapping("/property/campusrecive")
public class ZxCampusReceiveController extends BaseController {

    private String prefix = "property/campusrecive";

    @Autowired
    private ZxChangeServiceImpl zxChangeService;

    @Autowired(required = false)
    private IZxAssetManagementService zxAssetManagementService;

    @RequiresPermissions("property:campusrecive:view")
    @GetMapping()
    public String management()
    {
        return prefix + "/campusrecive";
    }

    /**
     * 查询资产信息列表
     */
    @RequiresPermissions("property:campusrecive:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxChange zxChange)
    {
        // 变动类型为1代表领用
        zxChange.setChangeType(1);

       /* List<ZxChange> zxChanges = zxChangeService.selectZxChangeList(zxChange);
        List<ZxAssetManagement> list=new LinkedList<>();
        for (ZxChange l:zxChanges){
            ZxAssetManagement zxAssetManagement = zxAssetManagementService.selectZxAssetManagementById(l.getAssetsId());
            zxAssetManagement.setZxChange(l);
            list.add(zxAssetManagement);
        }
*/
        // 查询变动类型为1,即领用的所有信息
        startPage();
        List<ZxAssetManagement> list = zxChangeService.findAllStateOne(zxChange);
        return getDataTable(list);
    }

    /**
     * 新增资产信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }


   // 新增保存资产信息

    @RequiresPermissions("property:campusrecive:add")
    @Log(title = "资产信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(String ids, ZxAssetManagement zxAssetManagement)
    {
        // 修改资产状态：闲置变为领用
        zxAssetManagementService.modifyZxAssertManagement(ids);

        System.out.println("ids:_____________" + ids);
        System.out.println("zxAssetManagement:_____________" + zxAssetManagement.toString());

        return toAjax(zxAssetManagementService.insertZxAssetManagement(zxAssetManagement));
    }

    // 修改资产信息
    @GetMapping("/detail/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxAssetManagementService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/detail";
    }

    // 跳转到闲置资产信息页面
    @GetMapping("/showIdle")
    public String showIdle()
    {
        return prefix + "/showIdle";
    }

    // 查询闲置资产信息列表
    @RequiresPermissions("property:campusrecive:list")
    @PostMapping("/list1")
    @ResponseBody
    public TableDataInfo list1(ZxAssetManagement zxAssetManagement)
    {
        // 资产状态1代表闲置
        zxAssetManagement.setState(1);

        // 带条件查询所有
        startPage();
        List<ZxAssetManagement> list = zxAssetManagementService.findAllStateOne(zxAssetManagement);
        return getDataTable(list);
    }

    // 查询所有选中的闲置资产信息
    @RequiresPermissions("property:campusrecive:list")
    @PostMapping("/list2")
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
