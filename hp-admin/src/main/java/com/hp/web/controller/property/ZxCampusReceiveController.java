package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.DateString;
import com.hp.common.utils.SnowFlake;
import com.hp.framework.util.ShiroUtils;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.impl.ZxChangeServiceImpl;
import com.hp.system.domain.SysDept;
import com.hp.system.domain.SysDictData;
import com.hp.system.domain.SysUser;
import com.hp.system.service.ISysDeptService;
import com.hp.system.service.ISysDictDataService;
import com.hp.system.service.ISysUserService;
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

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private ISysDictDataService iSysDictDataService;

    // 跳转到校区领用主页面
    @RequiresPermissions("property:campusrecive:view")
    @GetMapping()
    public String management(ModelMap mmap)
    {
        SysDept sysDept = new SysDept();
        List<SysDept> sysDepts = iSysDeptService.selectDeptList(sysDept);
        mmap.put("school",sysDepts);
        return prefix + "/campusrecive";
    }

    /**
     * 查询校区领用主页面即领用资产信息列表
     */
    @RequiresPermissions("property:campusrecive:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxChange zxChange)
    {
        System.out.println("extend4*******************:" + zxChange.getExtend4());
        startPage();
        // 查询变更表中所有变动类型为1即领用的所有记录
        List<ZxChange> list = zxChangeService.findAllChangeTypeOne(zxChange);

        SysDept sysDept = new SysDept();
        List<SysDept> sysDepts = iSysDeptService.selectDeptList(sysDept);

        //循环存入校区名，存入备用字段extend4
        for (ZxChange zxChange1:list){
            for (SysDept sysDept1:sysDepts) {
                if (zxChange1.getExtend4()!=null){
                    String a=zxChange1.getExtend4();
                    String b=sysDept1.getDeptId().toString();
                    if (a.equals(b)) {
                        String c=sysDept1.getDeptName();
                        zxChange1.setExtend4(c);
                    }
                }
            }
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
    public AjaxResult addSave(ZxAssetManagement zxAssetManagement, HttpSession session)
    {
        System.out.println("zxAssetManagement:_____________" + zxAssetManagement.toString());

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
                zxChange.setShareTime(zxChange.getShareTime());
                //在变更表中存入提交人
                zxChange.setSubmitOne(ShiroUtils.getLoginName());
                //在变更表中存入提交人所属部门
                SysUser sysUser = iSysUserService.selectUserByLoginName(ShiroUtils.getLoginName());
                String c= iSysDeptService.selectDeptById(sysUser.getDeptId()).getDeptName();
                List<SysDictData> zc_department = iSysDictDataService.selectDictDataByType("zc_department");
                for (SysDictData sysDictData:zc_department){
                    if (c.equals(sysDictData.getDictLabel())){
                        int d=Integer.parseInt(sysDictData.getDictValue());
                        zxChange.setSubmittedDepartment(d);
                    }
                }
                // 在变更表中存入使用校区
                zxChange.setExtend4(zxAssetManagement.getExtend4());

                zxChangeService.insertZxChange(zxChange);
                i1 = zxAssetManagementService.updateZxAssetManagement(zxone);
                session.removeAttribute("s");
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
    @PostMapping("/list3")
    @ResponseBody
    public TableDataInfo list3(ZxAssetManagement zxAssetManagement, HttpServletRequest request)
    {
        //获取num（用于删除做判断：num=0删除状态,num=-1正常添加状态）
        int num = zxAssetManagement.getNum();
        if(num == 0){
            //清空session信息
            request.getSession().removeAttribute("s");

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
                        long idam = Long.parseLong(s1);
                        ZxAssetManagement ls = zxAssetManagementService.selectZxAssetManagementById(idam);
                        list.add(ls);
                    }
                }
                return getDataTable(list);
            }else {
                List<ZxAssetManagement> list=new LinkedList<>();
                return getDataTable(list);
            }
        }else{
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
                        long idam = Long.parseLong(s1);
                        ZxAssetManagement ls = zxAssetManagementService.selectZxAssetManagementById(idam);
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
}
