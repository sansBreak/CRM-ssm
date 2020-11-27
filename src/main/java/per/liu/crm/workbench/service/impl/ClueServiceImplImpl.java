package per.liu.crm.workbench.service.impl;

import org.springframework.stereotype.Service;
import per.liu.crm.utils.DateTimeUtil;
import per.liu.crm.utils.UUIDUtil;
import per.liu.crm.workbench.dao.*;
import per.liu.crm.workbench.domain.*;
import per.liu.crm.workbench.service.ClueService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClueServiceImplImpl implements ClueService {
    @Resource private ClueDao clueDao;
    @Resource private ClueActivityRelationDao clueActivityRelationDao;
    @Resource private ClueRemarkDao clueRemarkDao;

    //客户相关private
    @Resource private CustomerDao customerDao;
    @Resource private CustomerRemarkDao customerRemarkDao;

    //联系相关private
    @Resource private ContactsDao contactsDao;
    @Resource private ContactsRemarkDao contactsRemarkDao;
    @Resource private ContactsActivityRelationDao contactsActivityRelationDao;

    //交易private
    @Resource private TranDao tranDao;
    @Resource private TranHistoryDao tranHistoryDao;


    @Override
    public boolean save(Clue clue) {
        System.out.println("进入到了service层下的save（）中");
        boolean flag = true;

        int count = clueDao.save(clue);

        if (count != 1) {
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
        List<Activity> activityList = clueDao.getActivityListByClueId(clueId);
        return activityList;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {

        boolean flag = true;

        for (String aid : aids) {

            //取得每一个aid和cid做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);

            //添加关联关系表中的记录
            int count = clueActivityRelationDao.bund(car);
            if (count != 1) {
                flag = false;
            }

        }

        return flag;


    }

    @Override
    public boolean convert(String clueId,Tran tran) {

        String createBy = tran.getCreateBy();
        String createTime = DateTimeUtil.getSysTime();

        boolean flag = true;

        //1、通过线索id获取线索对象（线索对象封装了客户信息的信息）
        Clue c = clueDao.getById(clueId);

        //2、通过线索对象提取客户信息，当该客户不存在时，新建客户（根据公司的名称精确匹配，判断该客户是否存在）
        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);
           //如果customer为null，说明以前没有这个客户，需要重新建一个    ，数据从线索（clue）里取
        if (cus == null){

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateTime(createTime);
            cus.setCreateBy(createBy);
            cus.setContactSummary(c.getContactSummary());

            //添加客户
            int count1 = customerDao.save(cus);
            if (count1 != 1){
                flag = false;
            }
        }

        /*
        ----------------------------------------------------------------------
            经过第二步处理，客户的信息已经存在了，将来在处理其他表时，
                如果要使用到客户的id，可以直接使用cus.getId()
        ----------------------------------------------------------------------
        */

        //3、通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(c.getContactSummary());
        con.setAppellation(c.getAppellation());
        con.setAppellation(c.getAppellation());
        //添加联系人
        int count2 = contactsDao.save(con);
        if (count2 != 1){
            flag = false;
        }

        /*
        ----------------------------------------------------------------------
            经过第三步处理，联系人的信息已经存在了，将来在处理其他表时，
                如果要使用到联系人的id，可以直接使用con.getId()
        ----------------------------------------------------------------------
        */

        //4、线索备注转换到客户备注以及联系人备注
        //查询出与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClue(clueId);
        //取出每一条线索的备注
        for (ClueRemark clueRemark:clueRemarkList){

            //取出备注信息（主要转换到客户备注和联系人备注的）
            String noteContent = clueRemark.getNoteContent();

            //创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);

            System.out.println("-----------------------");
            System.out.println(customerRemark);
            System.out.println("-----------------------");

            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 != 1){
                flag = false;
            }


            //创建联系人备注对象，添加联系人
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);

            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 != 1){
                flag = false;
            }

        }

        //5.“线索与市场活动”的关系转换到“联系人和市场活动”的关系
        //查询出与该线索关联的市场活动，查询出与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        //遍历出每一条与市场活动关联的关联关系记录
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){

            //从每一个遍历出来的记录中取出相关联的市场活动id
            String activityId = clueActivityRelation.getActivityId();

            //创建 联系人与市场活动的关联对象  让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());

            //添加联系人与市场活动的关联关系
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 != 1){
                flag = false;
            }

        }

        //6、如果有创建交易需求，创建一条交易
        if (tran != null){
            /**
                tran对象在controller里已经封装好的信息如下：
             id，money，name，expectedDate，stage，activityId，createBy，createTime
                接下来可以通过第一步生成的c对象，取出一些信息，继续完善对tran对象的封装
             */

            tran.setSource(c.getSource());
            tran.setOwner(c.getOwner());
            tran.setNextContactTime(c.getNextContactTime());
            tran.setDescription(c.getDescription());
            tran.setCustomerId(cus.getId());
            tran.setContactSummary(c.getContactSummary());
            tran.setContactsId(con.getId());

            //添加交易
            int count6 = tranDao.save(tran);
            if (count6 != 1){
                flag = false;
            }

            //7、如果创建了交易，则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setExpectedDate(tran.getExpectedDate());
            th.setMoney(tran.getMoney());
            th.setStage(tran.getStage());
            th.setTranId(tran.getId());
            //添加交易历史
            int count7 = tranHistoryDao.save(th);
            if (count7 != 1){
                flag = false;
            }

        }

        //8、删除线索备注
        for (ClueRemark clueRemark : clueRemarkList){
            int count8 = clueRemarkDao.delect(clueRemark);
            if (count8 != 1){
                flag = false;
            }
        }

        //9、删除线索备注
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){

            int count9 = clueActivityRelationDao.delete(clueActivityRelation);

            if (count9 != 1){
                flag = false;
            }
        }

        //10、删除线索
        int count10 = clueDao.delete(clueId);
        if (count10 != 1){
            flag = false;
        }


        return flag;
    }


}
