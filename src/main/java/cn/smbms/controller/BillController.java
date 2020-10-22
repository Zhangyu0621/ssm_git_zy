package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;
import com.mysql.jdbc.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class BillController {

    @Resource
    private BillService billService;
    @Resource
    private ProviderService providerService;

    /**
     * 处理 订单显示
     *
     * @param model
     * @param queryProductName
     * @param queryProviderId
     * @param queryIsPayment
     * @return
     */
    @RequestMapping("/billlist.html")
    public String main(Model model, String queryProductName, String queryProviderId, String queryIsPayment) {
        List<Provider> providerList = null;
        providerList = providerService.getProviderList("", "");
        model.addAttribute("providerList", providerList);

        if (StringUtils.isNullOrEmpty(queryProductName)) {
            queryProductName = "";
        }

        List<Bill> billList = null;
        Bill bill = new Bill();
        if (StringUtils.isNullOrEmpty(queryIsPayment)) {
            bill.setIsPayment(0);
        } else {
            bill.setIsPayment(Integer.parseInt(queryIsPayment));
        }

        if (StringUtils.isNullOrEmpty(queryProviderId)) {
            bill.setProviderId(0);
        } else {
            bill.setProviderId(Integer.parseInt(queryProviderId));
        }
        bill.setProductName(queryProductName);
        billList = billService.getBillList(bill);
        model.addAttribute("billList", billList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        return "billlist";
    }

    /**
     * 跳转  添加订单页面
     *
     * @param bill
     * @return
     */
    @RequestMapping("/addbill.html")
    public String addbill(@ModelAttribute Bill bill) {
        return "billadd";
    }

    /**
     * 处理  添加订单
     *
     * @param bill
     * @param session
     * @return
     */
    @RequestMapping(value = "/addbillsave.html")
    public String addbillSave(Bill bill, HttpSession session) {
        //创建时间
        bill.setCreationDate(new Date());
        //创建者
        bill.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        if (billService.add(bill)) {
            return "redirect:/billlist.html";
        }
        return "billadd";
    }

    /**
     *
     * ResponseBody   异步获取数据时使用
     * @return
     */
    @RequestMapping("/providerList")
    @ResponseBody
    public List<Provider> providerList() {
        return providerService.getProviderList(null, null);
    }


    /**
     * 查看
     * @param
     * @param model
     * @return
     */
    @RequestMapping(value = "/billview")
    public String view(String id, Model model) {
        Bill bill=billService.getBillById(id);
        model.addAttribute("bill", bill);
        return "billview";
    }

    /**
     * 跳转  修改订单
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/billmodify.html")
    public String modify(String id,Model model){
        Bill bill=billService.getBillById(id);
        model.addAttribute("bill",bill);
        return "billmodify";
    }

    @RequestMapping("/billsavemodify.html")
    public String savemodify(Bill bill, HttpSession session){
        //创建时间
        bill.setModifyDate(new Date());
        User proId = (User) session.getAttribute(Constants.USER_SESSION);
        bill.setModifyBy(proId.getId());
        if(billService.modify(bill)){
            return "redirect:/billlist.html";
        }
        return "billmodify";
    }

}
