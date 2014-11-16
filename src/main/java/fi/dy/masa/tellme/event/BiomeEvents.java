package fi.dy.masa.tellme.event;

import net.minecraftforge.event.terraingen.WorldTypeEvent.BiomeSize;
import net.minecraftforge.event.terraingen.WorldTypeEvent.InitBiomeGens;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fi.dy.masa.tellme.TellMe;

public class BiomeEvents
{
    @SubscribeEvent
    public void onInitBiomeGens(InitBiomeGens event)
    {
        TellMe.logger.info("InitBiomeGens: seed: " + event.seed);

        if (event.worldType != null)
        {
            TellMe.logger.info("InitBiomeGens: worldType: " + event.worldType.toString());
        }
        else
        {
            TellMe.logger.info("InitBiomeGens: worldType: null");
        }

        if (event.originalBiomeGens != null)
        {
            TellMe.logger.info("InitBiomeGens: event.originalBiomeGens.length: " + event.originalBiomeGens.length);
        }
        else
        {
            TellMe.logger.info("InitBiomeGens: event.originalBiomeGens: null");
        }
    }

    @SubscribeEvent
    public void onBiomeSize(BiomeSize event)
    {
        TellMe.logger.info("BiomeSize: size: " + event.originalSize);
    }
}