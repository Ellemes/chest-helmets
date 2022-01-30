package ninjaphenix.chest_helmets.items;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import ninjaphenix.chest_helmets.Main;
import ninjaphenix.chest_helmets.inventory.ChestScreenHandler;
import ninjaphenix.chest_helmets.inventory.ItemInventory;

public final class ChestHelmet extends ArmorItem {
    public ChestHelmet(ArmorMaterial material, Settings settings) {
        super(material, EquipmentSlot.HEAD, settings);
    }

    public static void openInventory(PlayerEntity player, ItemStack helmet) {
        player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return helmet.getName();
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new ChestScreenHandler(Main.INSTANCE.getChestScreenHandlerType(), syncId, inv, new ItemInventory(helmet, 27));
            }
        });
    }
}
