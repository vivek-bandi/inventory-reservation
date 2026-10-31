package com.inventory;

import java.sql.Connection;
import  java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReservationRepository {
    public int getAvailableStock(Connection connection, long productId) throws SQLException{
        String sql = """
                SELECT available_stock
                FROM inventory
                WHERE product_id = ?
                """;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,productId);

            try(var result = statement.executeQuery()){
                if (result.next()){
                    return result.getInt("available_stock");
                }
                throw new SQLException("Product not found");
            }
        }
    }

    public void decreaseStock(Connection connection, long productId) throws SQLException{
        String sql = """
                INSERT INTO reservation(product_id,quantity) VALUES(?,1)
                """;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,productId);
            statement.executeUpdate();
        }
    }


    public void createReservation(Connection connection, long productId) throws SQLException{
        String sql = """
                UPDATE inventory SET available_stock = available_stock-1 WHERE product_id = ?
                """;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,productId);
            statement.executeUpdate();
        }
    }
}
