package ru.spbu.math.pk.web_services;

/**
 * <ol><li>Узнать путь к wsdl-файлу (он должен иметь хотя бы один элемент definition/service/port, чтобы он
 * распарсился.</li>
 * <li>Либо сохранить этот путь в поле, либо ввести руками</li>
 * <li>WSDL парсится, создаются *.class, а при желании и их исодники *.java. Кладутся в папку проекта\исполняемого
 * файла в gen/bin и gen/src, соответственно.</li>
 * <li>Производится попытка загрузки Reflections из созданных классов.</li>
 * <li>Найденные веб-методы выводятся в соответствии с их описанием в wsdl, помещенном в аннотации на шаге (3).</li>
 * ===========
 * <li>Проходимся циклом либо по списку тестов(а), либо по спуску методов(б)</li>
 * <li>Выполняем тесты.</li>
 * <li>Для каждого метода получаем определенный тест и выполняем его.</li>
 * </li></ol>
 * <p>
 * В первом случае надо надеятся, что заданные тесты покрывают всё.
 * <ul><li>Никак не контролируется соответствия методов и тестов.</li>
 * <li>Черный ящик, но полная свобода для тестов.</li>
 * <li>Каждый тест, должен знать о внутренностях системы, о тестируемых методах, используемых в них объектах и
 * т.п.</li>
 * </ul>
 * <p>
 * Во втором случае каждому покрытому методу сопоставлен тест.
 * <ul><li>Имеется четкое соответствие метод<->тест.</li>
 * <li>Меньше шанс потерять какой-то метод непокрытым, большая привязанность друг к другу тестов и методов.
 * </li><li>Встает вопрос о методах, которые должны применяться вместе подряд <br>(в крайнем случае, для группы методов
 * можно иметь один тест, выполняемый только один раз, а для остальных -- что-то типа "if (runned) return result;". И в
 * случаем падения теста будет известно, что один из методов группы работает не верно. Таким образом для простоты
 * тестирования нужно разбивать всё на как можно более мелкие группы (возможно с некоторым пересечением по покрытию:
 * Тест1 для a(),b(),c(), d(); Тест2 для b(), c(),d(),e(),f()).</li></ul>
 * <p>
 * В любом случае каждый тест проходит следующим образом:
 * <ol><li>Подготовка объектов (например, объектов, создаваемых классом org.onvif.ver10.ObjectFactory,
 * который описывает объекты из схемы Onvif.xsd)</li>
 * <li>Выполнение (метода)/(списка методов) с сохранением возвращаемых значений (возможно использование в цепочке
 * вызовов)</li>
 * <li>Проверка (возвращенных значений)/(того, что на сервере что-то изменилось)</li></ol>
 */
public class Main {

    private static String wsdlLocation = "onvif/ver10/device/wsdl/devicemgmt.wsdl";

    public static void main(String[] args) {
//        if(wsdlLocation == null) {
//            System.out.print("Path to wsdl-file location: ");
//            wsdlLocation = new Scanner(System.in).next();
//    }
        WsdlLoader.setGenerateSources(true);
        WsdlLoader.load(wsdlLocation);
        AnnotatedPrinter.printMethods();
    }

}