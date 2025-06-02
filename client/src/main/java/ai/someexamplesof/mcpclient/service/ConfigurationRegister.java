package ai.someexamplesof.mcpclient.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * dumb, in-memory collection of kubeconfig setups
 * just registers by name
 */
@Service
public class ConfigurationRegister {
    
    private Map<String,String> configurations = new HashMap<>();

    /**
     * 'saves' the config
     * @param fileName
     * @param path
     * @throws Exception
     */
    public void add(String fileName,String path) throws Exception {
        configurations.put(fileName,path);
    }

    /**
     * retrieves the location it by filename
     * @param fileName
     * @return
     * @throws Exception
     */
    public String get(String fileName) throws Exception {
        return configurations.get(fileName);
    }


    /**
     * turns it into a usable string to include in the prompt
     * @param fileName
     * @return
     * @throws Exception
     */
    public String getContent(String fileName) throws Exception {
        String path = get(fileName);
        if (path == null) {
            throw new Exception("unknown filename " + fileName);
        }//end if
        Path location = Path.of(path);
        String content = Files.readString(location);
        return content;
    }

}
