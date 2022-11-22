package be.vinci.api;

import be.vinci.classes.User;
import be.vinci.instances.InstanceGraph1;
import be.vinci.services.ClassAnalyzer;
import be.vinci.services.InstancesAnalyzer;
import be.vinci.utils.InstanceGraphBuilder;
import jakarta.json.JsonStructure;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Send instances graph data to make object diagrams
 *
 * The instances graphs are initialized by a class containing the "initInstanceGraph" method,
 * building the instance graph, and returning it.
 *
 * The "instance builder class name" must be given and present into the "instances" package
 */
@Path("instances")
public class Instances {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonStructure getInstanceGraphInfo(@QueryParam("builderclassname") String builderClassname) throws IllegalAccessException {
        // TODO change this line to use the query parameter, and generate dynamically the builder
        InstancesAnalyzer analyzer;
        try{
            Class maClasse = Class.forName("be.vinci.instances."+builderClassname);
            Object object = maClasse.getDeclaredConstructor().newInstance();
            Method method = null ;
            for (Method declaredMethod : maClasse.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(InstanceGraphBuilder.class)){
                    method=declaredMethod;
                    break;
                }
            }
            Object objetInvoque =  method.invoke(object);
            analyzer = new InstancesAnalyzer(objetInvoque);
        }catch (ClassNotFoundException e) {
            throw new WebApplicationException(404);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return analyzer.getFullInfo();
    }
}
