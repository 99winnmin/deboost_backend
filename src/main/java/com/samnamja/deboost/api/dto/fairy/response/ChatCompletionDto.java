package com.samnamja.deboost.api.dto.fairy.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatCompletionDto {
    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private List<Choice> choices;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Choice {
        private Message message;
        private String finish_reason;
        private int index;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Message {
            private String role;
            private String content;
        }
    }
}