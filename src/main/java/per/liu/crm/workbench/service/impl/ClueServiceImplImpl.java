package per.liu.crm.workbench.service.impl;

import org.springframework.stereotype.Service;
import per.liu.crm.utils.UUIDUtil;
import per.liu.crm.workbench.dao.ClueActivityRelationDao;
import per.liu.crm.workbench.dao.ClueDao;
import per.liu.crm.workbench.domain.Activity;
import per.liu.crm.workbench.domain.Clue;
import per.liu.crm.workbench.domain.ClueActivityRelation;
import per.liu.crm.workbench.service.ClueService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClueServiceImplImpl implements ClueService {
    @Resource
    private ClueDao clueDao;
    @Resource
    ClueActivityRelationDao clueActivityRelationDao;

    @Override
    public boolean save(Clue clue) {
        System.out.println("进入到了service层下的save（）中");
        boolean flag = true;

        int count = clueDao.save(clue);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue detail(String id) {

        return clueDao.detail(id);
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> activityList= clueDao.getActivityListByClueId(clueId);
        return activityList;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {

        boolean flag=true;

        for (String aid:aids){

            //取得每一个aid和cid做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);

            //添加关联关系表中的记录
            int count = clueActivityRelationDao.bund(car);
            if (count != 1){
                flag=false;
            }

        }

        return flag;


    }


}
