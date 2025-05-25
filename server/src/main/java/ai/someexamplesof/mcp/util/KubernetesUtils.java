package ai.someexamplesof.mcp.util;

import java.io.StringReader;

import io.kubernetes.client.util.KubeConfig;

public class KubernetesUtils {
    
    public static KubeConfig kubeConfigFromString(String kubeconfigYaml) {
        return KubeConfig.loadKubeConfig(new StringReader(kubeconfigYaml));
    }
}
