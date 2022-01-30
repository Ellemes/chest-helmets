package ninjaphenix.chest_helmets.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3f;
import ninjaphenix.chest_helmets.items.ChestHelmet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ArmorFeatureRenderer.class)
public class ChestHelmetRender<T extends LivingEntity, A extends BipedEntityModel<T>> {

    @Inject(
            method = "renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;ILnet/minecraft/client/render/entity/model/BipedEntityModel;)V",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true
    )
    private void chest_helmets_renderChestHelmet(MatrixStack stack, VertexConsumerProvider provider, T entity, EquipmentSlot slot, int light, A model, CallbackInfo ci, ItemStack headStack) {
        if (headStack.getItem() instanceof ChestHelmet) {
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
            stack.scale(0.5f, 0.5f, 0.5f);
            stack.translate(0.0f, 1.5f, 0.0f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(Items.CHEST.getDefaultStack(), ModelTransformation.Mode.HEAD, light, OverlayTexture.DEFAULT_UV, stack, provider, 0);
            ci.cancel();
        }
    }
}
