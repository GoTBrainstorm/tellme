package fi.dy.masa.tellme.datadump;

import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import fi.dy.masa.tellme.util.datadump.DataDump;
import fi.dy.masa.tellme.util.datadump.DataDump.Format;

public class SimpleVanillaRegistryKeyOnlyDump
{
    public static List<String> getFormattedDump(Format format, Registry<?> registry)
    {
        DataDump dump = new DataDump(1, format);

        for (ResourceLocation key : registry.keySet())
        {
            dump.addData(key.toString());
        }

        dump.addTitle("Registry name");

        return dump.getLines();
    }
}
