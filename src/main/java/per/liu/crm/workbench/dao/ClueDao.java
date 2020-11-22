package per.liu.crm.workbench.dao;


import per.liu.crm.workbench.domain.Clue;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);
}
