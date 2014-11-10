package ru.spbu.math.pk.web_services;

import com.sun.tools.ws.wscompile.WsimportTool;

import java.io.File;
import java.io.OutputStream;

/**
 * Класс, генерирующий классы веб-сервиса и подключающий их к системе
 */
public class WsdlLoader {

    /** Папка для сгенерированных файлов */
    private static final String GEN_FILES_DIR = "gen";

    /** Папка для скомпилированых классов (*.class) */
    private static final String BIN_FILES_DIR = "gen/bin";

    /** Папка для исходных кодов классов (*.java) */
    private static final String SRC_FILES_DIR = "gen/src";

    /** Аргументы для запуска {@link com.sun.tools.ws.wscompile.WsimportTool} с генерацией исходных кодов */
    private static final String[] argsGenSrc = {"-d", "gen/bin", "-s", "gen/src", "-verbose", "-extension", ""};

    /** Аргументы для запуска {@link com.sun.tools.ws.wscompile.WsimportTool} без генерации исходных кодов */
    private static final String[] args = {"-d", "gen/bin", "-extension", ""};

    /** Нужно ли гененрировать файлы с исходным кодом сгенерированных классов */
    private static boolean generateSources = false;

    /**
     * @param generateSources нужно ли гененрировать файлы с исходным кодом сгенерированных классов
     */
    public static void setGenerateSources(boolean generateSources) {
        WsdlLoader.generateSources = generateSources;
    }

    /**
     * Генерирует и компилирует классы из предоставленного описания веб-сервиса. В случае успеха -- так же подгружает
     * веб-элементы из них в {@link ReflectionsHandler}
     *
     * @param wsdlLocation локальный или удаленный путь к WSDL-файлу
     */
    public static void load(String wsdlLocation) {
        generateFolders();
        OutputStream logStream = System.out;

        try {
            new WsimportTool(logStream).run(putWsdlLocationToArgs(wsdlLocation));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return;
        }
        tryLoad();
    }

    public static void tryLoad() {ReflectionsHandler.readReflectionsFrom(BIN_FILES_DIR);}

    /**
     * Генерирует папки для файлов, создаваемых {@link com.sun.tools.ws.wscompile.WsimportTool}
     */
    private static void generateFolders() {
        final File gen = new File(GEN_FILES_DIR);
        final File bin = new File(BIN_FILES_DIR);
        final File src = new File(SRC_FILES_DIR);
        if (gen.exists()) {
            deleteFolder(gen);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean success = gen.mkdir();
        success = success && bin.mkdir() && src.mkdir();
        if (!success) {
            //SecurityException? ISE?
            throw new RuntimeException("Не могу сосздать папки для генерации файлов классов");
        }
    }

    /** Помещает полученный адрес WSDL-файла в заготовку с агрументами запуска */
    private static String[] putWsdlLocationToArgs(String wsdlLocation) {
        String[] argz = generateSources ? argsGenSrc : args;
        argz[argz.length - 1] = wsdlLocation;
        return argz;
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
