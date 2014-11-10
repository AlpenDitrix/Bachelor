package ru.spbu.math.pk.web_services;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceClient;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Вызов через отражение
 */
public class ReflectionRequest {

    public static void runMethod(WebMethodName wnm) {
        Class<?> client = findWebServiceClient(wnm.getServiceName().getLocalPart());
        Class<?> service = findWebService(wnm.getServiceName().getLocalPart());
        Set<Method> endpoints = ReflectionsHandler.getInstance().getEndpoints().get(client);
        boolean success = false;

        for (Method ep : endpoints) {
            try {
                Object port = ep.invoke(client.newInstance());
                Method webMethod = findWebMethod(wnm.getMethodName(), service);
//                Object[] args = ParamsProvider.getParams(webMethod);
//                webMethod.invoke(port, 10, null, null, null, null);
//                Main.printResponse((Holder<String>) args[2], (Holder<SecurityHeaderType>) args[3],
//                                   (Holder<List<MediaSourceType>>) args[4]);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                continue;
            }
            success = true;
        }
        if (!success) { throw new RuntimeException("Нет доступа к веб-сервису"); }
    }

    public static void doRequest() {
        mediaSourcesProviderDummyRequest();
    }

    private static void mediaSourcesProviderDummyRequest() {
        final String serviceName = "MediaSourcesProvider";
        try {
            invoke(serviceName);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Этот метод пытается для заданного веб-сервиса найти его клиет, получить все endpoint-ы и вызвать все их методы.
     * В данный момент вызов метода выполняется всегда одинаково, но вообще todo набор параметров должен меняться
     * в зависимости от сервиса и метода (двойной switch по @WebService:name и @WebMethod:name, например)
     *
     * @param serviceName название сервиса
     *
     * @throws IllegalAccessException    метод получение endpoint-а или вызов веб-метода оказался private
     * @throws InstantiationException    оказалось невозможным инстанцииорвать клиент
     * @throws InvocationTargetException неправильный объект для вызова метода
     */
    private static void invoke(final String serviceName)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> mspClient = findWebServiceClient(serviceName);
        Class<?> mspService = findWebService(serviceName);

        ReflectionsHandler rhandler = ReflectionsHandler.getInstance();
        Set<Method> mspClientEndpoints = rhandler.getEndpoints().get(mspClient);
        Set<Method> mspServiceMethods = rhandler.getMethods().get(mspService);

        for (Method e : mspClientEndpoints) {
            Object port = e.invoke(mspClient.newInstance());
            for (Method m : mspServiceMethods) {
                Holder<String> nextStartReference = new Holder<>();
//                Holder<SecurityHeaderType> security = new Holder<>();
//                Holder<List<MediaSourceType>> mediaSource = new Holder<>();
//                m.invoke(port, 10, null, nextStartReference, security, mediaSource);
//                //не выпадет исключение только для MediaSourcesProvider::GetMediaSources
//                Main.printResponse(nextStartReference, security, mediaSource);
            }
        }
    }


    /**
     * @param forName искомое имя сервиса
     *
     * @return класс первого попавшегося веб-сервиса с искомым названием
     */
    private static Class<?> findWebService(String forName) {
        for (Class<?> s : ReflectionsHandler.getInstance().getServices()) {
            if (s.getAnnotation(WebService.class).name().equals(forName)) {
                return s;
            }
        }
        return null;
    }

    /**
     * @param forName искомое имя сервиса
     *
     * @return класс первого попавшегося веб-клиента с искомым именем
     */
    private static Class<?> findWebServiceClient(String forName) {
        final String clientName = forName.concat("Service"); // так wsimport обзывает клиентские классы,
        // когда не задано WebService(serviceName)
        for (Class<?> s : ReflectionsHandler.getInstance().getClients()) {
            if (s.getAnnotation(WebServiceClient.class).name().equals(clientName)) {
                return s;
            }
        }
        return null;
    }

    /**
     * @param forName искомое имя метода
     *
     * @return первый попавшийся веб-метод с искомым именем
     */
    private static Method findWebMethod(String forName, Class<?> service) {
        for (Method m : ReflectionsHandler.getInstance().getMethods().get(service)) {
            if (m.getAnnotation(WebMethod.class).operationName().equals(forName)) {
                return m;
            }
        }
        return null;
    }

}
