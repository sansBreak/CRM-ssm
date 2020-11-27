package per.liu.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import per.liu.crm.settings.domain.User;
import per.liu.crm.settings.service.UserService;
import per.liu.crm.utils.DateTimeUtil;
import per.liu.crm.utils.UUIDUtil;
import per.liu.crm.workbench.domain.Activity;
import per.liu.crm.workbench.domain.Clue;
import per.liu.crm.workbench.domain.Tran;
import per.liu.crm.workbench.service.ActivityService;
import per.liu.crm.workbench.service.ClueService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    @Resource
    private ClueService clueService;
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;

    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        System.out.println("取得用户信息列表");

        return userService.getUserList();
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public boolean save(Clue clue, HttpServletRequest request){
        System.out.println("保存线索！");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        clue.setId(id);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);

        boolean flag = clueService.save(clue);

        return flag;
    }

    @RequestMapping("/detail.do")
    @ResponseBody
    public ModelAndView detail(String id){
        System.out.println("挑转到线索详细信息页的操作");
        ModelAndView mv = new ModelAndView();

        Clue clue = clueService.detail(id);
        mv.addObject("c", clue);
        mv.setViewName("forward:/workbench/clue/detail.jsp");

        return mv;
    }

    @RequestMapping("/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId){
        System.out.println("进入取得关联的市场活动信息列表");

        return clueService.getActivityListByClueId(clueId);

    }

    @RequestMapping("/unbund.do")
    @ResponseBody
    public boolean unbund(String id){

        return clueService.unbund(id);
    }

    @RequestMapping("/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByNameAndNotByClueId(@RequestParam Map<String,String> map){
                                 // @RequestParam 注解如果是个map类型,那么mvc适配器就将参数封装到map中
        String aname =  map.get("aname");
        String clueId = map.get("clueId");
        System.out.println("aname=="+ aname + "    clueId=="+clueId);

        List<Activity> activityList = activityService.getActivityListByNameAndNotByClueId(map);
        System.out.println("size" + activityList.size());

        for (Activity activity: activityList) {
            System.out.println("--" + activity);
        }

        return activityList;

    }

    @RequestMapping("/bund.do")
    @ResponseBody
    public boolean bund(String cid,String[] aids){

        return clueService.bund(cid,aids);
    }

    @RequestMapping("/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String aname){
        System.out.println("查询市场活动列表（根据名称模糊查）");

        return activityService.getActivityListByName(aname);
    }

    @RequestMapping("/convert.do")
    @ResponseBody
    public ModelAndView convert(String flag, String clueId, Tran tran, HttpServletRequest request){
        System.out.println("执行线索转换的操作");

        ModelAndView mv = new ModelAndView();

        //flag 这个参数是代表是否需要创建交易的标记,如果需要创建交易，则接收表单中的参数
        if ("a".equals(flag)){
            //若flag里存储的是a，则需要创建交易,由于部分参数已经自动赋给Tran对象，我们只需完善一部分即可
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        }

        /*


        */
        System.out.println("================"+tran);

        boolean flag1 = clueService.convert(clueId,tran);



        if (flag1){
            mv.setViewName("redirect:/" + request.getContextPath() + "/workbench/clue/index.jsp");
        }

        return mv;
    }


}
