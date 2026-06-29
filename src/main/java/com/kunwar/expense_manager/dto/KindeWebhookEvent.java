package com.kunwar.expense_manager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KindeWebhookEvent {
    private String type;
    private KindeEventData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KindeEventData {
        private KindeUser user;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KindeUser {
        private String id;
        private String email;
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;
    }
}