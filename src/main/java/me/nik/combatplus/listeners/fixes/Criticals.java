package me.nik.combatplus.listeners.fixes;

import me.nik.combatplus.CombatPlus;
import me.nik.combatplus.api.Manager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

public class Criticals extends Manager {

    public Criticals(CombatPlus plugin) {
        super(plugin);
    }

    // Patches the Criticals Cheat if a player crits while he's on the ground

    @EventHandler(priority = EventPriority.LOW)
    public void onCritical(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        Player p = (Player) e.getDamager();
        Location pLoc = p.getLocation();
        if (isCritical(p)) {
            if ((pLoc.getY() % 1.0 == 0 || pLoc.getY() % 0.5 == 0) && pLoc.clone().subtract(0, 1.0, 0).getBlock().getType().isSolid()) {
                e.setCancelled(true);
                debug(p, "&3Criticals &f&l>> &6Canceled: &a" + e.isCancelled() + " &6Invalid: &atrue");
            }
        }
    }

    private boolean isCritical(Player p) {
        return p.getFallDistance() > 0.0f
                && !p.isOnGround()
                && !p.isInsideVehicle()
                && !p.hasPotionEffect(PotionEffectType.BLINDNESS)
                && !isAtWater(p.getLocation())
                && p.getEyeLocation().getBlock().getType() != Material.LADDER;
    }

    private boolean isAtWater(Location loc, int blocks) {
        for (int i = loc.getBlockY(); i > loc.getBlockY() - blocks; i--) {
            Block block = (new Location(loc.getWorld(), loc.getBlockX(), i, loc.getBlockZ())).getBlock();
            if (block.getType() != Material.AIR) {
                return block.isLiquid();
            }
        }
        return false;
    }

    private boolean isAtWater(Location loc) {
        return isAtWater(loc, 25);
    }
}
