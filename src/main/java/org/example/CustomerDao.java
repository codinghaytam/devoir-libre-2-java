package org.example;

import java.util.List;

public interface CustomerDao {
    Customer getCustomer(String id);
    List<Customer> getCustomers();
    void saveCustomer(Customer customer);
}
