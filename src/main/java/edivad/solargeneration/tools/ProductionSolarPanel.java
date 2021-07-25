package edivad.solargeneration.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class ProductionSolarPanel {

    public static float computeSunIntensity(Level world, BlockPos pos, SolarPanelLevel solarPanelLevel)
    {
        float sunIntensity = 0;

        if(world.canSeeSkyFromBelowWater(pos))
        {
            float multiplicator = 1.5f;
            float displacement = 1.2f;
            // Celestial angle == 0 at zenith.
            float celestialAngleRadians = world.getSunAngle(1.0f);
            if(celestialAngleRadians > Math.PI)
            {
                celestialAngleRadians = (2 * 3.141592f - celestialAngleRadians);
            }

            sunIntensity = multiplicator * Mth.cos(celestialAngleRadians / displacement);
            sunIntensity = Math.max(0, sunIntensity);
            sunIntensity = Math.min(1, sunIntensity);

            if(sunIntensity > 0)
            {
                if(solarPanelLevel == SolarPanelLevel.LEADSTONE)
                    sunIntensity = 1;

                if(world.isRaining())
                    sunIntensity *= 0.4;

                if(world.isThundering())
                    sunIntensity *= 0.2;
            }
        }

        return sunIntensity;
    }
}
