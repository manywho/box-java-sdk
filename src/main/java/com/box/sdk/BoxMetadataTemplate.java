package com.box.sdk;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an metadata template on Box. This class can be used to list and display templates inside an enterprise.
 *
 * <p>Unless otherwise noted, the methods in this class can throw an unchecked {@link BoxAPIException} (unchecked
 * meaning that the compiler won't force you to handle it) if an error occurs. If you wish to implement custom error
 * handling for errors related to the Box REST API, you should capture this exception explicitly.</p>
 */
public class BoxMetadataTemplate extends BoxResource {

    private static final URLTemplate METADATA_TEMPLATE_URL_TEMPLATE = new URLTemplate("metadata_templates/%s");
    private static final URLTemplate METADATA_TEMPLATE_SCHEMA_URL_TEMPLATE =
            new URLTemplate("metadata_templates/%s/%s/schema");

    private final String template;

    /**
     * Constructs a BoxMetadataTemplate for a metadata template with a given scope and template.
     *
     * @param api the API connection to be used by the resource.
     * @param scope the scope of the metadata template.
     * @param template the key of the metadata template.
     */
    public BoxMetadataTemplate(BoxAPIConnection api, String scope, String template) {
        super(api, scope);

        this.template = template;
    }

    /**
     * Constructs a BoxMetadataTemplate for a metadata template with a given scope.
     *
     * @param api the API connection to be used by the resource.
     * @param scope the scope of the metadata template.
     */
    public BoxMetadataTemplate(BoxAPIConnection api, String scope) {
        super(api, scope);

        this.template = null;
    }

    /**
     * Gets the template key of this resource.
     * @return the template key of this resource.
     */
    public String getTemplate() {
        return this.template;
    }

    /**
     * Get all the metadata templates within the user's enterprise.
     * @param api the API connection to be used by the resource.
     * @return a list of metadata templates within the user's enterprise.
     */
    public static List<Info> getEnterpriseTemplates(BoxAPIConnection api) {
        return new BoxMetadataTemplate(api, "enterprise").getTemplates();
    }

    /**
     * Gets information about this metadata template.
     * @return info about this metadata template.
     */
    public BoxMetadataTemplate.Info getInfo() {
        URL url = METADATA_TEMPLATE_SCHEMA_URL_TEMPLATE.build(this.getAPI().getBaseURL(), this.getID(), this.getTemplate());
        BoxAPIRequest request = new BoxAPIRequest(this.getAPI(), url, "GET");
        BoxJSONResponse response = (BoxJSONResponse) request.send();
        return new Info(response.getJSON());
    }

    /**
     * Gets all the templates inside the current scope.
     * @return a list of metadata templates
     */
    public List<Info> getTemplates() {
        URL url = METADATA_TEMPLATE_URL_TEMPLATE.build(this.getAPI().getBaseURL(), this.getID());
        BoxAPIRequest request = new BoxAPIRequest(this.getAPI(), url, "GET");
        BoxJSONResponse response = (BoxJSONResponse) request.send();
        JsonObject responseJSON = JsonObject.readFrom(response.getJSON());

        List<BoxMetadataTemplate.Info> templates = new ArrayList<BoxMetadataTemplate.Info>();
        JsonArray entries = responseJSON.get("entries").asArray();
        for (JsonValue value : entries) {
            JsonObject templateJSON = value.asObject();
            BoxMetadataTemplate template = new BoxMetadataTemplate(this.getAPI(),
                    templateJSON.get("templateKey").asString());
            BoxMetadataTemplate.Info info = template.new Info(templateJSON);
            templates.add(info);
        }

        return templates;
    }

    /**
     * Contains information about a BoxMetadataTemplate.
     */
    public class Info extends BoxResource.Info {
        private String templateKey;
        private String scope;
        private String displayName;
        private List<MetadataField> fields;

        /**
         * Constructs an Info object by parsing information from a JSON string.
         * @param  json the JSON string to parse.
         */
        public Info(String json) {
            super(json);
        }

        /**
         * Constructs an Info object using an already parsed JSON object.
         * @param  jsonObject the parsed JSON object.
         */
        Info(JsonObject jsonObject) {
            super(jsonObject);
        }

        @Override
        public BoxResource getResource() {
            return BoxMetadataTemplate.this;
        }

        /**
         * Gets the key for this template.
         * @return the key for this template
         */
        public String getTemplateKey() {
            return this.templateKey;
        }

        /**
         * Gets the scope for this template.
         * @return the scope for this template
         */
        public String getScope() {
            return this.scope;
        }

        /**
         * Gets the display name for this template.
         * @return the display name for this template
         */
        public String getDisplayName() {
            return this.displayName;
        }

        /**
         * Gets the list of fields for this template.
         * @return the list of fields for this template
         */
        public List<MetadataField> getFields() {
            return this.fields;
        }

        @Override
        void parseJSONMember(JsonObject.Member member) {
            super.parseJSONMember(member);

            String memberName = member.getName();
            JsonValue value = member.getValue();
            if (memberName.equals("templateKey")) {
                this.templateKey = value.asString();
            } else if (memberName.equals("scope")) {
                this.scope = value.asString();
            } else if (memberName.equals("displayName")) {
                this.displayName = value.asString();
            } else if (memberName.equals("fields")) {
                this.fields = this.parseFieldCollection(value.asArray());
            }
        }

        private List<MetadataField> parseFieldCollection(JsonArray entries) {
            List<MetadataField> fields = new ArrayList<MetadataField>(entries.size());
            for (JsonValue value : entries) {
                JsonObject entry = value.asObject();
                MetadataField field = new MetadataField(entry);
                fields.add(field);
            }

            return fields;
        }
    }
}