package Main;

import dao.AssetManagementService;
import dao.AssetManagementServiceImpl;
import Entity.Asset;
import exception.AssetNotFoundException;
import exception.AssetNotMaintainException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AssetManagementApp {

    public static void main(String[] args) throws AssetNotFoundException, AssetNotMaintainException {
        Scanner scanner = new Scanner(System.in);
        AssetManagementService service = new AssetManagementServiceImpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        while (true) {
            System.out.println("\n=== Digital Asset Management Menu ===");
            System.out.println("0. Add Employee");
            System.out.println("1. Add Asset");
            System.out.println("2. Update Asset");
            System.out.println("3. Delete Asset");
            System.out.println("4. Allocate Asset");
            System.out.println("5. Deallocate Asset");
            System.out.println("6. Perform Maintenance");
            System.out.println("7. Reserve Asset");
            System.out.println("8. Withdraw Reservation");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            try {
                switch (choice) {
                case 0:
                    System.out.print("Employee Name: ");
                    String empName = scanner.nextLine();
                    System.out.print("Department: ");
                    String dept = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Password: ");
                    String pass = scanner.nextLine();

                    if (service.addEmployee(empName, dept, email, pass)) {
                        System.out.println("Employee added successfully.");
                    } else {
                        System.out.println("Failed to add employee.");
                    }
                    break;

                    case 1:
                        System.out.print("Asset Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Type: ");
                        String type = scanner.nextLine();
                        System.out.print("Serial Number: ");
                        String serial = scanner.nextLine();
                        System.out.print("Purchase Date (yyyy-MM-dd): ");
                        Date purchaseDate = sdf.parse(scanner.nextLine());
                        System.out.print("Location: ");
                        String location = scanner.nextLine();
                        System.out.print("Status: ");
                        String status = scanner.nextLine();
                        System.out.print("Owner ID: ");
                        int ownerId = scanner.nextInt();

                        Asset asset = new Asset(0, name, type, serial, purchaseDate, location, status, ownerId);
                        if (service.addAsset(asset)) {
                            System.out.println("Asset added successfully.");
                        } else {
                            System.out.println("Failed to add asset.");
                        }
                        break;

                    case 2:
                        System.out.print("Enter Asset ID to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        System.out.print("New Name: ");
                        name = scanner.nextLine();
                        System.out.print("New Type: ");
                        type = scanner.nextLine();
                        System.out.print("New Serial Number: ");
                        serial = scanner.nextLine();
                        System.out.print("New Purchase Date (yyyy-MM-dd): ");
                        purchaseDate = sdf.parse(scanner.nextLine());
                        System.out.print("New Location: ");
                        location = scanner.nextLine();
                        System.out.print("New Status: ");
                        status = scanner.nextLine();
                        System.out.print("New Owner ID: ");
                        ownerId = scanner.nextInt();

                        asset = new Asset(updateId, name, type, serial, purchaseDate, location, status, ownerId);
                        if (service.updateAsset(asset)) {
                            System.out.println("Asset updated successfully.");
                        } else {
                            System.out.println("Failed to update asset.");
                        }
                        break;

                    case 3:
                        System.out.print("Enter Asset ID to delete: ");
                        int deleteId = scanner.nextInt();
                        if (service.deleteAsset(deleteId)) {
                            System.out.println("Asset deleted.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter Asset ID: ");
                        int assetId = scanner.nextInt();
                        System.out.print("Enter Employee ID: ");
                        int empId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Allocation Date (yyyy-MM-dd): ");
                        String allocDate = scanner.nextLine();
                        if (service.allocateAsset(assetId, empId, allocDate)) {
                            System.out.println("Asset allocated.");
                        } else {
                            System.out.println("Allocation failed.");
                        }
                        break;

                    case 5:
                        System.out.print("Enter Asset ID: ");
                        assetId = scanner.nextInt();
                        System.out.print("Enter Employee ID: ");
                        empId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Return Date (yyyy-MM-dd): ");
                        String returnDate = scanner.nextLine();
                        if (service.deallocateAsset(assetId, empId, returnDate)) {
                            System.out.println("Asset deallocated.");
                        } else {
                            System.out.println("Deallocation failed.");
                        }
                        break;

                    case 6:
                        System.out.print("Enter Asset ID: ");
                        assetId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Maintenance Date (yyyy-MM-dd): ");
                        String mDate = scanner.nextLine();
                        System.out.print("Description: ");
                        String desc = scanner.nextLine();
                        System.out.print("Cost: ");
                        double cost = scanner.nextDouble();
                        if (service.performMaintenance(assetId, mDate, desc, cost)) {
                            System.out.println("Maintenance recorded.");
                        } else {
                            System.out.println("Maintenance failed.");
                        }
                        break;

                    case 7:
                        System.out.print("Enter Asset ID: ");
                        assetId = scanner.nextInt();
                        System.out.print("Enter Employee ID: ");
                        empId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Reservation Date (yyyy-MM-dd): ");
                        String rDate = scanner.nextLine();
                        System.out.print("Start Date (yyyy-MM-dd): ");
                        String sDate = scanner.nextLine();
                        System.out.print("End Date (yyyy-MM-dd): ");
                        String eDate = scanner.nextLine();
                        if (service.reserveAsset(assetId, empId, rDate, sDate, eDate)) {
                            System.out.println("Asset reserved.");
                        } else {
                            System.out.println("Reservation failed.");
                        }
                        break;

                    case 8:
                        System.out.print("Enter Reservation ID: ");
                        int rid = scanner.nextInt();
                        if (service.withdrawReservation(rid)) {
                            System.out.println("Reservation withdrawn.");
                        } else {
                            System.out.println("Withdraw failed.");
                        }
                        break;

                    case 9:
                        System.out.println("Exiting application.");
                        scanner.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}
