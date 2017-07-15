package fi.dy.masa.tellme.datadump;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import fi.dy.masa.tellme.TellMe;

public class BiomeDump extends DataDump
{
    private BiomeDump(Format format)
    {
        super(8, format);

        this.setSort(false);
    }

    public static List<String> getFormattedBiomeDump(Format format)
    {
        BiomeDump biomeDump = new BiomeDump(format);
        Iterator<Biome> iter = Biome.REGISTRY.iterator();

        while (iter.hasNext())
        {
            Biome biome = iter.next();
            String id = String.valueOf(Biome.getIdForBiome(biome));
            String regName = biome.getRegistryName().toString();
            String name = biome.getBiomeName();
            String waterColor = String.format("0x%08X (%10d)", biome.getWaterColorMultiplier(), biome.getWaterColorMultiplier());
            String temp = String.format("%5.2f", biome.getTemperature());
            String tempCat = biome.getTempCategory().toString();
            String rain = String.format("%.2f", biome.getRainfall());
            String snow = String.valueOf(biome.getEnableSnow());

            biomeDump.addData(id, regName, name, waterColor, temp, tempCat, rain, snow);
        }

        biomeDump.addTitle("ID", "Registry name", "Biome name",
                "waterColorMultiplier", "temperature", "temp. category", "rainfall", "enableSnow");

        biomeDump.setColumnProperties(0, Alignment.RIGHT, true); // id
        biomeDump.setColumnProperties(4, Alignment.RIGHT, true); // temperature
        biomeDump.setColumnProperties(6, Alignment.RIGHT, true); // rainfall
        biomeDump.setColumnAlignment(7, Alignment.RIGHT); // snow

        biomeDump.setUseColumnSeparator(true);

        return biomeDump.getLines();
    }

    public static void printCurrentBiomeInfoToChat(EntityPlayer player)
    {
        World world = player.getEntityWorld();
        BlockPos pos = player.getPosition();
        Biome biome = world.getBiome(pos);

        String pre = TextFormatting.GREEN.toString();
        String rst = TextFormatting.RESET.toString() + TextFormatting.WHITE.toString();

        player.sendMessage(new TextComponentString("------------- Current biome info ------------"));
        player.sendMessage(new TextComponentString(String.format("Name: %s%s%s - ID: %s%d%s - Registry name: %s%s%s",
                pre, biome.getBiomeName(), rst, pre, Biome.getIdForBiome(biome), rst, pre, biome.getRegistryName().toString(), rst)));
        player.sendMessage(new TextComponentString(String.format("canRain: %s%s%s, rainfall: %s%f%s - enableSnow: %s%s%s",
                pre, biome.canRain(), rst, pre, biome.getRainfall(), rst, pre, biome.getEnableSnow(), rst)));
        player.sendMessage(new TextComponentString(String.format("waterColorMultiplier: %s0x%08X (%d)%s",
                pre, biome.getWaterColorMultiplier(), biome.getWaterColorMultiplier(), rst)));
        player.sendMessage(new TextComponentString(String.format("temperature: %s%f%s, temp. category: %s%s%s",
                pre, biome.getFloatTemperature(pos), rst, pre, biome.getTempCategory(), rst)));

        // Get the grass and foliage colors, if called on the client side
        TellMe.proxy.getCurrentBiomeInfoClientSide(player, biome);
    }

    public static List<String> getBiomeDumpIdToName(Format format)
    {
        List<IdToStringHolder> data = new ArrayList<IdToStringHolder>();
        List<String> lines = new ArrayList<String>();
        Iterator<Biome> iter = Biome.REGISTRY.iterator();

        while (iter.hasNext())
        {
            Biome biome = iter.next();
            data.add(new IdToStringHolder(Biome.getIdForBiome(biome), biome.getBiomeName()));
        }

        Collections.sort(data);

        if (format == Format.ASCII)
        {
            for (IdToStringHolder holder : data)
            {
                lines.add(String.valueOf(holder.getId()) + " = " + holder.getString());
            }
        }
        else if (format == Format.CSV)
        {
            for (IdToStringHolder holder : data)
            {
                lines.add(String.valueOf(holder.getId()) + ",\"" + holder.getString() + "\"");
            }
        }

        return lines;
    }

    public static class IdToStringHolder implements Comparable<IdToStringHolder>
    {
        private final int id;
        private final String str;

        public IdToStringHolder(int id, String str)
        {
            this.id = id;
            this.str = str;
        }

        public int getId()
        {
            return this.id;
        }

        public String getString()
        {
            return this.str;
        }

        @Override
        public int compareTo(IdToStringHolder other)
        {
            if (this.id < other.id)
            {
                return -1;
            }
            else if (this.id > other.id)
            {
                return 1;
            }

            return 0;
        }
    }
}
