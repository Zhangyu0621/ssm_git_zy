package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;


    /**
     * 退出
     * redirect  重定向
     *
     * @return
     */
    @RequestMapping("/loginout.html")
    public String loginout(HttpSession session) {
        session.removeAttribute(Constants.USER_SESSION);
        return "redirect:/user/login.html";
    }

    /**
     * 跳转登录页面
     *
     * @return
     */
    @RequestMapping("/login.html")
    public String login() {
        return "login";
    }

    /**
     * 处理登录界面
     *
     * @param userCode
     * @param userPassword
     * @param
     * @return
     */
    @RequestMapping(value = "/login.html", method = RequestMethod.POST)
    public String doLogin(String userCode, String userPassword, HttpSession session, HttpServletRequest request) {
        User user = userService.login(userCode, userPassword);
        if (user != null) {
            session.setAttribute(Constants.USER_SESSION, user);
            return "redirect:/user/frame.html";
} else {
        request.setAttribute("error", "用户名或密码错误!");
        return "login";
        }
        }
    @RequestMapping("/userlist.html")

    public String main(Model model, String queryname, @RequestParam(value = "queryUserRole", defaultValue = "0") Integer userRole,
                       @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex) {
        //查询用户列表
        int queryUserRole = 0;
        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        System.out.println("queryUserName servlet--------" + queryname);
        System.out.println("queryUserRole servlet--------" + queryUserRole);
        System.out.println("query pageIndex--------- > " + pageIndex);

        //总数量（表）
        int totalCount = userService.getUserCount(queryname, userRole);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(pageIndex);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        //控制首页和尾页
        if (pageIndex < 1) {
            pageIndex = 1;
        } else if (pageIndex > totalPageCount) {
            pageIndex = totalPageCount;
        }


        userList = userService.getUserList(queryname, userRole, pageIndex, pageSize);
        model.addAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryname);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", pageIndex);
        return "userlist";
    }

    @RequestMapping("/frame.html")
    public String frame(HttpSession session) {
        User user = (User) session.getAttribute(Constants.USER_SESSION);
        if (user == null) {
            return "login";
        }
        return "frame";
    }

    /**
     * 跳转添加页面
     *
     * @return
     */
    @RequestMapping("/addUser.html")
    public String addUser(@ModelAttribute User user) {
        return "useradd";
    }

    /**
     * 处理 添加页面
     *
     * @param user
     * @param session
     * @return
     */
    /*@RequestMapping(value = "/addUser.html", method = RequestMethod.POST)
    public String addUserSave(User user,HttpSession session) {
        if(bindingResult.hasErrors()){
            return "user/useradd";
        }
        //创建时间
        user.setCreationDate(new Date());
        //创建者
        User user_login = (User) session.getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(user_login.getId());
        if (userService.add(user)) {
            return "redirect:/user/userlist.html";
        }
        return "user/useradd";
    }*/

    /**
     * 文件上传
     *
     * @param user
     * @param session
     * @param request
     * @param attan
     * @return
     *//*
    @RequestMapping(value = "/addUser.html",method = RequestMethod.POST)
    public String addUserSave(User user, HttpSession session, HttpServletRequest request,
                              @RequestParam(value = "attan",required = false) MultipartFile attan){
        //个人图片路径
        String picPath="";
        //判断文件是否为空,不为空才能上传
        if(!attan.isEmpty()){
            //File.separator  文件的系统自适应分隔符
            //文件上传的路径
            String path=request.getServletContext().getRealPath("/statics"+ File.separator+"fileUpload");
            System.out.println("文件路径是==："+path);
            //判断文件大小
            if(attan.getSize()>5000000){
                request.setAttribute("error","文件太大，上传失败...");
                return "useradd";
            }
            List<String> suffexs= Arrays.asList(new String[]{".jpg",".png",".pneg",".gif"});
            //获取文件名称
            String oldFileName=attan.getOriginalFilename();
            System.out.println("文件名称："+oldFileName);
            //截取文件后缀
            String suffex=oldFileName.substring(oldFileName.lastIndexOf("."),oldFileName.length());
            System.out.println("源文件后缀为："+suffex);
            //判断文件后缀是否正确
            if(!suffexs.contains(suffex)){
                request.setAttribute("error","文件类型错误，上传失败...");
                return "useradd";
            }
            //文件重命名  ：解决1.重名2.中文乱码
            String newFileName=System.currentTimeMillis()+""+new Random().nextInt(100000)+suffex;
            File file=new File(path,newFileName);
            if(!file.exists()){
                file.mkdirs();
            }
            try {
                attan.transferTo(file);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error","上传失败...");
                return "useradd";
            }
            //图片路径
            picPath=file+File.separator+newFileName;
        }
        user.setPicPath(picPath);
        //创建时间
        user.setCreationDate(new Date());
        //创建者
        User user_login = (User) session.getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(user_login.getId());
        if (userService.add(user)) {
            return "redirect:/user/userlist.html";
        }
        return "useradd";
    }*/

    /**
     * 添加
     *
     * @param user
     * @param session
     * @param request
     * @param attan
     * @return
     */
    @RequestMapping(value = "/addUser.html", method = RequestMethod.POST)
    public String addUserSave(User user, HttpSession session, HttpServletRequest request,
                              @RequestParam(value = "attan", required = false) MultipartFile attan,
                              @RequestParam(value = "attan_work", required = false) MultipartFile attan_work) {
        //个人图片路径
        String picPath = uploadFile(request, attan);
        String picWorkPath = uploadFile(request, attan_work);
        //判断是否有文件
        if (picPath == null || picWorkPath == null) {
            return "useradd";
        }
        user.setPicPath(picPath);
        user.setPicWorkPath(picWorkPath);
        //创建时间
        user.setCreationDate(new Date());
        //创建者
        User user_login = (User) session.getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(user_login.getId());
        if (userService.add(user)) {
            return "redirect:/user/userlist.html";
        }
        return "useradd";
    }

    /**
     * 多文件上传(方法提取)
     *
     * @param request
     * @param attan
     * @return
     */
    public String uploadFile(HttpServletRequest request, MultipartFile attan) {
        String picPath = "";
        //判断文件是否为空,不为空才能上传
        if (!attan.isEmpty()) {
            //File.separator  文件的系统自适应分隔符
            //文件上传的路径
            String path = request.getServletContext().getRealPath("/statics" + File.separator + "fileUpload");
            System.out.println("文件路径是==：" + path);
            //判断文件大小
            if (attan.getSize() > 5000000) {
                request.setAttribute("error", "文件太大，上传失败...");
                return null;
            }
            List<String> suffexs = Arrays.asList(new String[]{".jpg", ".png", ".pneg", ".gif"});
            //获取文件名称
            String oldFileName = attan.getOriginalFilename();
            System.out.println("文件名称：" + oldFileName);
            //截取文件后缀
            String suffex = oldFileName.substring(oldFileName.lastIndexOf("."), oldFileName.length());
            System.out.println("源文件后缀为：" + suffex);
            //判断文件后缀是否正确
            if (!suffexs.contains(suffex)) {
                request.setAttribute("error", "文件类型错误，上传失败...");
                return null;
            }
            //文件重命名  ：解决1.重名2.中文乱码
            String newFileName = System.currentTimeMillis() + "" + new Random().nextInt(100000) + suffex;
            File file = new File(path, newFileName);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                attan.transferTo(file);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "上传失败...");
                return null;
            }
            //图片路径
            picPath = newFileName;
        }
        return picPath;
    }


    /**
     *  JSR  303验证
     *
     * @param user
     * @param session
     * @return
     */
    /*@RequestMapping(value = "/addUser.html", method = RequestMethod.POST)
    public String addUserSave(@Valid User user, BindingResult bindingResult,HttpSession session) {
        if(bindingResult.hasErrors()){
            return "user/useradd";
        }
        //创建时间
        user.setCreationDate(new Date());
        //创建者
        User user_login = (User) session.getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(user_login.getId());
        if (userService.add(user)) {
            return "redirect:/user/userlist.html";
        }
        return "user/useradd";
    }*/


    /**
     * 修改
     *
     * @return
     */
    @RequestMapping("/modify.html")
    public String modify(String uid, Model model) {
        User user = userService.getUserById(uid);
        model.addAttribute("user", user);
        return "usermodify";
    }

    /**
     * 处理 修改
     *
     * @param user
     * @param session
     * @return
     */
    @RequestMapping(value = "/modify.html", method = RequestMethod.POST)
    public String saveModify(User user, HttpSession session) {
        //创建时间
        user.setModifyDate(new Date());
        //创建者
        User user_login = (User) session.getAttribute(Constants.USER_SESSION);
        user.setModifyBy(user_login.getId());
        if (userService.modify(user)) {
            return "redirect:/user/userlist.html";
        }
        return "usermodify";
    }

    /**
     * 异步请求  获取角色
     *
     * @return
     */
    @RequestMapping("/roleList")
    @ResponseBody
    public List<Role> roleList() {
        return roleService.getRoleList();
    }

    /**
     * 查看
     *
     * @param id
     * @param model
     * @return
     *//*
    @RequestMapping(value = "/view.html/{id}")
    public String view(@PathVariable String id, Model model) {
        User user = userService.getUserById(id);
       *//* String picPath = user.getPicPath();3
        user.setPicPath(picPath.substring(picPath.lastIndexOf("\\") + 1));*//*
        model.addAttribute("user", user);
        return "userview";
    }*/
    @RequestMapping(value = "/view")
    @ResponseBody
    public User view(String id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return user;
    }

    @RequestMapping(value = "/isExists")
    @ResponseBody
    public Object isExist(String userCode) {
        User user = userService.selectUserCodeExist(userCode);
        Map<String, Object> map = new HashMap<>();
        if (user != null) {
            map.put("userCode", "exist");
        } else {
            map.put("userCode", "noexist");
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 跳转   修改密码
     *
     * @return
     */
    @RequestMapping("/pwdmodify.html")
    public String pwdmodify() {
        return "pwdmodify";
    }

   /* @RequestMapping("/getPwdByUserId")
    @ResponseBody
    public String getPwdByUserId(){
        return userService.getUserById(idz);
    }*/

    @RequestMapping("/pwdsave.html")
    public String pwdSave() {


        return "pwdmodify";
    }

}