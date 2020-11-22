package per.liu.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import per.liu.crm.settings.domain.User;
import per.liu.crm.settings.service.UserService;
import per.liu.crm.utils.DateTimeUtil;
import per.liu.crm.utils.UUIDUtil;
import per.liu.crm.workbench.domain.Clue;
import per.liu.crm.workbench.service.ClueService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    @Resource
    private ClueService clueService;
    @Resource
    private UserService userService;

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
}
