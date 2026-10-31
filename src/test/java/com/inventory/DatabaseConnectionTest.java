package com.inventory;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void shouldConnectToDb() throws Exception{
        try (Connection connection = DatabaseConnection.getConnection()){
            assertTrue(connection.isValid(2));
        }
    }
}
