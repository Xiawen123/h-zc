package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.core.text.Convert;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.DateString;
import com.hp.common.utils.FastJsonUtils;
import com.hp.common.utils.SnowFlake;
import com.hp.framework.util.ShiroUtils;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxChangeService;
import com.hp.property.service.IZxReturnService;
import com.hp.system.domain.SysDept;
import com.hp.system.domain.SysDictData;
import com.hp.system.domain.SysUser;
import com.hp.system.service.ISysDeptService;
import com.hp.system.service.ISysDictDataService;
import com.hp.system.service.ISysUserService;
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
    public TableDataInfo list(ZxChange zxChange){
        startPage();
        List<ZxChange> list = zxReturnService.selectZxReturnList(zxChange);
        return getDataTable(list);
    }

    /**
     * 新增页面展示
     * @return
     */

    @RequiresPermissions("property:return:add")
    @GetMapping("/add")
    public String add(HttpServletRequest request)
    {
        if(request.getSession().getAttribute("s")!=null){
            request.getSession().removeAttribute("s");
        }

        return prefix + "/add";
    }

    /**
     * 新增退还信息
     */

    @RequiresPermissions("property:return:add")
    @Log(title = "退还登记", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxChange zxChange,ZxAssetManagement zxAssetManagement,HttpServletRequest request)
    {

        String ids =  request.getSession().getAttribute("s").toString();
        Date returnTime = zxAssetManagement.getReturnTime();
        int i1=0;
        if (ids!=null&&!ids.equals("")){
            Set set = new HashSet();
            String[] split = ids.split(",");
            for (int i=0;i<split.length;i++){
                set.add(split[i]);
            }
            set.remove("0");
            set.remove(" ");
            for(Object id:set) {
                String s1 = id.toString();
                if (!s1.equals("")) {
                    ZxAssetManagement zxone = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(s1));
                    zxone.setState(1);
                    zxChange.setAssetsId(Long.parseLong(s1));
                    long l = SnowFlake.nextId();
                    zxChange.setId(l);
                    zxChange.setChangeType(5);
                    zxChange.setUseDepartment(zxChange.getSubmittedDepartment());
                    zxChange.setUsers(zxAssetManagement.getExtend2());

                   /* SysUser sysUser = iSysUserService.selectUserByLoginName(ShiroUtils.getLoginName());
                    String c= iSysDeptService.selectDeptById(sysUser.getDeptId()).getDeptName();
                    List<SysDictData> zc_department = iSysDictDataService.selectDictDataByType("zc_department");
                    for (SysDictData sysDictData:zc_department){
                        if (c.equals(sysDictData.getDictLabel())){
                            int d=Integer.parseInt(sysDictData.getDictValue());
                            zxChange.setSubmittedDepartment(d);
                        }
                    }*/


                    SysUser sysUser = iSysUserService.selectUserByLoginName(ShiroUtils.getLoginName());
                    SysDept sysDept = iSysDeptService.selectDeptById(sysUser.getDeptId());
                    String deptName = sysDept.getDeptName();
                    List<SysDept> sysDepts = iSysDeptService.selectDeptList(sysDept);
                    // List<SysDictData> zc_department = iSysDictDataService.selectDictDataByType("zc_department");

                    for (SysDept sysDept1:sysDepts){
                        if (deptName.equals(sysDept1.getDeptName())){
                            int d=sysDept1.getParentId().intValue();;
                            zxChange.setSubmittedDepartment(d);
                        }
                    }

                    zxChange.setSubmitOne(ShiroUtils.getLoginName());
                    int i = zxChangeService.insertZxChange(zxChange);
                    System.out.println(i);
                    i1 = zxAssetManagementService.updateZxAssetManagement(zxone);
                    System.out.println(i1);
                }
            }
            return toAjax(i1);
        }else{

            return toAjax(zxChangeService.insertZxChange(null));
        }
    }

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
            for (int i=0;i<split.length;i++){
                set.add(split[i]);
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
