package com.hp.web.controller.property;

import com.hp.common.core.controller.BaseController;
import com.hp.common.core.page.TableDataInfo;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxDiscardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产报废Controller
 *
 * @author 小鱼儿
 * @date 2019-09-02
 */
@Controller
@RequestMapping("/property/discard")
public class ZxDiscardController extends BaseController {
    private String prefix = "property/discard";

    @Autowired
    private IZxDiscardService zxDiscardService;
@Autowired
    private IZxAssetManagementService zxAssetManagementService;
    /**
     * 跳转到 discard.html
     *
     * @return
     */
    @RequiresPermissions("property:discard:view")
    @GetMapping()
    public String change()
    {
        return prefix + "/discard";
    }

    /**
     * 查询报废信息列表
     *
     * @param zxAssetManagement
     * @return
     */
    @RequiresPermissions("property:discard:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxAssetManagement zxAssetManagement)
    {
        startPage();
        //调用 zxDiscardService 的 selectZxDiscardList 方法查询报废信息列表
        List<ZxAssetManagement> list = zxDiscardService.selectZxDiscardList(zxAssetManagement);
        return getDataTable(list);
    }

    /**
     * 新增报废信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }



    /**
     * 详情
     */
    @GetMapping("/one/{id}")
    public String one(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxAssetManagementService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/one";
    }
    /**
     * 修改资产信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        ZxAssetManagement zxAssetManagement = zxAssetManagementService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/edit";
    }
}
