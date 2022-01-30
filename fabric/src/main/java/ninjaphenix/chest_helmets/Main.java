package ninjaphenix.chest_helmets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;
import ninjaphenix.chest_helmets.inventory.ChestScreen;
import ninjaphenix.chest_helmets.inventory.ChestScreenHandler;
import ninjaphenix.chest_helmets.items.ChestHelmet;

public final class Main implements ModInitializer {
    public static final Main INSTANCE = new Main();
    private ScreenHandlerType<ChestScreenHandler> chestScreenHandlerType;

    private Main() {

    }

    @Override
    public void onInitialize() {
        var item = new ChestHelmet(ArmorMaterials.LEATHER, new Item.Settings().maxCount(1));
        Registry.register(Registry.ITEM, Utils.id("wooden_helmet"), item);
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player.getStackInHand(hand) == ItemStack.EMPTY && !world.isClient()) {
                if (entity instanceof LivingEntity livingEntity) {
                    ItemStack headStack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
                    if (headStack.getItem() instanceof ChestHelmet) {
                        System.out.println(hitResult.getPos());
                        System.out.println(hitResult.getEntity().getPos());
                        ChestHelmet.openInventory(player, headStack);
                        return ActionResult.SUCCESS;
                    }
                }
            }
            return ActionResult.PASS;
        });

        chestScreenHandlerType = ScreenHandlerRegistry.registerSimple(Utils.id("chest"), (syncId, inventory) ->
                new ChestScreenHandler(chestScreenHandlerType, syncId, inventory, new SimpleInventory(27)));
        if (FabricLoaderImpl.INSTANCE.getEnvironmentType() == EnvType.CLIENT) {
            ScreenRegistry.register(chestScreenHandlerType, ChestScreen::new);
        }
    }

    public ScreenHandlerType<ChestScreenHandler> getChestScreenHandlerType() {
        return chestScreenHandlerType;
    }
}
