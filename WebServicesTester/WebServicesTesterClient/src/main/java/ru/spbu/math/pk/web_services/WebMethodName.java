package ru.spbu.math.pk.web_services;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import java.lang.reflect.Method;

/**
 * Класс, описывающий глобальное имя веб-метода, учитывающее имя и namespace сервиса, к которому он относится
 */
public class WebMethodName {
    public QName getServiceName() {
        return serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    private QName  serviceName;
    private String methodName;

    public WebMethodName(Method method) {
        WebMethod a = method.getAnnotation(WebMethod.class);
        if (a == null) {
            throw new IllegalArgumentException("That method is not a @WebMethod");
        }
        this.methodName = a.operationName();
        Class<?> service = method.getDeclaringClass();
        WebService aa = service.getAnnotation(WebService.class);
        if (aa == null) {
            throw new IllegalArgumentException("That method`s class is not a @WebService");
        }
        this.serviceName = new QName(aa.targetNamespace(), aa.name());
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof WebMethodName)) {
            return false;
        }
        WebMethodName won = (WebMethodName) o;
        return won.serviceName.equals(this.serviceName) && won.methodName.equals(this.methodName);
    }
}
