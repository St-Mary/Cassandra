package com.stmarygate.cassandra.application;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

  private static final String LANGUAGES_DIRECTORY = "translations/";
  private static final Map<String, Map<String, String>> translations = new HashMap<>();

  public static void loadLanguages() {
    File directory =
            new File(LanguageManager.class.getClassLoader().getResource(LANGUAGES_DIRECTORY).getFile());


    Gson gson = new Gson();
    for (File file : directory.listFiles()) {
      try (FileReader reader = new FileReader(file)) {
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
          JsonObject translation = entry.getValue().getAsJsonObject();
          for (Map.Entry<String, JsonElement> translationEntry : translation.entrySet()) {
            String language = translationEntry.getKey();
            String translationValue = translationEntry.getValue().getAsString();

            Map<String, String> translationsForFile = translations.computeIfAbsent(language,
                    k -> new HashMap<>());
            translationsForFile.put(entry.getKey(), translationValue);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static String getString(String key) {
    String language = GameApplication.getLanguage();
    Map<String, String> translationsForFile = translations.get(language);
    if (translationsForFile == null) {
      return key;
    }
    String translation = translationsForFile.get(key);
    return translation != null ? translation : key;
  }
}
