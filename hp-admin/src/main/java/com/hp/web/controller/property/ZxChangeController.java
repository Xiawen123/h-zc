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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 资产变更Controller
 *
 * @author hp
 * @date 2019-09-02
 */
@Controller
@RequestMapping("/property/transfer")
public class ZxChangeController extends BaseController
{
    private String prefix = "property/transfer";

    @Autowired
    private IZxChangeService zxChangeService;

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;

    @RequiresPermissions("property:transfer:view")
    @GetMapping()
    public String change()
    {
        return prefix + "/transfer";
    }

    /**
     *@description:  转移查询
     *@author:  CaiYan
     *@createTime:  2019/9/3
     *@return:
     *@param:
     *
     */
    @RequiresPermissions("property:transfer:transferList")
    @PostMapping("/transferList")
    @ResponseBody
    public TableDataInfo transferList(ZxChange zxChange)
    {
        startPage();
        List<ZxChange> list = zxChangeService.selectZxChangeTransferList(zxChange);
        return getDataTable(list);
    }

    /**
     * 查看资产详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxChange zxChange = zxChangeService.selectZxChangeById(id);
        mmap.put("zxChange", zxChange);
        return prefix + "/detail";
    }


    /**
     * 新增资产变更
     */
    @GetMapping("/add")
    public String add()
    {

        return prefix+"/add";
    }

    /**
     * 新增保存转移信息
     */
    @RequiresPermissions("property:transfer:add")
    @Log(title = "资产信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxChange zxChange)
    {

        String ids = zxChange.getIds();
        int i1=0;
        if (ids!=null&&!ids.equals("")){
            String[] split = ids.split(",");
            //ZxChange zxChange1=new ZxChange();
            for (int i=0;i<split.length;i++){
                ZxAssetManagement zxone = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(split[i]));
                zxone.setLocation(Integer.parseInt(zxChange.getExtend3()));
                zxChange.setAssetsId(Long.parseLong(split[i]));
                zxChange.setId(SnowFlake.nextId());
                zxChange.setChangeType(2);
                zxChange.setExtend1(DateString.getString(new Date(),"yyyy-MM-dd HH:mm:ss"));
                zxChangeService.insertZxChange(zxChange);
                i1 = zxAssetManagementService.updateZxAssetManagement(zxone);
            }
            return toAjax(i1);
        }
        return toAjax(zxChangeService.insertZxChange(null));
    }

    @RequiresPermissions("property:department:propertyList")
    @PostMapping("/propertyList")
    @ResponseBody
    public TableDataInfo propertyList(ZxAssetManagement zxAssetManagement, HttpSession httpSession)
    {
        if (zxAssetManagement.getIds()!=null&&!zxAssetManagement.getIds().equals("")){
            List<ZxAssetManagement> list=new LinkedList<>();
            String s=zxAssetManagement.getIds();
            String[] split = s.split(",");
            String ids = (String)httpSession.getAttribute("ids");
            if (ids!=null&&!ids.equals("")){
                String s1 = ids + s;
                String[] split1 = s1.split(",");
                List<String> list1=new ArrayList<>();
                for (int i=0;i<split1.length;i++){
           //如果集合里面没有相同的元素才往里存
                    if(!list1.contains(split1[i])){
                        list1.add(split1[i]);
                    }
                }
                split = list1.toArray(new String[1]);
            }else {
                httpSession.setAttribute("ids",s);
            }
            for (int i=0;i<split.length;i++){
                if (split[i]!=null&&!split[i].equals("")) {
                    ZxAssetManagement ls = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(split[i]));
                    list.add(ls);
                }
            }
            return getDataTable(list);
        }else {
            List<ZxAssetManagement> list=new LinkedList<>();
            return getDataTable(list);
        }
    }

    @GetMapping("/parent")
    public String parent()
    {
        return prefix + "/addProperty";
    }
    /**
     * 资产详情
     */
    @RequiresPermissions("property:transfer:listooo")
    @PostMapping("/listooo")
    @ResponseBody
    public TableDataInfo listooo(ZxAssetManagement zxAssetManagement)
    {
        zxAssetManagement.setState(2);
        startPage();
        List<ZxAssetManagement> list = zxAssetManagementService.selectZxAssetManagementList(zxAssetManagement);
        return getDataTable(list);
    }

    /**
     * 添加资产信息
     */
    @GetMapping("/addProperty")
    public String addProperty(){
        return prefix+"/addProperty";
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

}
