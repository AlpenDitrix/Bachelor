package ru.spbu.math.pk.web_services;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/** fixme тут какой-то говнокод */
public abstract class AvailabilityChecker {

    private Map<MethodIdentifier, Boolean> requiredOperations;
    private Map<Object, Boolean>           requiredSmthYet;

    public AvailabilityChecker(Object input) {
        //todo инициализация данных требований наличия элементов. Хранить в файлах? Текстом? В БД?
        setRequirements(input);
    }

    private static void _errorMethodNotFound(MethodIdentifier id) {
        System.out.println(String.format("[ERROR]: Не найден метод: %s", id));
    }

    private static void _warningExceedMethod(MethodIdentifier id) {
        System.out.println(String.format("[INFO]: Найден лишний метод: %s", id));
    }

    private static MethodIdentifier identifyMethod(Method method) {
        return null;//todo идентификация методов. Оставить класс WNM? Или по SoapAction?
    }

    protected abstract void setRequirements(Object input);

    private void reset() {
        Boolean b = Boolean.FALSE;
        for (MethodIdentifier key : requiredOperations.keySet()) {
            requiredOperations.put(key, b);
        }
        for (Object key : requiredSmthYet.keySet()) {
            requiredSmthYet.put(key, b);
        }
    }

    public void check(ReflectionsHandler reflections) {
        reset();
        checkOperations(reflections.getMethods());
        checkSmthYet(null);
    }

    private void checkOperations(Map<Class<?>, Set<Method>> methods) {
        for (Class<?> k : methods.keySet()) {
            for (Method m : methods.get(k)) {
                MethodIdentifier id = identifyMethod(m);
                if (requiredOperations.containsKey(id)) {
                    requiredOperations.put(id, Boolean.TRUE);
                } else {
                    _warningExceedMethod(id);
                }
            }
        }
        for (MethodIdentifier id : requiredOperations.keySet()) {
            if (!requiredOperations.get(id)) {
                _errorMethodNotFound(id);
            }
        }
    }

    private void checkSmthYet(Set<Object> smthYet) {
        //todo не приходит в голову наличие каких элементов кроме, собственно, дёргаемых методов может потребоваться
    }

}
