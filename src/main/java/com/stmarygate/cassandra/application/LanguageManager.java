package com.stmarygate.cassandra.application;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stmarygate.cassandra.client.Cassandra;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LanguageManager {

  private static final String LANGUAGES_DIRECTORY = "translations";
  private static final Map<String, Map<String, String>> translations = new HashMap<>();

  public static void loadLanguages() throws IOException {
    File directory =
        new File(Cassandra.class.getClassLoader().getResource(LANGUAGES_DIRECTORY).getFile());

    if (!directory.exists()) {
      CodeSource src = LanguageManager.class.getProtectionDomain().getCodeSource();
      URL jar = src.getLocation();
      ZipInputStream zip = new ZipInputStream(jar.openStream());
      while (true) {
        ZipEntry e = zip.getNextEntry();
        if (e == null) break;
        String name = e.getName();
        if (name.startsWith("translations/") && name.endsWith(".json")) {
          try (InputStream stream =
              LanguageManager.class.getClassLoader().getResourceAsStream(name)) {
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            loadTranslationsFromObject(jsonObject);
          }
        }
      }
    } else {
      Gson gson = new Gson();
      for (File file : directory.listFiles()) {
        try (FileReader reader = new FileReader(file)) {
          JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
          loadTranslationsFromObject(jsonObject);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static void loadTranslationsFromObject(JsonObject jsonObject) {
    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      JsonObject translation = entry.getValue().getAsJsonObject();
      for (Map.Entry<String, JsonElement> translationEntry : translation.entrySet()) {
        String language = translationEntry.getKey();
        String translationValue = translationEntry.getValue().getAsString();

        Map<String, String> translationsForFile =
            translations.computeIfAbsent(language, k -> new HashMap<>());
        translationsForFile.put(entry.getKey(), translationValue);
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
