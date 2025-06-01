package ai.someexamplesof.mcpclient.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationRegister {
    
    private Map<String,String> configurations = new HashMap<>();

    public void add(String fileName,String path) throws Exception {
        configurations.put(fileName,path);
    }

    public String get(String fileName) throws Exception {
        return configurations.get(fileName);
    }

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
