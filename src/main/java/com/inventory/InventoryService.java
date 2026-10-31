package com.inventory;

import java.sql.Connection;
import java.sql.SQLException;

public class InventoryService {
    private final ReservationRepository repository;

    public InventoryService(ReservationRepository repository){
        this.repository = repository;
    }

    public boolean reserve(Connection connection, long productId) throws SQLException {
        int availableStock = repository.getAvailableStock(connection,productId);

        if (availableStock < 1){
            return false;
        }
        repository.decreaseStock(connection,productId);

        repository.createReservation(connection,productId);

        return true;
    }
}
