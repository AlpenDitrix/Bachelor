package ru.spbu.math.pk.web_services;

/**
 * Ручная отправка запроса
 */
@Deprecated
public class SimpleRequest {

    public void testDummyService() {
        System.out.print("TEST ");
        final int a = 30;
        final int b = 239;
        final int correctResult = a + b;
        final int result = doRequest(a, b);

        if (result == correctResult) {
            System.out.println("OK");
        }
        System.err.println(String.format("FAIL: expected=%s, actual=%s", correctResult, result));
    }

    private int doRequest(int a, int b) {
        return 0;
    }

}
