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
import com.hp.property.service.IZxDiscardService;
import com.hp.system.domain.SysDept;
import com.hp.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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

    @Autowired
    private IZxChangeService zxChangeService;

    @Autowired
    private ISysDeptService iSysDeptService;
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
     * @param zxChange
     * @return
     */
    @RequiresPermissions("property:discard:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxChange zxChange)
    {
        startPage();
        //调用 zxDiscardService 的 selectZxDiscardList 方法查询报废信息列表
        List<ZxChange> list = zxDiscardService.selectZxDiscardList(zxChange);
        return getDataTable(list);
    }

    /**
     * 新增报废信息
     */
    @GetMapping("/add")
    public String add(HttpServletRequest request)
    {
        //清空session的值，防止有上次残留的值
        if (request.getSession().getAttribute("s")!=null){
            request.getSession().removeAttribute("s");
        }
        return prefix + "/add";
    }

    /**
     * 跳转到查询未报废的信息列表的页面(子窗口)
     */
    @GetMapping("/insert")
    public String insert()
    {
        return prefix + "/insert";
    }

    /**
     * 根据小窗口的id查未报废的资产数据
     *
     * （就是根据传过来的idss查询数据）
     *
     * @param zxAssetManagement
     * @return
     */
    @RequiresPermissions("property:discard:alist")
    @PostMapping("/alist")
    @ResponseBody
    public TableDataInfo alist(ZxAssetManagement zxAssetManagement,HttpServletRequest request)
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
            for (int i=0;i<split.length;i++){
                set.add(split[i]);
            }
            set.remove("0");
            set.remove(" ");
            for(Object id:set){
                String s1 = id.toString();
                /*System.out.println("我现在在加id呀"+id);*/
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


    /**
     * 新增保存资产变更
     */
    @RequiresPermissions("property:discard:add")
    @Log(title = "新增资产报废", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxAssetManagement zxAssetManagement,HttpServletRequest request)
   {
       String ids = request.getSession().getAttribute("s").toString();
       System.out.println("ids="+ids);
       int i1=0;
       Set set = new HashSet();
       ZxChange zxChange=new ZxChange();
       String[] split =ids.split(",");
       for (int i=0;i<split.length;i++){
           set.add(split[i]);
       }
       set.remove("0");
       set.remove(" ");
       System.out.println("set="+set);
       for(Object id:set){
           String s1 = id.toString();
           System.out.println("id="+id);
           if(!s1.equals("")){
               ZxAssetManagement zxone = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(s1));
               zxone.setState(4);
               System.out.println("zxone="+zxone);
               zxChange.setAssetsId(Long.parseLong(s1));
               long cid = SnowFlake.nextId();
               //变更
               zxChange.setId(cid);
               //变更类型
               zxChange.setChangeType(4);
               //提交部门
               zxChange.setSubmittedDepartment(zxAssetManagement.getDepartment());
               //提交人
               zxChange.setSubmitOne(zxAssetManagement.getExtend2());
               //变更时间
               zxChange.setExtend1(DateString.getString(new Date(),"yyyy-MM-dd HH:mm:ss"));
               System.out.println("zxchange="+zxChange);
               int i = zxChangeService.insertZxChange(zxChange);
               System.out.println("i="+i);
               i1 = zxAssetManagementService.updateZxAssetManagement(zxone);
               System.out.println("我的状态是"+i1);
           }
       }
       System.out.println("最终i1是"+i1);
       return toAjax(i1);
    }


    /**
     * 查询资产信息列表（查询未报废的资产）
     */
    @RequiresPermissions("property:discard:inserts")
    @PostMapping("/inserts")
    @ResponseBody
    public TableDataInfo insert(ZxAssetManagement zxAssetManagement)
    {
        zxAssetManagement.setState(4);
        startPage();
        List<ZxAssetManagement> list = zxDiscardService.selectZxNoDiscardList(zxAssetManagement);
        return getDataTable(list);
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
}
