package be.vinci.services;

import jakarta.json.*;

import java.lang.reflect.*;
import java.util.logging.Logger;

/**
 * Class analyzer. It saves a class into attribute, from a constructor, and
 * gives a lot of convenient methods to transform this into a JSON object
 * to print the UML diagram.
 */
public class ClassAnalyzer {

    private Class aClass;

    public ClassAnalyzer(Class aClass) {
        this.aClass = aClass;
    }

    /**
     * Create a JSON Object with all the info of the class.
     * @return
     */
    public JsonObject getFullInfo() {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("name", aClass.getSimpleName());
        objectBuilder.add("fields", getFields());
        objectBuilder.add("methods",getMethods());
        return objectBuilder.build();
    }

    /**
     * Get fields, and create a Json Array with all fields data.
     * Example :
     * [ {}, {} ]
     */
    public JsonArray getFields() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        // TODO Add all fields descriptions to array (use the getField() method above)
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            arrayBuilder.add(getField(field));
        }
        return arrayBuilder.build();
    }

    public JsonArray getMethods() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        // TODO Add all fields descriptions to array (use the getField() method above)
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method: methods) {
            arrayBuilder.add(getMethod(method));
        }
        return arrayBuilder.build();
    }



    public JsonObject getMethod(Method m) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("name",m.getName());
        objectBuilder.add("returnType",m.getReturnType().getName());
        TypeVariable<Method>[] parameters = m.getTypeParameters();
        JsonArrayBuilder jsonValues = Json.createArrayBuilder();
        for (TypeVariable<Method> parameter : parameters) {
            jsonValues.add(parameter.getName());
        }
        objectBuilder.add("parameters",jsonValues.build());
        objectBuilder.add("visibility",getMethodVisibility(m));
        objectBuilder.add("isStatic", isMethodStatic(m));
        objectBuilder.add("isAbstract",Modifier.isAbstract(m.getModifiers()));
        return objectBuilder.build();
    }
    /**
     * Get a field, and create a Json Object with all field data.
     * Example :
     * {
     * name: "firstname",
     * type: "String",
     * visibility : "private"  // public, private, protected, package
     * isStatic: false,
     * }
     */
    public JsonObject getField(Field f) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("name",f.getName());
        objectBuilder.add("type",f.getType().getName());
        objectBuilder.add("visibility", getFieldVisibility(f));
        objectBuilder.add("isStatic", isFieldStatic(f));
        return objectBuilder.build();
    }

    /**
     * Return whether a field is static or not
     *
     * @param f the field to check
     * @return true if the field is static, false else
     */
    private boolean isFieldStatic(Field f) {
        int modifier = f.getModifiers();
        return Modifier.isStatic(modifier);
    }

    private boolean isMethodStatic(Method m) {
        int modifier = m.getModifiers();
        return Modifier.isStatic(modifier);
    }

    /**
     * Get field visibility in a string form
     *
     * @param f the field to check
     * @return the visibility (public, private, protected, package)
     */
    private String getFieldVisibility(Field f) {
        int modifiers = f.getModifiers();
        if (Modifier.isPublic(modifiers))return "public";
        if (Modifier.isPrivate(modifiers))return "private";
        if (Modifier.isProtected(modifiers))return "protected";
        return "package";
    }

    private String getMethodVisibility(Method m) {
        int modifiers = m.getModifiers();
        if (Modifier.isPublic(modifiers))return "public";
        if (Modifier.isPrivate(modifiers))return "private";
        if (Modifier.isProtected(modifiers))return "protected";
        return "package";
    }
}
