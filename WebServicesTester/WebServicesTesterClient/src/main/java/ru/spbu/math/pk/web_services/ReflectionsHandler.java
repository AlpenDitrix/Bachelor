package ru.spbu.math.pk.web_services;

import com.google.common.base.Predicate;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Хранит данные об отображениях элементов веб-сервисов
 */
public class ReflectionsHandler {

    private static final ReflectionsHandler INSTANCE = new ReflectionsHandler();

    public static ReflectionsHandler getInstance() { return INSTANCE;}

    private ReflectionsHandler() {}

    /**
     * Множество всех веб-клиентов
     */
    private final Set<Class<?>>              webServiceClients         = new HashSet<>();
    /**
     * Множество всех веб-сервисов
     */
    private final Set<Class<?>>              webServices               = new HashSet<>();
    /**
     * Сопоставление каждому веб-клиенту всех его простых(методы без параметров) endpoint-ов
     */
    private final Map<Class<?>, Set<Method>> webServiceClientEndpoints = new HashMap<>();
    /**
     * Сопоставление каждому веб-сервису всех его методов
     */
    private final Map<Class<?>, Set<Method>> webServicesMethods        = new HashMap<>();
    private final String                     MAIN_PACKAGE              = "";

    private static void __clearCollections() {
        ReflectionsHandler rh = getInstance();
        rh.webServiceClients.clear();
        rh.webServices.clear();
        rh.webServiceClientEndpoints.clear();
        rh.webServicesMethods.clear();
    }

    /**
     * Подгружает отображение классов из данной папки и добавляет найденные элементы веб-сервисов к имеющимся
     *
     * @param binFilesDir путь к папке, откуда загружать файлы
     */
    public static void readReflectionsFrom(String binFilesDir) {
        __clearCollections();
        ReflectionsHandler.getInstance().readReflections(binFilesDir);
    }

    public void readReflections(String binFilesDir) {
        long start = System.nanoTime();
        long whole = System.nanoTime();
        System.out.println("Init...");
        System.out.print(" Reflections... ");
        Reflections reflections = new Reflections(MAIN_PACKAGE, new URLClassLoader(new URL[]{dirToUrl(binFilesDir)}));
        start = ok(start);

        System.out.print(" Services... ");
        webServices.addAll(reflections.getTypesAnnotatedWith(WebService.class));
        start = ok(start);

        System.out.print(" Clients... ");
        webServiceClients.addAll(reflections.getTypesAnnotatedWith(WebServiceClient.class));
        start = ok(start);

        System.out.print(" Endpoints... ");
        for (Class<?> client : webServiceClients) {
            webServiceClientEndpoints.put(client, ReflectionUtils.getMethods(client, new SimpleWebEndpointPredicate()));
        }
        start = ok(start);

        System.out.print(" Methods... ");
        for (Class<?> service : webServices) {
            webServicesMethods.put(service, ReflectionUtils.getMethods(service, new WebMethodPredicate()));
        }
        ok(start);

        System.out.print("Init ");
        ok(whole);

        System.out.println();
    }

    private static long ok(long start) {
        long end = System.nanoTime();
        System.out.println(String.format("OK (%sms)", (end - start) / 10e6));
        return System.nanoTime();
    }

    private static class WebMethodPredicate implements Predicate<Method> {
        @Override
        public boolean apply(@Nullable Method m) {
            return m != null && m.getAnnotation(WebMethod.class) != null;
        }
    }

    private static class SimpleWebEndpointPredicate implements Predicate<Method> {

        @Override
        public boolean apply(@Nullable Method m) {
            return m != null && m.getParameterTypes().length == 0 && m.getAnnotation(WebEndpoint.class) != null;
        }
    }

    private static URL dirToUrl(String path) {
        try {
            return new File(path).toURI().toURL();
        } catch (MalformedURLException e) {
            // never happens since path is correct
            throw new InternalError(e.getMessage());
        }
    }

    public Set<Class<?>> getServices() {
        return webServices;
    }

    public Set<Class<?>> getClients() {
        return webServiceClients;
    }

    public Map<Class<?>, Set<Method>> getEndpoints() {
        return webServiceClientEndpoints;
    }

    public Map<Class<?>, Set<Method>> getMethods() {
        return webServicesMethods;
    }
}
