package per.liu.crm.workbench.service.impl;

import org.springframework.stereotype.Service;
import per.liu.crm.workbench.dao.ActivityDao;
import per.liu.crm.workbench.domain.Activity;
import per.liu.crm.workbench.service.ActivityService;

import javax.annotation.Resource;

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
}
