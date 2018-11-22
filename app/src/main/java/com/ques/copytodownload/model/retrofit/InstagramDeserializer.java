package com.ques.copytodownload.model.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ques.copytodownload.model.InstagramMedia;

import java.lang.reflect.Type;

/**
 * Created by jeong on 22/11/2018.
 */

public class InstagramDeserializer implements JsonDeserializer<InstagramMedia> {

    @Override
    public InstagramMedia deserialize(JsonElement element, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        JsonElement content = extractRootElement(element);
        content = addTitleToNode(content);
        content = addNodesForAdditionalImages(content);

        return new Gson().fromJson(content, InstagramMedia.class);
    }

    private JsonElement extractRootElement(JsonElement element) {
        return element.getAsJsonObject().getAsJsonObject("graphql").get("shortcode_media");
    }

    private JsonElement addTitleToNode(JsonElement content) {
        String title = content.getAsJsonObject()
                .getAsJsonObject("edge_media_to_caption")
                .getAsJsonArray("edges").get(0).getAsJsonObject()
                .getAsJsonObject("node").get("text").getAsString();
        content.getAsJsonObject().addProperty("title", title);

        return content;
    }

    private JsonElement addNodesForAdditionalImages(JsonElement content) {
        if (content.getAsJsonObject().has("edge_sidecar_to_children")) {
            JsonArray edges = content.getAsJsonObject()
                    .getAsJsonObject("edge_sidecar_to_children")
                    .getAsJsonArray("edges");
            JsonArray nodes = new JsonArray();
            String title = content.getAsJsonObject().get("title").getAsString();

            edges.iterator().forEachRemaining(jsonElement -> {
                JsonElement node = jsonElement.getAsJsonObject().get("node");
                String id = node.getAsJsonObject().get("id").getAsString();
                node.getAsJsonObject().addProperty("title", String.format("%s-%s", title, id));
                nodes.add(node);
            });
            content.getAsJsonObject().add("other_images", nodes);
        }

        return content;
    }
}
