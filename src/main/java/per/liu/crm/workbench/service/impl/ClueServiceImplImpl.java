package per.liu.crm.workbench.service.impl;

import org.springframework.stereotype.Service;
import per.liu.crm.workbench.dao.ClueDao;
import per.liu.crm.workbench.service.ClueService;

import javax.annotation.Resource;

@Service
public class ClueServiceImplImpl implements ClueService {
    @Resource
    private ClueDao clueDao;
}
