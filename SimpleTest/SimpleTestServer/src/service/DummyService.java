package service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService
public class DummyService {

    public static final String URL = "http://localhost:8080/dummy_service";

    @WebMethod
    public int sum(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        System.out.print("Starting server at " + URL + " ... ");
        Endpoint.publish(URL, new DummyService());
        System.out.println("OK!");
    }
}
