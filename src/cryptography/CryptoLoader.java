package cryptography;

import configuration.Configuration;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class CryptoLoader {
    private Object port;
    private Method cryptoMethod;

    public void createCryptographyMethod(CryptoMethod method) {
        Object instance;
        try {
            URL[] urls = {new File(Configuration.instance.getCryptoAlgorithmPath()).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, CryptoLoader.class.getClassLoader());
            Class clazz = Class.forName(Configuration.instance.cryptoAlgorithm.toString(), true, urlClassLoader);

            instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);
            cryptoMethod = port.getClass().getMethod(method.toString().toLowerCase(), String.class, File.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getPort() {
        return port;
    }

    public Method getCryptoMethod() {
        return cryptoMethod;
    }
}
