package pokecube.mobs.moves.attacks.special;

import net.minecraft.entity.LivingEntity;
import pokecube.core.PokecubeCore;
import pokecube.core.interfaces.pokemob.moves.MovePacket;
import pokecube.core.moves.PokemobDamageSource;
import pokecube.core.moves.templates.Move_Basic;

public class MoveBide extends Move_Basic
{

    public MoveBide()
    {
        super("bide");
    }

    @Override
    public void postAttack(final MovePacket packet)
    {
        super.postAttack(packet);
        if (packet.canceled || packet.failed) return;
        final LivingEntity attacker = packet.attacker.getEntity();
        if (!packet.attacker.getMoveStats().biding)
        {
            attacker.getPersistentData().putLong("bideTime", attacker.getEntityWorld().getGameTime() + PokecubeCore
                    .getConfig().attackCooldown * 5);
            packet.attacker.getMoveStats().biding = true;
            packet.attacker.getMoveStats().PHYSICALDAMAGETAKENCOUNTER = 0;
            packet.attacker.getMoveStats().SPECIALDAMAGETAKENCOUNTER = 0;
        }
        else if (attacker.getPersistentData().getLong("bideTime") < attacker.getEntityWorld().getGameTime())
        {
            attacker.getPersistentData().remove("bideTime");
            final int damage = packet.attacker.getMoveStats().PHYSICALDAMAGETAKENCOUNTER + packet.attacker
                    .getMoveStats().SPECIALDAMAGETAKENCOUNTER;
            packet.attacker.getMoveStats().PHYSICALDAMAGETAKENCOUNTER = 0;
            packet.attacker.getMoveStats().SPECIALDAMAGETAKENCOUNTER = 0;
            packet.attacked.attackEntityFrom(new PokemobDamageSource("mob", attacker, this), damage);
            packet.attacker.getMoveStats().biding = false;
        }
    }
}
