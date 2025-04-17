package Entity;

import java.util.Date;

public class AssetAllocation {
    private int allocationId;
    private int assetId;
    private int employeeId;
    private Date allocationDate;
    private Date returnDate;

    public AssetAllocation() {}

    public AssetAllocation(int allocationId, int assetId, int employeeId,
                           Date allocationDate, Date returnDate) {
        this.allocationId = allocationId;
        this.assetId = assetId;
        this.employeeId = employeeId;
        this.allocationDate = allocationDate;
        this.returnDate = returnDate;
    }

 
    public int getAllocationId() { return allocationId; }
    public void setAllocationId(int allocationId) { this.allocationId = allocationId; }

    public int getAssetId() { return assetId; }
    public void setAssetId(int assetId) { this.assetId = assetId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public Date getAllocationDate() { return allocationDate; }
    public void setAllocationDate(Date allocationDate) { this.allocationDate = allocationDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
}