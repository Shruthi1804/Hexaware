package dao;

import Entity.Asset;
import util.DBConnUtil;
import exception.AssetNotFoundException;
import exception.AssetNotMaintainException;

import java.sql.*;

public class AssetManagementServiceImpl implements AssetManagementService {

    private Connection conn;

    public AssetManagementServiceImpl() {
        conn = DBConnUtil.getConnection();
    }
    @Override
    public boolean addEmployee(String name, String department, String email, String password) {
        String sql = "INSERT INTO employees(name, department, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, department);
            stmt.setString(3, email);
            stmt.setString(4, password);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addAsset(Asset asset) {
        String sql = "INSERT INTO assets(name, type, serial_number, purchase_date, location, status, owner_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, asset.getName());
            stmt.setString(2, asset.getType());
            stmt.setString(3, asset.getSerialNumber());
            stmt.setDate(4, new java.sql.Date(asset.getPurchaseDate().getTime()));
            stmt.setString(5, asset.getLocation());
            stmt.setString(6, asset.getStatus());
            stmt.setInt(7, asset.getOwnerId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding asset: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateAsset(Asset asset) throws AssetNotFoundException {
        String sql = "UPDATE assets SET name=?, type=?, serial_number=?, purchase_date=?, " +
                     "location=?, status=?, owner_id=? WHERE asset_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, asset.getName());
            stmt.setString(2, asset.getType());
            stmt.setString(3, asset.getSerialNumber());
            stmt.setDate(4, new java.sql.Date(asset.getPurchaseDate().getTime()));
            stmt.setString(5, asset.getLocation());
            stmt.setString(6, asset.getStatus());
            stmt.setInt(7, asset.getOwnerId());
            stmt.setInt(8, asset.getAssetId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new AssetNotFoundException("Asset with ID " + asset.getAssetId() + " not found for update.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating asset: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAsset(int assetId) throws AssetNotFoundException {
        String sql = "DELETE FROM assets WHERE asset_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, assetId);
            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new AssetNotFoundException("Asset ID " + assetId + " not found.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting asset: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean allocateAsset(int assetId, int employeeId, String allocationDate) throws AssetNotFoundException {
        // Check if asset exists
        if (!assetExists(assetId)) {
            throw new AssetNotFoundException("Cannot allocate: Asset ID " + assetId + " does not exist.");
        }

        String sql = "INSERT INTO asset_allocations(asset_id, employee_id, allocation_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, assetId);
            stmt.setInt(2, employeeId);
            stmt.setDate(3, Date.valueOf(allocationDate));
       

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error allocating asset: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deallocateAsset(int assetId, int employeeId, String returnDate) throws AssetNotFoundException {
        String sql = "UPDATE asset_allocations SET return_date=? WHERE asset_id=? AND employee_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setInt(2, assetId);
            stmt.setInt(3, employeeId);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new AssetNotFoundException("Deallocation failed: Asset ID " + assetId + " with Employee ID " + employeeId + " not found.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error deallocating asset: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost) throws AssetNotMaintainException {
  
        if (cost <= 0 || description == null || description.isEmpty()) {
            throw new AssetNotMaintainException("Invalid maintenance details: Cost or description missing.");
        }

        String sql = "INSERT INTO maintenance_records(asset_id, maintenance_date, description, cost) " +
                     "VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, assetId);
            stmt.setDate(2, Date.valueOf(maintenanceDate));
            stmt.setString(3, description);
            stmt.setDouble(4, cost);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error performing maintenance: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean reserveAsset(int assetId, int employeeId, String reservationDate, String startDate, String endDate) throws AssetNotFoundException {
       
        if (!assetExists(assetId)) {
            throw new AssetNotFoundException("Reservation failed: Asset ID " + assetId + " not found.");
        }

        String sql = "INSERT INTO reservations(asset_id, employee_id, reservation_date, start_date, end_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, assetId);
            stmt.setInt(2, employeeId);
            stmt.setDate(3, Date.valueOf(reservationDate));
            stmt.setDate(4, Date.valueOf(startDate));
            stmt.setDate(5, Date.valueOf(endDate));
            stmt.setString(6, "pending");

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error reserving asset: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean withdrawReservation(int reservationId) throws AssetNotFoundException {
        String sql = "UPDATE reservations SET status='canceled' WHERE reservation_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new AssetNotFoundException("Reservation ID " + reservationId + " not found.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error withdrawing reservation: " + e.getMessage());
            return false;
        }
    }

    private boolean assetExists(int assetId) {
        String sql = "SELECT 1 FROM assets WHERE asset_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, assetId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}
