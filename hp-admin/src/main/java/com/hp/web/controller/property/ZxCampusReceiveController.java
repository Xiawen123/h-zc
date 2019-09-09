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
import com.hp.property.service.impl.ZxChangeServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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

    // 跳转到校区领用主页面
    @RequiresPermissions("property:campusrecive:view")
    @GetMapping()
    public String management()
    {
        return prefix + "/campusrecive";
    }

    /**
     * 查询校区领用主页面即领用资产信息列表
     */
    @RequiresPermissions("property:campusrecive:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxAssetManagement zxAssetManagement)
    {
        System.out.println("领用时间查询: " + zxAssetManagement.getExtend4());
        // 查询变更表中所有变动类型为1即领用的所有记录
        ZxChange zxChange=new ZxChange();
        zxChange.setChangeType(1);
        List<ZxChange> zxChanges = zxChangeService.selectZxChangeList(zxChange);

        startPage();
        // 查询变更记录表中，变动类型为领用的记录对应资产表的记录
        List<ZxAssetManagement> list =new ArrayList<>();
        for (ZxChange z:zxChanges){

            Long id = z.getAssetsId();
            zxAssetManagement.setId(id);
            list = zxAssetManagementService.selectZxAssetManagementListById(zxAssetManagement);
        }

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
    public AjaxResult addSave(ZxAssetManagement zxAssetManagement)
    {
        // 修改资产状态：闲置变为领用
        // zxAssetManagementService.modifyZxAssertManagement(ids);

        System.out.println("zxAssetManagement:_____________" + zxAssetManagement.toString());

        // return toAjax(zxAssetManagementService.insertZxAssetManagement(zxAssetManagement));

        String ids = zxAssetManagement.getIds();
        int i1=0;
        if (ids!=null&&!ids.equals("")){
            String[] split = ids.split(",");
            ZxChange zxChange=new ZxChange();
            for (int i=0;i<split.length;i++){
                ZxAssetManagement zxone = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(split[i]));
                // 修改资产表的状态为领用
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

    @RequiresPermissions("property:campusrecive:list")
    @PostMapping("/list3")
    @ResponseBody
    public TableDataInfo list3(ZxAssetManagement zxAssetManagement, HttpServletRequest request)
    {
       /* String ids = zxAssetManagement.getIds();
        if(ids != null && !ids.equals("")){
            LinkedList<ZxAssetManagement> list = new LinkedList<>();
            HttpSession session = request.getSession();
            String ids1 = (String) session.getAttribute("ids");
            if(ids1 == null){
                session.setAttribute("ids",ids1);
            }
        }*/



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
            for (int i=0;i<split.length;i++){
                set.add(split[i]);
                System.out.println("set********************:" + set);
            }
            set.remove("0");
            set.remove(" ");
            for(Object id:set){
                String s1 = id.toString();
                System.out.println("s1*****************:" + s1);
                System.out.println("id********************:" + id);
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
