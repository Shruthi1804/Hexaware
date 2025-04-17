package Test;

import dao.AssetManagementService;
import dao.AssetManagementServiceImpl;
import Entity.Asset;
import exception.AssetNotFoundException;
import exception.AssetNotMaintainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.text.SimpleDateFormat;
import java.util.Date;



@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssetManagementServiceTest {

    private static AssetManagementService service;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeAll
    public static void init() {
        service = new AssetManagementServiceImpl();
    }

    @Test
    @Order(1)
    public void testAddAssetSuccessfully() throws Exception {
        Date purchaseDate = sdf.parse("2024-01-01");
        Asset asset = new Asset(0, "Test Projector", "Electronics", "SN-8888", purchaseDate,
                "Lab Room", "in use", 1); 

        boolean added = service.addAsset(asset);
        assertTrue(added, "Asset should be added successfully");
    }
}
