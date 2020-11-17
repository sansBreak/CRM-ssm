package per.liu.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import per.liu.crm.settings.domain.User;
import per.liu.crm.settings.service.UserService;
import per.liu.crm.utils.DateTimeUtil;
import per.liu.crm.utils.UUIDUtil;
import per.liu.crm.vo.PaginationVo;
import per.liu.crm.workbench.domain.Activity;
import per.liu.crm.workbench.service.ActivityService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {

    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;

    //查询所有用户信息，并返回
    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> getUserList() {
        List<User> userList = userService.getUserList();

        return userList;
    }


    //workbench/activity/save.do
    @RequestMapping("/save.do")
    @ResponseBody
    public Boolean save(HttpServletRequest request){
        System.out.println("执行市场活动添加操作！");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //创建时间：为当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建者：从session中取出当前用户user，然后取得它所存储的name
        String createBy = ((User)request.getSession().getAttribute("user")).getName();


        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        Boolean flag = activityService.save(activity);

        return flag;
    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    /**
     *  @param pageNo 页码
     *  @param pageSize  每页所展现的记录数
     *    在mysql语句中：
     *           select * from table limit 10,5
     *       表示的是显示 5 条记录，略过前 10 填记录，即显示第 11 12 13 14 15 条记录
     *
     */
    public PaginationVo<Activity> PageList(Activity activity,int pageNo, int pageSize){
        System.out.println("进入到查询市场活动信息列表的操作（结合分页查询和条件查询）");

        //计算跳过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("activity", activity);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        //调用service里的方法返回值是记录条数和记录信息
        PaginationVo<Activity> vo = activityService.pageList(map);
        System.out.println("在ActivityController中-->" + vo);

        return vo;

    }
}
