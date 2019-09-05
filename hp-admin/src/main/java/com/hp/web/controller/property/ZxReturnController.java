package com.hp.web.controller.property;

import com.hp.common.core.controller.BaseController;
import com.hp.common.core.page.TableDataInfo;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxReturnService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 资产退还控制层
 */
@Controller
@RequestMapping("/property/return")
public class ZxReturnController extends BaseController {
    private String prefix = "property/return";
    @Autowired
    private IZxReturnService zxReturnService;
    @Autowired
    private IZxAssetManagementService zxAssetManagementService;
    /**
     * 页面展示
     * @return
     */
    @RequiresPermissions("property:return:view")
    @GetMapping()
    public String returns()
    {


        return prefix + "/return";
    }

    /**
     * 数据展示
     * @param zxAssetManagement
     * @return
     */

    @RequiresPermissions("property:return:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxAssetManagement zxAssetManagement,String returnTime)
    {
        startPage();

        List<ZxAssetManagement> list = zxReturnService.selectZxReturnList(zxAssetManagement);
        return getDataTable(list);
    }

    /**
     * 新增页面展示
     * @return
     */

    @RequiresPermissions("property:return:add")
    @GetMapping("/add")
    public String add()
    {

        return prefix + "/add";
    }


    /**
     * 新增资产退还页面
     */
    /*@RequiresPermissions("property:return:list")
    @PostMapping("/adds")
    @ResponseBody
    public TableDataInfo adds(ZxAssetManagement zxAssetManagement)
    {
        startPage();
        List<ZxAssetManagement> list = zxReturnService.selectZxReturnList(zxAssetManagement);
        return getDataTable(list);
    }*/

    @RequiresPermissions("property:return:adds")
    @GetMapping("/adds")
    public String adds()
    {

        return prefix + "/adds";
    }


    /**
     * 新增资产退还页面
     */
    @GetMapping("/select/{id}")
    public String select(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxReturnService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/select";
    }


    @RequiresPermissions("property:return:list")
    @PostMapping("/lists")
    @ResponseBody
    public TableDataInfo lists(ZxAssetManagement zxAssetManagement,String returnTime)
    {
        startPage();
        List<ZxAssetManagement> list = zxReturnService.selectZxReturnList(zxAssetManagement);
        return getDataTable(list);
    }

}
