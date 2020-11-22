package per.liu.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import per.liu.crm.settings.domain.User;
import per.liu.crm.settings.service.UserService;
import per.liu.crm.workbench.service.ClueService;

import javax.annotation.Resource;
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

}
