package per.liu.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import per.liu.crm.settings.domain.User;
import per.liu.crm.settings.service.UserService;
import per.liu.crm.utils.DateTimeUtil;
import per.liu.crm.utils.UUIDUtil;
import per.liu.crm.vo.PaginationVo;
import per.liu.crm.workbench.domain.Activity;
import per.liu.crm.workbench.domain.ActivityRemark;
import per.liu.crm.workbench.service.ActivityService;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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


    //市场活动添加操作
    @RequestMapping("/save.do")
    @ResponseBody
    public Boolean save(HttpServletRequest request) {
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
        String createBy = ((User) request.getSession().getAttribute("user")).getName();


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

    //局部刷新市场活动信息列表操作
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
    public PaginationVo<Activity> pageList(Activity activity, int pageNo, int pageSize) {
        System.out.println("进入到查询市场活动信息列表的操作（结合分页查询和条件查询）");

        //计算跳过的记录数
        int skipCount = (pageNo - 1) * pageSize;

        System.out.println("---------------------");
        System.out.println(activity);
        System.out.println(activity.getName());
        System.out.println("---------------------");


        Map<String, Object> map = new HashMap<>();
        map.put("activity", activity);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        //调用service里的方法返回值是记录条数和记录信息
        PaginationVo<Activity> vo = activityService.pageList(map);
        System.out.println("在ActivityController中-->" + vo);

        return vo;

    }

    //删除一条或多条市场活动信息
    @RequestMapping("/delete.do")
    @ResponseBody
    public Boolean Delete(String[] id) {

        System.out.println("执行市场活动的删除操作");

        for (String id1 : id) {
            System.out.println("--------" + id1);
        }

        boolean flag = activityService.delete(id);
        System.out.println("flag----->" + flag);

        //flag表示删除成功与否
        return flag;
    }


    @RequestMapping("/getUserListAndActivity.do")
    @ResponseBody
    public Map<String, Object> getUserListAndActivity(String id) {
        System.out.println("执行取得用户列表和单例市场活动的操作");

        Map<String, Object> map = activityService.getUserListAndActivity(id);

        return map;
    }

    @RequestMapping("/update.do")
    public Boolean update(HttpServletRequest request, Activity activity) {
        System.out.println("执行市场活动修改操作");
        Boolean flag = true;

        //在用户传过来的修改后数据上，添加修改时间、修改人
        activity.setEditTime(DateTimeUtil.getSysTime());
        activity.setEditBy(((User) request.getSession().getAttribute("user")).getName());

        flag = activityService.update(activity);

        System.out.println("controller中---flag=" + flag);

        return flag;
    }

    //workbench/activity/detail.do?id='+n.id+'
    @RequestMapping("/detail.do")
    public ModelAndView detail(String id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到挑转到详细信息页的操作");
        ModelAndView mv = new ModelAndView();
        System.out.println("收到的id=" + id);

        Activity activity = activityService.detail(id);
        mv.addObject("a", activity);
        mv.setViewName("forward:/workbench/activity/detail.jsp");

        System.out.println("aaaa="+ activity);
        //request.setAttribute("a", activity);
        //request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);
        return mv;
    }

    //      workbench/activity/getRemarkListByAid.do"
    @RequestMapping("/getRemarkListByAid.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkListByAid(String activityId){
        System.out.println("根据市场活动id，取得备注信息列表");
        System.out.println("activityId=" + activityId);
        List<ActivityRemark> activityRemarkList = activityService.getRemarkListByAid(activityId);

        System.out.println("getRemarkListByAid中的size" + activityRemarkList.size());

        return activityRemarkList;
    }
}
