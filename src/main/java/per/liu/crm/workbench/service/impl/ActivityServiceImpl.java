package per.liu.crm.workbench.service.impl;

import org.springframework.stereotype.Service;
import per.liu.crm.settings.dao.UserDao;
import per.liu.crm.settings.domain.User;
import per.liu.crm.vo.PaginationVo;
import per.liu.crm.workbench.dao.ActivityDao;
import per.liu.crm.workbench.dao.ActivityRemarkDao;
import per.liu.crm.workbench.domain.Activity;
import per.liu.crm.workbench.domain.ActivityRemark;
import per.liu.crm.workbench.service.ActivityService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao activityDao;
    @Resource
    private ActivityRemarkDao activityRemarkDao;
    @Resource
    private UserDao userDao;

    @Override
    public Boolean save(Activity activity) {
        Boolean flag = true;

        int count = activityDao.save(activity);

        if (count != 1){
            flag =false;
        }

        return flag;
    }

    /**
     *
     * @param map map里放的是 skipCount：跳过前面的条数
     *                  activity：从前端发过来的查询条件，里面有4个属性name owner startDate endDate
     * @return
     */
    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {
        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
        //取得total
        int total = activityDao.getTotal(map);
        //int total = dataList.size();
        System.out.println("一共" + total + "条数据！");

        //创建一个Vo，将dataList和total封装到vo中
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        System.out.println("PaginationVo：" + vo);

        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2= activityRemarkDao.deleteAids(ids);

        if (count1 != count2){
            flag = false;
        }
        System.out.println("bb----->" + flag);

        //删除市场活动
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length){
            flag = false;
        }
            System.out.println("aa----->" + flag);


        System.out.println("count1--->" + count1);
        System.out.println("count2--->" + count2);
        System.out.println("count3--->" + count3);

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        System.out.println("service中--id：" + id);
        //取uList
        List<User> uList = userDao.getUserList();

        //取a
        Activity a = activityDao.getById(id);

        //将uList和a打包到map中
        Map<String, Object> map = new HashMap<>();
        map.put("uList", uList);
        map.put("a", a);

        //返回map
        return map;
    }

    @Override
    public Boolean update(Activity activity) {

        System.out.println("Service中，update开始执行");

        Boolean flag = true;

        int count = activityDao.update(activity);

        if (count != 1){
            flag =false;
        }
        System.out.println("Service中，update执行结束--flag=" + flag);


        return flag;

    }

    @Override
    public Activity detail(String id) {

        Activity a = activityDao.detail(id);

        System.out.println("----a.id"+ a.getId());

        //根据id查单条活动信息
        return a;

    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        System.out.println("getRemarkListByAid中activityId=" + activityId);
        List<ActivityRemark> activityRemarks = activityRemarkDao.getRemarkListByAid(activityId);
        System.out.println("在getRemarkListByAid中 activityRemarks.size=" + activityRemarks.size());
        return activityRemarks;
    }

    @Override
    public boolean deleteRemark(String id) {

        return activityRemarkDao.deleteRemark(id);
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {


        return activityDao.getActivityListByNameAndNotByClueId(map);
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {

        return  activityDao.getActivityListByName(aname);
    }


}
