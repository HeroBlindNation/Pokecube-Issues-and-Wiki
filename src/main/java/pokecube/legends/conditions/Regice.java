package pokecube.legends.conditions;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import pokecube.core.database.Database;
import pokecube.core.database.PokedexEntry;
import pokecube.core.database.stats.CaptureStats;
import pokecube.core.interfaces.IPokemob;
import thut.api.maths.Vector3;

public class Regice extends Condition
{
    @Override
    public boolean canCapture(final Entity trainer, final IPokemob pokemon)
    {
        if (!this.canCapture(trainer)) return false;
        final boolean relicanth = CaptureStats.getTotalNumberOfPokemobCaughtBy(trainer.getUniqueID(), Database.getEntry(
                "relicanth")) > 0;
        final boolean wailord = CaptureStats.getTotalNumberOfPokemobCaughtBy(trainer.getUniqueID(), Database.getEntry(
                "wailord")) > 0;

        final String name = "Wailord, Relicanth";

        if (relicanth && wailord) return true;
        if (pokemon != null && !trainer.getEntityWorld().isRemote)
        {
            this.sendNoTrust(trainer);
            this.sendLegendExtra(trainer, name);
        }
        return false;
    }

    @Override
    public void onSpawn(IPokemob mob)
    {
        mob = mob.setForSpawn(54500);
        final Vector3 location = Vector3.getNewVector().set(mob.getEntity()).add(0, -1, 0);
        final ArrayList<Vector3> locations = new ArrayList<>();
        final World world = mob.getEntity().getEntityWorld();
        locations.add(location.add(0, -1, 0));
        locations.add(location.add(0, -2, 0));
        locations.add(location.add(1, -1, 0));
        locations.add(location.add(-1, -1, 0));
        locations.add(location.add(0, -1, -1));
        locations.add(location.add(0, -1, 1));
        for (final Vector3 v : locations)
            v.setAir(world);
        location.setAir(world);
    }

    @Override
    public boolean canSpawn(final Entity trainer, final Vector3 location, final boolean message)
    {
        if (!super.canSpawn(trainer, location, message)) return false;

        final ArrayList<Vector3> locations = new ArrayList<>();
        boolean check = false;
        final World world = trainer.getEntityWorld();

        locations.add(location.add(0, -1, 0));
        locations.add(location.add(0, -2, 0));
        locations.add(location.add(-1, -1, 0));
        locations.add(location.add(1, -1, 0));
        check = Condition.isBlock(world, locations, Blocks.ICE) || Condition.isBlock(world, locations,
                Blocks.PACKED_ICE);
        if (check)
        {
            Block b = location.add(0, -1, 1).getBlock(world);
            check = b == Blocks.ICE || b == Blocks.PACKED_ICE;
            if (!check)
            {
                b = location.add(0, -1, -1).getBlock(world);
                check = b == Blocks.ICE || b == Blocks.PACKED_ICE;
            }
        }
        else
        {
            locations.clear();
            locations.add(location.add(0, -1, 0));
            locations.add(location.add(0, -2, 0));
            locations.add(location.add(0, -1, 1));
            locations.add(location.add(0, -1, -1));
            check = Condition.isBlock(world, locations, Blocks.ICE) || Condition.isBlock(world, locations,
                    Blocks.PACKED_ICE);
            if (check)
            {
                Block b = location.add(1, -1, 0).getBlock(world);
                check = b == Blocks.ICE || b == Blocks.PACKED_ICE;
                if (!check)
                {
                    b = location.add(-1, -1, 0).getBlock(world);
                    check = b == Blocks.ICE || b == Blocks.PACKED_ICE;
                }
            }
        }
        if (!check)
        {
            if (message) trainer.sendMessage(new TranslationTextComponent("msg.reginotlookright.txt"));
            return false;
        }
        return true;
    }

    @Override
    public PokedexEntry getEntry()
    {
        return Database.getEntry("regice");
    }

}
