package com.box.sdk;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchParams {
    private Collection<JsonObject> metadataFilters;

    public String getMetadataFilters() {
        JsonArray filterArray = new JsonArray();

        for (JsonValue metadataFilter : metadataFilters) {
            filterArray.add(metadataFilter);
        }

        return filterArray.toString();
    }

    public boolean hasMetadataFilters() {
        return metadataFilters != null && !metadataFilters.isEmpty();
    }

    public AdvancedSearchParams addMetadataFilter(MetadataFilter metadataFilter) {
        if (metadataFilters == null) {
            metadataFilters = new ArrayList<JsonObject>();
        }

        metadataFilters.add(metadataFilter.build());

        return this;
    }

    public static class MetadataFilter {
        private String templateKey;
        private String scope;
        private Map<String, String> filters;

        public String getTemplateKey() {
            return templateKey;
        }

        public MetadataFilter setTemplateKey(String templateKey) {
            this.templateKey = templateKey;
            return this;
        }

        public String getScope() {
            return scope;
        }

        public MetadataFilter setScope(String scope) {
            this.scope = scope;
            return this;
        }

        public Map<String, String> getFilters() {
            return filters;
        }

        public MetadataFilter addFilter(String key, String value) {
            if (filters == null) {
                filters = new HashMap<String, String>();
            }

            filters.put(key, value);
            return this;
        }

        public MetadataFilter setFilters(Map<String, String> filters) {
            this.filters = filters;
            return this;
        }

        public JsonObject build() {
            JsonObject filter = new JsonObject();

            if (templateKey != null) {
                filter.add("templateKey", templateKey);
            }

            if (scope != null) {
                filter.add("scope", scope);
            }

            JsonObject filterFilters = new JsonObject();
            if (filters != null) {
                for (Map.Entry<String, String> entry : filters.entrySet()) {
                    filterFilters.add(entry.getKey(), entry.getValue());
                }
            }
            filter.add("filters", filterFilters);

            return filter;
        }
    }
}
