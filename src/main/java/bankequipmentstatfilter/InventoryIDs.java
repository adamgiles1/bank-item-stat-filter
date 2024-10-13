package bankequipmentstatfilter;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.InventoryID;

import java.util.Arrays;
import java.util.function.Function;

@Slf4j
public enum InventoryIDs {
    BANK(InventoryID.BANK.getId()),
    // Checks both inventory and current equipment
    INVENTORY(InventoryID.INVENTORY.getId(), BankEquipmentStatFilterConfig::checkInventory),
    // Checks both inventory and current equipment
    EQUIPMENT(InventoryID.EQUIPMENT.getId(), BankEquipmentStatFilterConfig::checkEquipped);

    InventoryIDs(int inventoryId) {
        this(inventoryId, null);
    }

    InventoryIDs(int inventoryId, Function<BankEquipmentStatFilterConfig, Boolean> checkSettingsFunc) {
        this.inventoryId = inventoryId;
        this.checkSettingsFunc = checkSettingsFunc;
    }

    private static BankEquipmentStatFilterConfig config;

    private final int inventoryId;
    private final Function<BankEquipmentStatFilterConfig, Boolean> checkSettingsFunc;

    public int getId() {
        return inventoryId;
    }

    public boolean shouldUseInventory() {
        // If there is no check settings function, allow the items to be checked
        if (checkSettingsFunc == null) {
            return true;
        }

        return checkSettingsFunc.apply(config);
    }

    public static InventoryIDs getById(int id) {
        return Arrays.stream(values())
            .filter(inventoryID -> inventoryID.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public static void setConfig(BankEquipmentStatFilterConfig config) {
        InventoryIDs.config = config;
    }
}
