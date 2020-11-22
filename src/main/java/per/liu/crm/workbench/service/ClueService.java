package per.liu.crm.workbench.service;

import per.liu.crm.workbench.domain.Clue;

public interface ClueService {

    boolean save(Clue clue);

    Clue detail(String id);
}
