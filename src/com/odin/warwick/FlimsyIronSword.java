package com.odin.warwick;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class FlimsyIronSword extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        ItemStack sword = new ItemStack( Material.DIAMOND_SWORD );
        ItemMeta im = sword.getItemMeta();
        im.setDisplayName( "Flimsy Iron Sword" );
        sword.setItemMeta( im );
        sword.addEnchantment( Enchantment.DAMAGE_ALL, 1 );
        ShapedRecipe recipe = new ShapedRecipe( sword );
        recipe.shape( " I ", " I ", " S " );
        recipe.setIngredient( 'I', Material.IRON_INGOT );
        recipe.setIngredient( 'S', Material.STICK );
        getServer().addRecipe( recipe );
    }
}