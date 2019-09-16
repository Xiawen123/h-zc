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
import com.hp.property.service.IZxChangeService;
import com.hp.property.service.IZxDiscardService;
import com.hp.system.domain.SysDept;
import com.hp.system.domain.SysDictData;
import com.hp.system.domain.SysUser;
import com.hp.system.service.ISysDeptService;
import com.hp.system.service.ISysDictDataService;
import com.hp.system.service.ISysUserService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 跳转到 discard.html
     *
     * @return
     */
    @RequiresPermissions("property:discard:view")
    @GetMapping()
    public String change() {
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
    public TableDataInfo list(ZxChange zxChange) {
        startPage();
        //调用    zxDiscardService 的 selectZxDiscardList 方法查询报废记录信息列表
        List<ZxChange> list = zxDiscardService.selectZxDiscardList(zxChange);
        return getDataTable(list);
    }

    /**
     * 新增报废信息
     */
    @GetMapping("/add")
    public String add(HttpServletRequest request) {
        //清空session的值，防止有上次残留的值
        if (request.getSession().getAttribute("s") != null) {
            request.getSession().removeAttribute("s");
        }
        return prefix + "/add";
    }

    /**
     * 跳转到查询未报废的信息列表的页面(子窗口)
     */
    @GetMapping("/insert")
    public String insert() {
        return prefix + "/insert";
    }

    /**
     * 根据小窗口的id查未报废的资产数据
     * <p>
     * （就是根据传过来的ids查询数据）
     *
     * @param zxAssetManagement
     * @return
     */
    @RequiresPermissions("property:discard:alist")
    @PostMapping("/alist")
    @ResponseBody
    public TableDataInfo alist(ZxAssetManagement zxAssetManagement, HttpServletRequest request) {
        System.out.println(zxAssetManagement.getIds());
        //判断id是否为空或为null
        if (zxAssetManagement.getIds() != null && !zxAssetManagement.getIds().equals("")) {
            List<ZxAssetManagement> list = new LinkedList<>();
            //获取id
            String s = zxAssetManagement.getIds();
            System.out.println("s=" + s);
            //获取session
            HttpSession session = request.getSession();
            //判断session是否为空
            if (session.getAttribute("s") == null) {
                session.setAttribute("s", "0");//为了保持一样的格式方便切割（0，s）
                session.setAttribute("s", session.getAttribute("s") + "," + s);
            } else {
                session.setAttribute("s", session.getAttribute("s") + "," + s);
            }
            //获取session里的s的值
            String spl = session.getAttribute("s").toString();
            Set set = new HashSet();
            //切割
            String[] split = spl.split(",");
            //利用set去除重复的id
            for (int i = 0; i < split.length; i++) {
                set.add(split[i]);
            }
            //移除0和" "
            set.remove("0");
            set.remove(" ");
            //根据id查询相应的资产，存入list
            for (Object id : set) {
                String s1 = id.toString();
                if (!s1.equals("")) {
                    ZxAssetManagement ls = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(s1));
                    list.add(ls);
                }
            }
            return getDataTable(list);
        } else {
            List<ZxAssetManagement> list = new LinkedList<>();
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
    public AjaxResult addSave(ZxAssetManagement zxAssetManagement, HttpServletRequest request) {
        String ids = request.getSession().getAttribute("s").toString();
        System.out.println("ids=" + ids);
        int i1 = 0;
        Set set = new HashSet();
        ZxChange zxChange = new ZxChange();
        String[] split = ids.split(",");
        for (int i = 0; i < split.length; i++) {
            set.add(split[i]);
        }
        set.remove("0");
        set.remove(" ");
        System.out.println("set=" + set);
        for (Object id : set) {
            String s1 = id.toString();
            System.out.println("id=" + id);
            if (!s1.equals("")) {
                ZxAssetManagement zxone = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(s1));
                //资产表资产的状态
                zxone.setState(4);
                //资产表的报废时间
                zxone.setDiscardTime(zxAssetManagement.getDiscardTime());
                //资产表的使用人
                zxone.setExtend2(zxAssetManagement.getExtend2());
                //变更表的资产id
                zxChange.setAssetsId(Long.parseLong(s1));
                //雪花算法生成id
                long cid = SnowFlake.nextId();
                //变更表的id
                zxChange.setId(cid);
                //变更表的变更类型
                zxChange.setChangeType(4);
                //提交部门
                SysUser sysUser = iSysUserService.selectUserByLoginName(ShiroUtils.getLoginName());
                SysDept sysDept = iSysDeptService.selectDeptById(sysUser.getDeptId());
                String deptName = sysDept.getDeptName();
                List<SysDept> sysDepts = iSysDeptService.selectDeptList(sysDept);
                // List<SysDictData> zc_department = iSysDictDataService.selectDictDataByType("zc_department");
                for (SysDept sysDept1 : sysDepts) {
                    if (deptName.equals(sysDept1.getDeptName())) {
                        int d = sysDept1.getParentId().intValue();
                        ;
                        //变更表的提交部门
                        zxChange.setSubmittedDepartment(d);
                        //资产表的提交部门
                        zxone.setExtend1(String.valueOf(d));
                    }
                }
                    System.out.println(zxChange.getExtend5() + "校区");
                    //变更表的使用部门
                    zxChange.setUseDepartment(zxAssetManagement.getDepartment());
                    //变更表的提交人
                    zxChange.setSubmitOne(ShiroUtils.getLoginName());
                    //变更表的使用人
                    zxChange.setUsers(zxAssetManagement.getExtend2());
                    //变更表的变更时间
                    /*zxChange.setExtend1(DateString.getString(new Date(),"yyyy-MM-dd HH:mm:ss"));*/
                    zxChange.setExtend1(DateString.getString(zxAssetManagement.getDiscardTime(), "yyyy-MM-dd HH:mm:ss"));
                    int i = zxChangeService.insertZxChange(zxChange);
                    i1 = zxAssetManagementService.updateZxAssetManagement(zxone);
                }
            }
            return toAjax(i1);
        }

        /**
         * 查询资产信息列表（查询未报废的资产）
         */
        @RequiresPermissions("property:discard:inserts")
        @PostMapping("/inserts")
        @ResponseBody
        public TableDataInfo insert (ZxAssetManagement zxAssetManagement)
        {
            zxAssetManagement.setState(4);
            startPage();
            List<ZxAssetManagement> list = zxDiscardService.selectZxNoDiscardList(zxAssetManagement);
            return getDataTable(list);
        }

        /**
         * 根据变更记录id查询详情
         */
        @GetMapping("/one/{id}")
        public String one (@PathVariable("id") Long id, ModelMap mmap)
        {
            ZxChange change = zxDiscardService.selectDiscardChangeById(id);
            mmap.put("change", change);
            return prefix + "/one";
        }

        /**
         * 删除资产信息
         */
        @RequiresPermissions("property:discard:remove")
        @Log(title = "报废", businessType = BusinessType.DELETE)
        @PostMapping("/remove")
        @ResponseBody
        public AjaxResult remove (String id, HttpServletRequest request, HttpServletResponse reps)
        {
            String s = request.getSession().getAttribute("s").toString();
            /*Set set = new HashSet();*/
            String c = "";
            String[] split = s.split(",");
            System.out.println(split);
            for (int i = 0; i < split.length; i++) {
                /* set.add(split[i]);*/
                c = c + "," + split[i];
            }
/*        set.remove("0");
        set.remove(" ");*/
            /* set.remove("s");*/
        /*System.out.println("set="+set);
        set.remove(" ");
        set.remove(id);*/
       /* System.out.println("set.toString()"+set.toString());
        request.getSession().removeAttribute("s");
        request.getSession().setAttribute("s",set.toString());*/
            System.out.println("c=" + c);
            request.getSession().removeAttribute("s");
            request.getSession().setAttribute("s", c);
            /*Cookie cookie = new Cookie("u"+i, java.net.URLEncoder.encode(jsonUser.toString(), "UTF-8"));*/
        /*String c = set.toString();
        System.out.println(c+"!!!!!");
        if (c.contains("[")){
            c.replace("[","");
        }
        if (c.contains("]")){
            c.replace("]","");
        }
        if (c.contains(",")){
            c.replace(",","#");
        }
        Cookie cookie = new Cookie("is",c);
        System.out.println(cookie.getValue());
        reps.addCookie(cookie);*/
            return toAjax(1);
        }

    /*public TableDataInfo remove(String ids,HttpServletRequest request)
    {
        List<ZxAssetManagement> list=new LinkedList<>();
        String s = request.getSession().getAttribute("s").toString();
        Set set = new HashSet();
        //切割
        String[] split =s.split(",");
        //利用set去除重复的id
        for (int i=0;i<split.length;i++){
            set.add(split[i]);
        }
        //移除0和" "
        set.remove("0");
        set.remove(" ");
        set.remove(ids);
        request.getSession().setAttribute("s",set);
        Object[] array = set.toArray();
        System.out.println(array);
        *//*session.setAttribute("s",session.getAttribute("s")+","+s);*//*

        return getDataTable(list);
    }*/



}
