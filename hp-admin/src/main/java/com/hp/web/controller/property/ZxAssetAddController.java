package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.FastJsonUtils;
import com.hp.common.utils.SnowFlake;
import com.hp.common.utils.poi.ExcelUtil;
import com.hp.framework.util.ShiroUtils;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxChangeService;
import com.hp.system.domain.SysDept;
import com.hp.system.service.ISysDeptService;
import com.hp.web.controller.system.cloud.CloudStorageService;
import com.hp.web.controller.system.cloud.OSSFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资产信息Controller
 * 
 * @author hp
 * @date 2019-09-02
 */
@Controller
@RequestMapping("/property/productIn")
public class ZxAssetAddController extends BaseController
{
    private String prefix = "property/productIn";
    @Autowired
    private IZxAssetManagementService zxAssetManagementService;
    @Autowired
    private IZxChangeService zxChangeService;
    @Autowired
    private ISysDeptService iSysDeptService;
    @RequiresPermissions("property:productIn:view")
    @GetMapping()
    public String management()
    {
        return prefix + "/productIn";
    }
    /**
     * 查询资产信息列表
     */
    @RequiresPermissions("property:productIn:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxAssetManagement zxAssetManagement)
    {
        startPage();
        List<ZxAssetManagement> list = zxAssetManagementService.selectZxAssetManagementList(zxAssetManagement);
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
        return getDataTable(list);
    }

    /**
     * 导出资产信息列表
     */
    @RequiresPermissions("property:productIn:export")
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
    @RequiresPermissions("property:productIn:add")
    @Log(title = "资产信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxAssetManagement zxAssetManagement)
    {

            //添加雪花算法 表id
            zxAssetManagement.setId(SnowFlake.nextId());
            //添加资产编号
            String maxNum = zxAssetManagementService.getMaxNum(zxAssetManagement);
            String substring = maxNum.substring(8);
            int aNum = Integer.parseInt(substring);
            zxAssetManagement.setAssetNum("WHHP -"+zxAssetManagement.getType()+ String.format("%05d",(aNum+1)));
            //添加入库时间,获取系统现在时间
            zxAssetManagement.setStorageTime(new Date());
            //添加操作人,直接获取登录账户
            zxAssetManagement.setOperator(ShiroUtils.getLoginName());
            //添加状态,默认为闲置状态,字典键值为1
            zxAssetManagement.setState(1);
            //添加所在校区
            zxAssetManagement.setCampus(zxAssetManagement.getWarehousingCampus());
            //添加图片url
            List<Map<String, Object>> list = FastJsonUtils.getJsonToListMap(zxAssetManagement.getPicture().toString());
            list.get(0).get("img");
            for (int i = 0; i < list.size(); i++) {
                String a=list.get(i).get("img").toString();
                String suffix = a.substring(a.indexOf("/")+1,a.indexOf(";"));
                CloudStorageService storage = OSSFactory.build();
                String str = a.substring(a.indexOf(",") + 1, a.length());
                byte[] bytes = Base64.getDecoder().decode(str);
                String url = storage.uploadSuffix(bytes, "." + suffix);
                zxAssetManagement.setPicture(url);
            }
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
    @RequiresPermissions("property:productIn:edit")
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
    @RequiresPermissions("property:productIn:remove")
    @Log(title = "资产信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(zxAssetManagementService.deleteZxAssetManagementByIds(ids));
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
     * 变更表
     */

    @GetMapping("/oneChangee/{id}")
    public String oneChangee(@PathVariable("id") Integer id, ModelMap mmap)
    {

        mmap.put("id",id);
        return prefix + "/oneChange";
    }
    @RequiresPermissions("property:productIn:oneChange")
    @PostMapping("/oneChange")
    @ResponseBody
    public TableDataInfo oneChange(ZxChange zxChange){
        List<ZxChange> zxChanges = zxChangeService.getTimeChange(zxChange);
       // mmap.addAttribute("zxChanges",zxChanges);
        //mmap.addAttribute("id",id);
        return getDataTable(zxChanges);
    }
}
