package fi.dy.masa.tellme.event;

import net.minecraft.init.Items;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fi.dy.masa.tellme.util.BlockInfo;
import fi.dy.masa.tellme.util.EntityInfo;
import fi.dy.masa.tellme.util.ItemInfo;

public class InteractEventHandler
{
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.world.isRemote == true)
        {
            return;
        }

        if (event.entityPlayer != null && event.entityPlayer.getCurrentEquippedItem() != null
            && event.entityPlayer.getCurrentEquippedItem().getItem() == Items.gold_nugget)
        {
            // Show info for the block the player right clicks on with a gold nugget
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            {
                BlockInfo.printBasicBlockInfoToChat(event.entityPlayer, event.world, event.x, event.y, event.z);

                if (event.entityPlayer.isSneaking() == true)
                {
                    BlockInfo.dumpBlockInfoToFile(event.entityPlayer, event.world, event.x, event.y, event.z);
                }
                else
                {
                    BlockInfo.printBlockInfoToConsole(event.entityPlayer, event.world, event.x, event.y, event.z);
                }
            }
            // Show info for the item to the right from the current slot when the player right clicks on air with a gold nugget
            else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
            {
                // Select the slot to the right from the current slot, or the first slot if the current slot is the last slot
                int slot = event.entityPlayer.inventory.currentItem;
                if (slot >= 0 && slot <= 7)
                {
                    slot++;
                }
                else if (slot == 8)
                {
                    slot = 0;
                }
                else
                {
                    return;
                }

                if (event.entityPlayer.inventory.getStackInSlot(slot) == null)
                {
                    return;
                }

                ItemInfo.printBasicItemInfoToChat(event.entityPlayer, slot);

                if (event.entityPlayer.isSneaking() == true)
                {
                    ItemInfo.dumpItemInfoToFile(event.entityPlayer, slot);
                }
                else
                {
                    ItemInfo.printItemInfoToConsole(event.entityPlayer, slot);
                }
            }

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event)
    {
        if (event.entityPlayer.worldObj.isRemote == true)
        {
            return;
        }

        if (event.entityPlayer != null && event.entityPlayer.getCurrentEquippedItem() != null
            && event.target != null && event.entityPlayer.getCurrentEquippedItem().getItem() == Items.gold_nugget)
        {
            EntityInfo.printBasicEntityInfoToChat(event.entityPlayer, event.target);

            if (event.entityPlayer.isSneaking() == true)
            {
                EntityInfo.dumpEntityInfoToFile(event.entityPlayer, event.target);
            }
            else
            {
                EntityInfo.printEntityInfoToConsole(event.entityPlayer, event.target);
            }

            event.setCanceled(true);
        }
    }
}