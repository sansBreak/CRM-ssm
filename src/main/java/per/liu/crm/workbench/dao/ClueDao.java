package per.liu.crm.workbench.dao;


import per.liu.crm.workbench.domain.Activity;
import per.liu.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    Clue getById(String clueId);

    int delete(String clueId);
}
