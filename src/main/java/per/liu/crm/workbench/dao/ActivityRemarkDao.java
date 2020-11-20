package per.liu.crm.workbench.dao;

import per.liu.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    int saveRemark(ActivityRemark ar);
}
