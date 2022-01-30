package ninjaphenix.chest_helmets.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public final class ItemInventory implements Inventory {
    private final ItemStack stack;
    private final int inventorySize;
    private final DefaultedList<ItemStack> items;

    public ItemInventory(ItemStack stack, int size) {
        this.stack = stack;
        inventorySize = size;
        items = DefaultedList.ofSize(size, ItemStack.EMPTY);
        if (stack.hasNbt()) {
            Inventories.readNbt(stack.getOrCreateNbt(), items);
        }
    }

    @Override
    public int size() {
        return inventorySize;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (stack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = Inventories.splitStack(items, slot, amount);
        if (!stack.isEmpty()) this.markDirty();
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(items, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (stack.getCount() > this.getMaxCountPerStack()) stack.setCount(this.getMaxCountPerStack());
        items.set(slot, stack);
        this.markDirty();
    }

    @Override
    public void markDirty() {
        Inventories.writeNbt(stack.getOrCreateNbt(), items);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public void onClose(PlayerEntity player) {
        this.markDirty();
    }
}
