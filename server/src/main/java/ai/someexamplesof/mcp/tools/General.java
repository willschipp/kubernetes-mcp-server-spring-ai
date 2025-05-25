package ai.someexamplesof.mcp.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import ai.someexamplesof.mcp.util.KubernetesUtils;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;

@Service
public class General {
    
    @Tool(description="list all the namespaces in a cluster")
    public List<String> listAllNamespaces(@ToolParam(description="kubeconfig") String kubeconfigString) throws Exception {

        //convert the string kubeconfig to a kubeconfig object
        KubeConfig kubeConfig = KubernetesUtils.kubeConfigFromString(kubeconfigString);

        // ApiClient client = Config.defaultClient();
        ApiClient client = Config.fromConfig(kubeConfig);
        Configuration.setDefaultApiClient(client);

        // Create CoreV1Api instance
        CoreV1Api api = new CoreV1Api();

        // List all namespaces in the cluster
        V1NamespaceList namespaceList = api.listNamespace().execute();

        List<String> namespaces = new ArrayList<>();
        for (V1Namespace ns : namespaceList.getItems()) {
            namespaces.add(ns.getMetadata().getName());
        }
        return namespaces;
    }

}
