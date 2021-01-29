package me.spaceman.psilocin.handlers;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import me.spaceman.psilocin.Psilocin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigHandler {

    private HashMap<String, String> loadedValues = new HashMap<>();
    private File configFolder, configFile;

    public ConfigHandler(String configFolderName) throws IOException {
        configFolder = new File(configFolderName + "/");
        if(!configFolder.exists())
            configFolder.createNewFile();

        configFile = new File(configFolder.getPath() + "config.json");
        if(!configFile.exists())
            configFile.createNewFile();

        if(!readConfig())
        {
            Psilocin.getInstance().log("Unable to read config file.", Psilocin.Level.WARN);
        }
    }

    public ConfigHandler() throws IOException {
        configFolder = new File("config/");
        if(!configFolder.exists() || !configFolder.isDirectory())
            Files.createDirectory(Paths.get(configFolder.getAbsolutePath()));

        configFile = new File(configFolder.getAbsolutePath() + "/config.json");
        if(!configFile.exists())
            configFile.createNewFile();

        if(!readConfig())
        {
            Psilocin.getInstance().log("Unable to read config file.", Psilocin.Level.WARN);
        }
    }

    private boolean readConfig()
    {
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonArray = parser.parse(new FileReader(this.configFile));
            if(jsonArray.isJsonNull() || !jsonArray.isJsonArray())
                return false;
            for(JsonElement element : (JsonArray)jsonArray)
            {
                if(element.isJsonObject())
                {
                    JsonObject obj = (JsonObject) element;
                    this.loadedValues.put(obj.get("valueName").getAsString(), obj.get("value").getAsString());
                } else {
                    return false;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T> T getValue(String valueName)
    {
        if(this.loadedValues.containsKey(valueName))
            return (T)this.loadedValues.get(valueName);
        else
            return null;
    }

    public void saveArray(String[] array, String fileName) throws IOException {
        File savedFile = new File(this.configFolder + "/" + fileName);
        if(!savedFile.exists())
            savedFile.createNewFile();

        try {
            FileWriter writer = new FileWriter(savedFile);
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.setIndent("    ");
            jsonWriter.beginArray();
            for(String string : array)
            {
                jsonWriter.value(string);
            }
            jsonWriter.endArray();
            jsonWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] readArray(String fileName) throws FileNotFoundException
    {
        File savedFile = new File(this.configFolder + "/" + fileName);
        if(!savedFile.exists())
            return new String[] {};

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonObject = jsonParser.parse(new FileReader(savedFile));
        if(jsonObject.isJsonArray())
        {
            JsonArray jsonArray = (JsonArray) jsonObject;
            String[] returnArray = new String[jsonArray.size()];
            for(int i = 0; i < jsonArray.size(); i++)
            {
                JsonPrimitive arrayElement = (JsonPrimitive) jsonArray.get(i);
                returnArray[i] = arrayElement.getAsString();
                Psilocin.getInstance().log(arrayElement.getAsString(), Psilocin.Level.INFO);
            }
            return returnArray;
        } else {
            return new String[] {};
        }
    }

    public void putValue(String valueName, Object value)
    {
        this.loadedValues.put(valueName, value.toString());
        saveConfig();
    }

    private void saveConfig()
    {
        try {

            FileWriter fileWriter = new FileWriter(this.configFile);
            JsonWriter jsonWriter = new JsonWriter(fileWriter);
            jsonWriter.setIndent("    ");
            jsonWriter.beginArray();
            for(Map.Entry<String, String> entry : this.loadedValues.entrySet())
            {
                jsonWriter.beginObject();
                jsonWriter.name("valueName").value(entry.getKey());
                jsonWriter.name("value").value(entry.getValue());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
