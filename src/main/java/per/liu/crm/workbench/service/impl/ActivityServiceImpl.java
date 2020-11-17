package per.liu.crm.workbench.service.impl;

import org.springframework.stereotype.Service;
import per.liu.crm.vo.PaginationVo;
import per.liu.crm.workbench.dao.ActivityDao;
import per.liu.crm.workbench.domain.Activity;
import per.liu.crm.workbench.service.ActivityService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao activityDao;


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
        int total = dataList.size();
        System.out.println("一共" + total + "条数据！");

        //创建一个Vo，将dataList和total封装到vo中
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        System.out.println("PaginationVo：" + vo);

        return vo;
    }
}
