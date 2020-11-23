package per.liu.crm.workbench.service;

import per.liu.crm.workbench.domain.Activity;
import per.liu.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueService {

    boolean save(Clue clue);

    Clue detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    boolean unbund(String id);
}
