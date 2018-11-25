package server.wenjapvp.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;
import server.wenjapvp.FreeForAll;

import java.util.*;

@Getter
public class Profile {
    private UUID uniqueId;

    @Setter private String name, killedInventory, killedLocation;
    @Setter private int kills, deaths;

    public Profile(UUID uniqueId) {
        this.uniqueId = uniqueId;

        load();
    }

    public void load() {
        Document document = (Document) FreeForAll.getInstance().getProfilesCollection().find(Filters.eq("_id", uniqueId)).first();

        if (document == null) return;

        name = document.getString("name");
        kills = document.getInteger("kills");
        deaths = document.getInteger("deaths");
    }

    public void save() {
        Document document = new Document("_id", uniqueId);

        // The "_id", uniqueId means that the id of the document will be the uuid of the player...
        // You first initialize a new document.

        document.put("name", name);
        document.put("kills", kills);
        document.put("deaths", deaths);

        // Now you put the values into the document. (each field has a value)

        Bson filter = Filters.eq("_id", uniqueId);
        FindIterable iterable = FreeForAll.getInstance().getProfilesCollection().find(filter);

        // You now search for documents with id == player#getUniqueId(), for that we use the find() feature

        // If no document was found with that id, we insert a new one..
        if (iterable.first() == null) {
            FreeForAll.getInstance().getProfilesCollection().insertOne(document);
        } else {
            // Else, if the document is found, we replace it.
            FreeForAll.getInstance().getProfilesCollection().replaceOne(filter, document);
        }

        //And, done!
    }
}
