package dao;

import Entity.Asset;
import exception.AssetNotFoundException;
import exception.AssetNotMaintainException;

public interface AssetManagementService {
	boolean addEmployee(String name, String department, String email, String password);
    boolean addAsset(Asset asset);
    boolean updateAsset(Asset asset) throws AssetNotFoundException;
    boolean deleteAsset(int assetId) throws AssetNotFoundException;
    boolean allocateAsset(int assetId, int employeeId, String allocationDate) throws AssetNotFoundException;
    boolean deallocateAsset(int assetId, int employeeId, String returnDate) throws AssetNotFoundException;
    boolean performMaintenance(int assetId, String maintenanceDate, String description, double cost) throws AssetNotMaintainException;
    boolean reserveAsset(int assetId, int employeeId, String reservationDate, String startDate, String endDate) throws AssetNotFoundException;
    boolean withdrawReservation(int reservationId) throws AssetNotFoundException;
}
