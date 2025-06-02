package ai.someexamplesof.mcp.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import ai.someexamplesof.mcp.util.KubernetesUtils;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class General {

    private static final Log logger = LogFactory.getLog(General.class);
    
// public List<String> listAllNamespaces(@ToolParam(description="kubeconfig") String kubeconfigString) throws Exception {

    // @Tool(description="list all the namespaces in a cluster")    
    // public List<String> listAllNamespaces(@ToolParam(description="kubeconfig") String kubeconfigString) throws Exception {

    //     //convert the string kubeconfig to a kubeconfig object
    //     KubeConfig kubeConfig = KubernetesUtils.kubeConfigFromString(kubeconfigString);

    //     // ApiClient client = Config.defaultClient();
    //     ApiClient client = Config.fromConfig(kubeConfig);
    //     Configuration.setDefaultApiClient(client);

    //     // Create CoreV1Api instance
    //     CoreV1Api api = new CoreV1Api();

    //     // List all namespaces in the cluster
    //     V1NamespaceList namespaceList = api.listNamespace().execute();

    //     List<String> namespaces = new ArrayList<>();
    //     for (V1Namespace ns : namespaceList.getItems()) {
    //         namespaces.add(ns.getMetadata().getName());
    //     }
    //     return namespaces;
    // }

    @Tool(description="list all the namespaces in a cluster")    
    public Mono<List<String>> listAllNamespaces(@ToolParam(description = "kubeconfig") String kubeconfigString) {
        logger.info("getting namespaces");
        return Mono.defer(() -> { // Defer creation until subscription
            try {
                // Convert kubeconfig string to object.  Error handling is crucial here.
                KubeConfig kubeConfig = KubernetesUtils.kubeConfigFromString(kubeconfigString);
                ApiClient client = Config.fromConfig(kubeConfig);
                Configuration.setDefaultApiClient(client);

                CoreV1Api api = new CoreV1Api();

                logger.info("after conversion and before execution");

                //Use Flux to process the list asynchronously
                return Flux.fromIterable(api.listNamespace().execute().getItems())
                        .log()
                        .map(V1Namespace::getMetadata)
                        .map(V1ObjectMeta::getName)
                        .collectList()
                        .onErrorResume(throwable -> { //Handle exceptions gracefully
                            logger.error("Error listing namespaces: " + throwable.getMessage());
                            return Mono.empty(); 
                        });
            } catch (Exception e) {
                logger.error("Error creating client or executing request: " + e.getMessage());
                return Mono.empty(); 
            }
        });
    }

}
