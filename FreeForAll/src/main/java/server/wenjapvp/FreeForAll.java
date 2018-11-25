package server.wenjapvp;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import server.wenjapvp.database.manager.ProfileManager;

import java.util.Arrays;

@Getter
@Setter
public class FreeForAll extends JavaPlugin implements Listener {

    @Getter private static FreeForAll instance;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private MongoCollection profilesCollection;
    private ProfileManager profileManager;

    private MongoCollection informationCollection;

    public void onEnable() {
        instance = this;

        mongoClient = new MongoClient("127.0.0.1", 27017);
        mongoDatabase = mongoClient.getDatabase("FreeForAll");

        profilesCollection = mongoDatabase.getCollection("Profiles");
        profileManager = new ProfileManager();

    }

    public void onDisable() {
        instance = null;
        getProfileManager().saveProfiles();
    }

    public void registerListeners(Listener... listener) {
        Arrays.stream(listener).forEach(event -> Bukkit.getPluginManager().registerEvents(event, this));
    }
}
