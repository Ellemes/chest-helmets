package ninjaphenix.chest_helmets.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class ChestScreenHandler extends ScreenHandler {
    public ChestScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(type, syncId);
        inventory.onOpen(playerInventory.player);
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9, 8 + x * 18, 18 + y * 18));
            }
        }
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 85 + y * 18));
            }
        }
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 143));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
