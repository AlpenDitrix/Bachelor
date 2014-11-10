package ru.spbu.math.pk.web_services;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@SuppressWarnings("unused")
public class AnnotatedPrinter {

    public static void printServices() {
        for (Class<?> s : ReflectionsHandler.getInstance().getServices()) {
            printService(s);
        }
    }

    private static void printService(Class<?> s) {
        WebService a = s.getAnnotation(WebService.class);
        System.out.println(
                String.format("(%s, %s, %s, %s, %s, %s)", a.name(), a.targetNamespace(), a.serviceName(), a.portName(),
                              a.wsdlLocation(), a.endpointInterface()));
    }

    public static void printClients() {
        for (Class<?> s : ReflectionsHandler.getInstance().getClients()) {
            printClient(s);
        }
    }

    private static void printClient(Class<?> s) {
        WebServiceClient a = s.getAnnotation(WebServiceClient.class);
        System.out.println(String.format("(%s, %s, %s)", a.name(), a.targetNamespace(), a.wsdlLocation()));
    }

    public static void printEndpoints() {
        Map<Class<?>, Set<Method>> endpoints = ReflectionsHandler.getInstance().getEndpoints();
        Set<Class<?>> clients = endpoints.keySet();
        for (Class<?> c : clients) {
            System.out.println("\n========");
            printClient(c);
            for (Method m : endpoints.get(c)) {
                printEndpoint(m);
            }
        }
    }

    private static void printEndpoint(Method m) {
        WebEndpoint a = m.getAnnotation(WebEndpoint.class);
        System.out.println("" + a.name());
    }

    public static void printMethods() {
        Map<Class<?>, Set<Method>> methods = ReflectionsHandler.getInstance().getMethods();
        Set<Class<?>> services = methods.keySet();
        for (Class<?> s : services) {
            System.out.println("\n========");
            printService(s);
            int i = methods.get(s).size();
            for (Method m : methods.get(s)) {
                printMethod(m, --i != 0);
            }
        }
    }

    private static final String f1 = "├┐(%s, %s, %s)";
    private static final String f2 = "│├ ReqWr: (%s, %s, %s, %s)";
    private static final String f3 = "│└ ResWr: (%s, %s, %s, %s)";

    private static final String f1l = "└┐(%s, %s, %s)";
    private static final String f2l = " ├ ReqWr: (%s, %s, %s, %s)";
    private static final String f3l = " └ ResWr: (%s, %s, %s, %s)";

    private static void printMethod(Method m, boolean notLast) {
        WebMethod a = m.getAnnotation(WebMethod.class);
        RequestWrapper aa = m.getAnnotation(RequestWrapper.class);
        ResponseWrapper aaa = m.getAnnotation(ResponseWrapper.class);
        String s1 = notLast ? f1 : f1l;
        String s2 = notLast ? f2 : f2l;
        String s3 = notLast ? f3 : f3l;
        System.out.println(String.format(s1, a.operationName(), a.action(), a.exclude()));
        if (aa != null) {
            System.out.println(String.format(s2, aa.localName(), aa.targetNamespace(), aa.className(), aa.partName()));
        } else {
            System.out.println(notLast ? "│├ ReqWr: NULL" : " ├ ReqWr: NULL");
        }
        if (aa != null) {
            System.out.println(
                    String.format(s3, aaa.localName(), aaa.targetNamespace(), aaa.className(), aaa.partName()));
        } else {
            System.out.println(notLast ? "└└ ResWr: NULL" : " └ ResWr: NULL");
        }
    }
}
