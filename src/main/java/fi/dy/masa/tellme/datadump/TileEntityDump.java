package fi.dy.masa.tellme.datadump;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import fi.dy.masa.tellme.TellMe;

public class TileEntityDump extends DataDump
{
    private static final Field field_REGISTRY = ReflectionHelper.findField(TileEntity.class, "field_190562_f", "REGISTRY");

    private TileEntityDump()
    {
        super(2);
    }

    public static List<String> getFormattedTileEntityDump()
    {
        TileEntityDump tileEntityDump = new TileEntityDump();
        try
        {
            @SuppressWarnings("unchecked")
            RegistryNamespaced <ResourceLocation, Class <? extends TileEntity>> registry = (RegistryNamespaced <ResourceLocation, Class <? extends TileEntity>>) field_REGISTRY.get(null);
            Set<ResourceLocation> keys = registry.getKeys();

            for (ResourceLocation key : keys)
            {
                Class <? extends TileEntity> clazz = registry.getObject(key);
                tileEntityDump.addData(clazz.getName(), key.toString());
            }

            tileEntityDump.addTitle("Class", "Registry name");
            tileEntityDump.setUseColumnSeparator(true);
        }
        catch (Exception e)
        {
            TellMe.logger.warn("Failed to dump the TileEntity map");
        }

        return tileEntityDump.getLines();
    }
}
