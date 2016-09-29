package fi.dy.masa.tellme.proxy;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.ClientCommandHandler;
import fi.dy.masa.tellme.TellMe;
import fi.dy.masa.tellme.command.ClientCommandTellme;
import fi.dy.masa.tellme.util.GameObjectData;

public class ClientProxy extends CommonProxy
{
    @Override
    public void getCurrentBiomeInfoClientSide(EntityPlayer player, Biome bgb)
    {
        BlockPos pos = player.getPosition();
        String pre = TextFormatting.YELLOW.toString();
        String rst = TextFormatting.RESET.toString() + TextFormatting.WHITE.toString();

        // These are client-side only:
        player.addChatMessage(new TextComponentString(String.format("Grass color: %s0x%08X (%d)%s",
                pre, bgb.getModdedBiomeGrassColor(bgb.getGrassColorAtPos(pos)), bgb.getModdedBiomeGrassColor(bgb.getGrassColorAtPos(pos)), rst)));
        player.addChatMessage(new TextComponentString(String.format("Foliage color: %s0x%08X (%d)%s",
                pre, bgb.getModdedBiomeFoliageColor(bgb.getFoliageColorAtPos(pos)), bgb.getModdedBiomeFoliageColor(bgb.getFoliageColorAtPos(pos)), rst)));
    }

    @Override
    public void getBlockSubtypes(List<GameObjectData> list, Block block, ResourceLocation rl, int id)
    {
        CreativeTabs tab = block.getCreativeTabToDisplayOn();
        Item item = Item.getItemFromBlock(block);

        if (item != null)
        {
            List<ItemStack> stacks = new ArrayList<ItemStack>();
            block.getSubBlocks(item, tab, stacks);
            boolean subtypes = stacks.size() > 1;

            for (ItemStack stack : stacks)
            {
                list.add(new GameObjectData(rl, id, stack.getMetadata(), block, subtypes, stack));
            }
        }
        else
        {
            list.add(new GameObjectData(rl, id, block));
        }
    }

    @Override
    public void getItemSubtypes(List<GameObjectData> list, Item item, ResourceLocation rl, int id)
    {
        if (item.getHasSubtypes())
        {
            for (CreativeTabs tab : item.getCreativeTabs())
            {
                List<ItemStack> stacks = new ArrayList<ItemStack>();
                item.getSubItems(item, tab, stacks);

                for (ItemStack stack : stacks)
                {
                    list.add(new GameObjectData(rl, id, stack.getMetadata(), item, true, stack));
                }
            }
        }
        else
        {
            list.add(new GameObjectData(rl, id, 0, item, false, new ItemStack(item, 1, 0)));
        }
    }

    @Override
    public void registerClientCommand()
    {
        TellMe.logger.info("Registering the client-side command");
        ClientCommandHandler.instance.registerCommand(new ClientCommandTellme());
    }
}