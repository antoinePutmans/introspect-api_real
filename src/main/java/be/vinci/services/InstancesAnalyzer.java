package be.vinci.services;

import jakarta.json.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Instances analyzer. It saves an instance into attribute, from a constructor, and
 * gives a lot of convenient methods to transform this into a JSON object
 * to print the UML diagram.
 */
public class InstancesAnalyzer {

    private Object anInstance;

    public InstancesAnalyzer(Object anInstance) {
        this.anInstance = anInstance;
    }

    /**
     * Create a Json Object with all instance data.
     * Example :
     * {
     * classname: "User",
     * fields: [{}, {}],
     * }
     */
    public JsonObject getFullInfo() throws IllegalAccessException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("classname",anInstance.getClass().getSimpleName());
        objectBuilder.add("fields", getFields());
        return objectBuilder.build();
    }

    /**
     * Get a field, and create a Json Object with all field data.
     * Example :
     * {
     * name: "firstname",
     * type: "String",
     * value: "Laurent"
     * isStatic: false
     * }
     * If the type is an object, the value will be null
     */
    public JsonObject getField() throws IllegalAccessException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        //objectBuilder.add("name",f.get(anInstance).toString());
        Field [] instanceFields = anInstance.getClass().getDeclaredFields();
        int compteur = 1;
        for (Field f : instanceFields) {
            f.setAccessible(true);
            objectBuilder.add("name"+compteur,f.get(anInstance).toString());
            compteur++;
        }
        //objectBuilder.add("type",f.get(anInstance).toString());
        //objectBuilder.add("value",f.get(anInstance).toString());
        //objectBuilder.add("isStatic", Modifier.isStatic(f.getModifiers()));
        return objectBuilder.build();
    }

    /**
     * Get fields, and create a Json Array with all fields data.
     * Example :
     * [ {}, {} ]
     */
    public JsonArray getFields() throws IllegalAccessException {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        arrayBuilder.add(getField());

        return arrayBuilder.build();
    }

}