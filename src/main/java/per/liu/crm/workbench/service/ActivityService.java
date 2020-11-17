package per.liu.crm.workbench.service;

import per.liu.crm.vo.PaginationVo;
import per.liu.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {

    Boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);
}
