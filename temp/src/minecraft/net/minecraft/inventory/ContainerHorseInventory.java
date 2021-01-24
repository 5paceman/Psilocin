package net.minecraft.inventory;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory extends Container {
   private IInventory field_111243_a;
   private EntityHorse field_111242_f;

   public ContainerHorseInventory(IInventory p_i45791_1_, final IInventory p_i45791_2_, final EntityHorse p_i45791_3_, EntityPlayer p_i45791_4_) {
      this.field_111243_a = p_i45791_2_;
      this.field_111242_f = p_i45791_3_;
      int i = 3;
      p_i45791_2_.func_174889_b(p_i45791_4_);
      int j = (i - 4) * 18;
      this.func_75146_a(new Slot(p_i45791_2_, 0, 8, 18) {
         public boolean func_75214_a(ItemStack p_75214_1_) {
            return super.func_75214_a(p_75214_1_) && p_75214_1_.func_77973_b() == Items.field_151141_av && !this.func_75216_d();
         }
      });
      this.func_75146_a(new Slot(p_i45791_2_, 1, 8, 36) {
         public boolean func_75214_a(ItemStack p_75214_1_) {
            return super.func_75214_a(p_75214_1_) && p_i45791_3_.func_110259_cr() && EntityHorse.func_146085_a(p_75214_1_.func_77973_b());
         }

         public boolean func_111238_b() {
            return p_i45791_3_.func_110259_cr();
         }
      });
      if(p_i45791_3_.func_110261_ca()) {
         for(int k = 0; k < i; ++k) {
            for(int l = 0; l < 5; ++l) {
               this.func_75146_a(new Slot(p_i45791_2_, 2 + l + k * 5, 80 + l * 18, 18 + k * 18));
            }
         }
      }

      for(int i1 = 0; i1 < 3; ++i1) {
         for(int k1 = 0; k1 < 9; ++k1) {
            this.func_75146_a(new Slot(p_i45791_1_, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + j));
         }
      }

      for(int j1 = 0; j1 < 9; ++j1) {
         this.func_75146_a(new Slot(p_i45791_1_, j1, 8 + j1 * 18, 160 + j));
      }

   }

   public boolean func_75145_c(EntityPlayer p_75145_1_) {
      return this.field_111243_a.func_70300_a(p_75145_1_) && this.field_111242_f.func_70089_S() && this.field_111242_f.func_70032_d(p_75145_1_) < 8.0F;
   }

   public ItemStack func_82846_b(EntityPlayer p_82846_1_, int p_82846_2_) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.field_75151_b.get(p_82846_2_);
      if(slot != null && slot.func_75216_d()) {
         ItemStack itemstack1 = slot.func_75211_c();
         itemstack = itemstack1.func_77946_l();
         if(p_82846_2_ < this.field_111243_a.func_70302_i_()) {
            if(!this.func_75135_a(itemstack1, this.field_111243_a.func_70302_i_(), this.field_75151_b.size(), true)) {
               return null;
            }
         } else if(this.func_75139_a(1).func_75214_a(itemstack1) && !this.func_75139_a(1).func_75216_d()) {
            if(!this.func_75135_a(itemstack1, 1, 2, false)) {
               return null;
            }
         } else if(this.func_75139_a(0).func_75214_a(itemstack1)) {
            if(!this.func_75135_a(itemstack1, 0, 1, false)) {
               return null;
            }
         } else if(this.field_111243_a.func_70302_i_() <= 2 || !this.func_75135_a(itemstack1, 2, this.field_111243_a.func_70302_i_(), false)) {
            return null;
         }

         if(itemstack1.field_77994_a == 0) {
            slot.func_75215_d((ItemStack)null);
         } else {
            slot.func_75218_e();
         }
      }

      return itemstack;
   }

   public void func_75134_a(EntityPlayer p_75134_1_) {
      super.func_75134_a(p_75134_1_);
      this.field_111243_a.func_174886_c(p_75134_1_);
   }
}
