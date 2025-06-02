package ai.someexamplesof.mcp.tools;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.someexamplesof.mcp.util.KubernetesUtils;

/**
 * Error event specific tools
 */
@Service
public class Events {

    private static final Log logger = LogFactory.getLog(Events.class);

    @Autowired
    General general;
    
    /**
     * main tool to retrieve ANY error pods in the cluster visible by the kubeconfig
     * @param kubeconfigString
     * @return
     * @throws Exception
     */
    @Tool(name="getAllErrorPods",description="list all the pods in error in a cluster")
    public Flux<V1Pod> getAllPodsInErrorState(@ToolParam(description="kubeconfig")String kubeconfigString) throws Exception {

        logger.info("Called getAllErrorPods");

        return general.listAllNamespaces(kubeconfigString)
            .flatMapMany(namespaces -> Flux.fromIterable(namespaces)
                .flatMapSequential(namespace -> getNamespacePodsInErrorState(namespace, kubeconfigString)
                    .onErrorResume(e -> {
                        logger.error("error getting pods in namespace " + namespace,e);
                        return Flux.empty();
                    })
                )
            ).subscribeOn(Schedulers.parallel());

    }

    /**
     * get error pods in a specific namespace
     * @param namespace
     * @param kubeconfigString
     * @return
     */
    @Tool(name="getAllErrorPodsByNS",description="list all the pods in error in a namespace")
    public Flux<V1Pod> getNamespacePodsInErrorState(@ToolParam(description="namespace to search")String namespace, @ToolParam(description="kubeconfig")String kubeconfigString) {

        logger.info("Called getAllErrorPodsByNS " + namespace);

        //convert the string kubeconfig to a kubeconfig object
        return Mono.fromCallable(() -> KubernetesUtils.kubeConfigFromString(kubeconfigString))
            .flatMap(kubeConfig -> Mono.fromCallable(() -> {
                ApiClient client = Config.fromConfig(kubeConfig);
                return new CoreV1Api(client);
            }))
            .flatMapMany(api -> Mono.fromCallable(() -> api.listNamespacedPod(namespace).execute())
                .subscribeOn(Schedulers.boundedElastic()))
            .flatMap(podList -> Flux.fromIterable(podList.getItems()).filter(pod -> isPodInErrorState(pod))) //This calls the separate filter method to figure out what's in error
            .onErrorResume(error -> {
                logger.error("Error fetching pods: ", error);
                return Flux.empty(); // Return an empty Flux on error to prevent propagation
            });
        
    }


    /**
     * The heavy lifting; determining if a pod is in error
     * @param pod
     * @return
     */
    private boolean isPodInErrorState(V1Pod pod) {
        String phase = pod.getStatus().getPhase(); //check the phase
        if ("Failed".equalsIgnoreCase(phase)) return true; //it's bad --> exit

        if (pod.getStatus().getContainerStatuses() != null) { //got container status
            for (V1ContainerStatus status : pod.getStatus().getContainerStatuses()) { //loop through them
                V1ContainerState state = status.getState(); //retrieve the state
                if (state != null && state.getWaiting() != null) { //check if it's WAITING
                    String reason = state.getWaiting().getReason();
                    if (reason.contains("Crash") || reason.contains("Error")) { //it's WAITING because something's wrong
                        return true; //it's bad --> exit
                    } //end if
                } //end if
            } //end for
        }//end if
        return false; //default --> not bad
    }

}
