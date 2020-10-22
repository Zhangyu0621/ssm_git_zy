package cn.smbms.controller;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;
import com.mysql.jdbc.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
//@RequestMapping("/provider")
public class ProviderController {

    @Resource
    private ProviderService providerService;

    @RequestMapping("/providerlist.html")
    public String main(Model model, String queryProName, String queryProCode){
        if(StringUtils.isNullOrEmpty(queryProName)){
            queryProName = "";
        }
        if(StringUtils.isNullOrEmpty(queryProCode)){
            queryProCode = "";
        }
        List<Provider> providerList = null;
        providerList = providerService.getProviderList(queryProName,queryProCode);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);
        return "providerlist";
    }

    /**
     * 跳转 添加供应商页面
     * @return
     */
    @RequestMapping("/addProvider.html")
    public String addProvider(){
        return "provideradd";
    }

    /**
     * 处理 添加供应商页面
     * @return
     */
    @RequestMapping(value = "/addProvider.html",method = RequestMethod.POST)
    public String saveProvider(Provider provider, HttpSession session){
        //创建时间
        provider.setCreationDate(new Date());
        if(providerService.add(provider)){
            return "redirect:/providerlist.html";
        }
        return "provideradd";
    }

    /**
     *  跳转 修改供应商信息
     * @return
     */
    @RequestMapping("/promodify.html")
    public String modify(String id,Model model){
        Provider provider= providerService.getProviderById(id);
        System.out.println("========================="+id);
        model.addAttribute("provider",provider);
        return "providermodify";
    }

    /**
     * 处理 修改供应商信息
     * @param provider
     * @return
     */
   @RequestMapping(value = "/promodify.html",method = RequestMethod.POST)
    public String saveModify(Provider provider, HttpSession session){
       //创建时间
       provider.setModifyDate(new Date());
       User proId = (User) session.getAttribute(Constants.USER_SESSION);
       provider.setModifyBy(proId.getId());
       if(providerService.modify(provider)){
           return "redirect:/providerlist.html";
       }
        return "providermodify";
    }

    /**
     * 查看
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/view.html/{id}")
    public String view(@PathVariable String id, Model model) {
        Provider provider=providerService.getProviderById(id);
        model.addAttribute("provider", provider);
        return "providerview";
    }



}
