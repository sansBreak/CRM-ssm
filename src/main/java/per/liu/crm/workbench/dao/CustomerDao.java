package per.liu.crm.workbench.dao;

import per.liu.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);
}
