package org.example;

import lombok.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


class OrderDaoImpl implements OrderDao {

    @Override
    public Order GetOrder(String id) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Orders WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getString("id"),
                        rs.getTimestamp("date"),
                        rs.getInt("amount"),
                        new CustomerDaoImpl().getCustomer(rs.getString("customer_id")) // Assuming Customer is another class
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> GetAllOrders() {
            String query = "SELECT * FROM Orders";
            List<Order> orders = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                orders.add(new Order(
                        rs.getString("id"),
                        rs.getTimestamp("date"),
                        rs.getInt("amount"),
                        new CustomerDaoImpl().getCustomer(rs.getString("customer_id"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void AddOrder(Order order) {
        String query = "INSERT INTO Orders (id, date, amount, customer_id) VALUES (?, ?, ?, ?)";

        try  {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, order.getId());
            stmt.setTimestamp(2, order.getDate());
            stmt.setInt(3, order.getAmount());
            stmt.setString(4, order.getCustomer().getId()); // Assuming Customer has a getId method
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;
    private Timestamp date;
    private int amount;
    private Customer customer;

}
