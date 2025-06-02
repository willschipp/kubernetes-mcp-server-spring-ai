package ai.someexamplesof.mcp.tools;

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
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * General, non-error focused tools for Kubernetes
 */
@Service
public class General {

    private static final Log logger = LogFactory.getLog(General.class);
    
    /**
     * simple method to get all the namespaces visible with the kubeconfig passed
     */
    @Tool(description="list all the namespaces in a cluster") //MCP-specific tool description   
    public Mono<List<String>> listAllNamespaces(@ToolParam(description = "kubeconfig") String kubeconfigString) {
        logger.info("Getting namespaces");
        return Mono.defer(() -> { // Defer creation until subscription
            try {
                // Convert kubeconfig string to object.
                KubeConfig kubeConfig = KubernetesUtils.kubeConfigFromString(kubeconfigString);
                ApiClient client = Config.fromConfig(kubeConfig);
                Configuration.setDefaultApiClient(client);

                CoreV1Api api = new CoreV1Api();

                //process the link asynchronously
                return Flux.fromIterable(api.listNamespace().execute().getItems())
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
