package com.box.sdk;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Box metadata template field.
 */
public class MetadataField extends BoxJSONObject {
    private String type;
    private String key;
    private String displayName;
    private List<String> options;

    /**
     * Constructs an empty MetadataField.
     */
    public MetadataField() { }

    /**
     * Constructs a MetadataField from a JSON string.
     * @param  json the json encoded metadata field.
     */
    public MetadataField(String json) {
        super(json);
    }

    MetadataField(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * Gets the type for the metadata field. Can be one of string, float, date or enum.
     * @return the type for the metadata field
     */
    public String getType() {
        return this.type;
    }

    /**
     * Gets the key for the metadata field.
     * @return the key for the metadata field
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Gets the display name for the metadata field.
     * @return the display name for the metadata field
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Gets the list of options for the metadata field. Only populated if the field type is enum.
     * @return the list of options for the metadata field
     */
    public List<String> getOptions() {
        return this.options;
    }

    @Override
    void parseJSONMember(JsonObject.Member member) {
        JsonValue value = member.getValue();
        String memberName = member.getName();
        if (memberName.equals("type")) {
            this.type = value.asString();
        } else if (memberName.equals("key")) {
            this.key = value.asString();
        } else if (memberName.equals("displayName")) {
            this.displayName = value.asString();
        } else if (memberName.equals("options")) {
            this.options = this.parseOptionsCollection(value.asArray());
        }
    }

    private List<String> parseOptionsCollection(JsonArray entries) {
        List<String> options = new ArrayList<String>(entries.size());
        for (JsonValue value : entries) {
            JsonObject entry = value.asObject();
            String key = entry.get("key").asString();
            options.add(key);
        }

        return options;
    }
}