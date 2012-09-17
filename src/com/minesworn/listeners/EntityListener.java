package com.minesworn.listeners;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.detectors.AutoAttack;
import com.minesworn.detectors.AutoClicker;
import com.minesworn.detectors.CombatLogDetector;
import com.minesworn.detectors.FactionBetrayalDetector;

public class EntityListener implements Listener {

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;
				
		if (e.getDamager() instanceof Player) {
			Player damager = (Player) e.getDamager();
			AutoAttack.check(damager, e.getEntity());
			if (AutoClicker.check(damager)) {
				e.setCancelled(true);
				return;
			}
			
			if (e.getEntity() instanceof Player) {
				Player player = (Player) e.getEntity();
				FactionBetrayalDetector.checkFactionBetrayal(player, e.getDamage(), damager);
				
				if (player.getHealth() - e.getDamage() < 0) {
					PlayerInfo i = PlayerDatabase.getPlayer(damager.getName());
					if (System.currentTimeMillis() - i.getLastPlayerKill() > 300L) {
						i.setLastPlayerKill(System.currentTimeMillis());
						i.setPlayerKills(i.getPlayerKills() + 1);
					}
				}
				
			} else if (e.getEntity() instanceof Monster) {
				Monster mob = (Monster) e.getEntity();
				if (mob.getHealth() - e.getDamage() < 0) {
					PlayerInfo i = PlayerDatabase.getPlayer(damager.getName());
					if (System.currentTimeMillis() - i.getLastMobKill() > 300L) {
						i.setLastMobKill(System.currentTimeMillis());
						i.setMobKills(i.getMobKills() + 1);
					}
				}
			} else if (e.getEntity() instanceof Animals) {
				Animals mob = (Animals) e.getEntity();
				if (mob.getHealth() - e.getDamage() < 0) {
					PlayerInfo i = PlayerDatabase.getPlayer(damager.getName());
					if (System.currentTimeMillis() - i.getLastAnimalKill() > 300L) {
						i.setLastAnimalKill(System.currentTimeMillis());
						i.setAnimalKills(i.getAnimalKills() + 1);
					}
				}
			}
		}
		
		if (e.getEntity() instanceof Player) {
			CombatLogDetector.addAttacked((Player) e.getEntity(), e.getDamager());
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		PlayerInfo i = PlayerDatabase.getPlayer(e.getEntity().getName());
		i.setLastAttacked(0);
	}
	
}
