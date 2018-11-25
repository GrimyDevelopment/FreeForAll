package server.wenjapvp.database.manager;

import lombok.Getter;

import org.bson.Document;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import server.wenjapvp.FreeForAll;
import server.wenjapvp.database.Profile;

import java.util.*;

public class ProfileManager implements Listener {

    public ProfileManager() {
        Bukkit.getPluginManager().registerEvents(this, FreeForAll.getInstance());
    }

    @Getter
    private Map<UUID, Profile> profiles = new HashMap<>();

    public Profile getProfile(UUID uniqueId) {
        return profiles.get(uniqueId);
    }

    public List<Profile> getNotCachedProfiles() {
        List<Profile> toReturn = new ArrayList<>();

        for (Object object : FreeForAll.getInstance().getProfilesCollection().find()) {
            Document document = (Document) object;

            toReturn.add(new Profile((UUID) document.get("_id")));
        }

        return toReturn;
    }

    public Profile getNotCachedProfile(UUID uniqueId) {
        for (Object object : FreeForAll.getInstance().getProfilesCollection().find()) {
            Document document = (Document) object;

            if (document.get("_id").equals(uniqueId)) {
                return new Profile(uniqueId);
            }
        }

        return null;
    }

    public Profile getProfile(Player player) {
        return getProfile(player.getUniqueId());
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        Profile profile = new Profile(event.getUniqueId());

        if (profile.getName() == null) {
            profile.setName(event.getName());
        }

        profile.save();
        profiles.put(event.getUniqueId(), profile);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(FreeForAll.getInstance(), () -> getProfile(event.getPlayer().getUniqueId()).save());
    }

    public void saveProfiles() {
        profiles.values().forEach(Profile::save);
    }
}
