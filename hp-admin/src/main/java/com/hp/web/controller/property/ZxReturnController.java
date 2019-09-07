package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.core.text.Convert;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.DateString;
import com.hp.common.utils.FastJsonUtils;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxChangeService;
import com.hp.property.service.IZxReturnService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private IZxChangeService zxChangeService;
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
    public TableDataInfo list(ZxAssetManagement zxAssetManagement){
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
     * 新增退还信息
     */

    @RequiresPermissions("property:return:add")
    @Log(title = "退还登记", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxChange zxChange,String ids,HttpServletRequest request)
    {
        request.getSession().removeAttribute("s");
        Long[] Ids = Convert.toLongArray(ids);

      /*List<ZxAssetManagement> list= FastJsonUtils.getJsonToList(zxAssetsManagement, ZxAssetManagement.class);*/
        System.out.println(Ids);
        return toAjax(zxReturnService.insertManagementAndChange(zxChange,Ids));
    }
    /**
     * 新增资产退还页面
     */
/*    @RequiresPermissions("property:return:adds")
    @PostMapping("/addss")
    @ResponseBody
    public TableDataInfo addss(String ids){
        startPage();
        List<ZxAssetManagement> list = zxReturnService.selectZxAssetManagementsById(ids);
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
    @PostMapping("/listss")
    @ResponseBody
    public TableDataInfo listss(ZxAssetManagement zxAssetManagement)
    {
        startPage();
        List<ZxAssetManagement> list = zxReturnService.selectZxAssetManagementList(zxAssetManagement);
        return getDataTable(list);
    }


    @RequiresPermissions("property:return:list")
    @PostMapping("/lists")
    @ResponseBody
    public TableDataInfo lists(ZxAssetManagement zxAssetManagement, HttpServletRequest request)
    {
        if (zxAssetManagement.getIds()!=null&&!zxAssetManagement.getIds().equals("")){
            List<ZxAssetManagement> list=new LinkedList<>();
            String s=zxAssetManagement.getIds();
            HttpSession session=request.getSession();
            if(session.getAttribute("s")==null){
                session.setAttribute("s","0");
                session.setAttribute("s",session.getAttribute("s")+","+s);
            }else{
                session.setAttribute("s",session.getAttribute("s")+","+s);
            }
            String spl=session.getAttribute("s").toString();
            Set set = new HashSet();
            String[] split =spl.split(",");
            /*System.out.println(Arrays.toString(split));*/
           /* String[] split=s.split(",");*/
            for (int i=0;i<split.length;i++){
                set.add(split[i]);

                   /* ZxAssetManagement ls = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(set));
                    list.add(ls);*/
            }
            set.remove("0");
            set.remove(" ");
            for(Object id:set){
                String s1 = id.toString();
                if(!s1.equals("")){
                    ZxAssetManagement ls = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(s1));
                    list.add(ls);
                }

            }
            return getDataTable(list);
        }else {
            List<ZxAssetManagement> list=new LinkedList<>();
            return getDataTable(list);
        }
    }

}
