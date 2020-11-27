package per.liu.crm.workbench.dao;

import per.liu.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClue(String clueId);

    int delect(ClueRemark clueRemark);
}
