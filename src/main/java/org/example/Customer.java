package org.example;


import lombok.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class CustomerDaoImpl implements CustomerDao {

    @Override
    public Customer getCustomer(String id) {
        Customer customer = null;
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("select * from customer where id = ?");
            stmt.setString(1,id );
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                customer = new Customer(
                        rs.getString("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                return customer;
            }else{
                return null;
            }

        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Customer> getCustomers() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("select * from customer");
            ResultSet rs = stmt.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while(rs.next()){
                Customer customer = new Customer(
                        rs.getString("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                customers.add(customer);

            }
            return customers;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    @Override
    public void saveCustomer(Customer customer) {
            try {
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement("insert into customer values(?,?,?,?)");
                stmt.setString(1,customer.getId());
                stmt.setString(2,customer.getNom());
                stmt.setString(3,customer.getEmail());
                stmt.setString(4,customer.getPhone());
                stmt.executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
            }
    }
}
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String id;
    private String nom;
    private String email;
    private String phone;
}
