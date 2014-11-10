package client;

import client.service.DummyService;
import client.service.DummyServiceService;

import java.util.Scanner;

/**
 *
 */
public class DummyTester {

    public static void main(String[] args) {
        DummyService s = new DummyServiceService().getDummyServicePort();
        test(s);
        testConsole(s);
    }

    private static void testConsole(DummyService dummyService) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Type in two numbers to test DummyService.sum(int, int)");
        final int a = sc.nextInt(), b = sc.nextInt(), expected = a + b;
        final int actual = dummyService.sum(a, b);
        if (actual == expected) {
            System.out.println("SIMPLE CONSOLE TEST OK");
        } else {
            System.err.println(
                    String.format("SIMPLE CONSOLE TEST FAILED: \nexpected=%s actual=  %s", expected, actual));
        }
    }

    private static void test(DummyService dummyService) {
        final int a = 30, b = 239, expected = a + b;
        final int actual = dummyService.sum(a, b);
        if (actual == expected) {
            System.out.println("SIMPLE TEST OK");
        } else {
            System.err.println(String.format("SIMPLE TEST FAILED: \nexpected=%s actual=  %s", expected, actual));
        }
    }
}
