package com.springstudy.backend.Api.ChatGPT.Prompt;

import java.util.List;
import java.util.StringJoiner;

public class PromptTemplate {

    public static String generateSystemPrompt(List<String> categories) {
        String categoryText;

        if (categories == null || categories.isEmpty()) {
            categoryText = "아무 분야";
        } else if (categories.size() == 1) {
            categoryText = categories.get(0);
        } else {
            StringJoiner joiner = new StringJoiner(", ");
            for (int i = 0; i < categories.size() - 1; i++) {
                joiner.add(categories.get(i));
            }
            categoryText = joiner + "와 " + categories.get(categories.size() - 1);
        }

        return String.format("""
                너는 %s의 향상을 돕는 랜덤챌린지 추천 AI야.
                """, categoryText);
    }

    public static String generateUserPrompt(List<String> categories, int age, String sex, int successRate) {
        String categoryText;
        if (categories == null || categories.isEmpty()) {
            categoryText = "없음";
        } else {
            categoryText = String.join(", ", categories);
        }
        return String.format(
                "나이: %d, 성별: %s, 선호 챌린지 유형: %s, 챌린지 성공률: %d%%",
                age, sex, categoryText, successRate
        );
    }
}
