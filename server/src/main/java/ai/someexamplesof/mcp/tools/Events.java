package ai.someexamplesof.mcp.tools;

import io.kubernetes.client.openapi.ApiClient;
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

@Service
public class Events {

    private static final Log logger = LogFactory.getLog(Events.class);

    @Autowired
    General general;
    
    // public List<V1Pod> getAllPodsInErrorState(@ToolParam(description="kubeconfig")String kubeconfigString) throws Exception {

    @Tool(name="getAllErrorPods",description="list all the pods in error in a cluster")
    public Flux<V1Pod> getAllPodsInErrorState(@ToolParam(description="kubeconfig")String kubeconfigString) throws Exception {

        logger.info("Called getAllErrorPods");

        Flux<String> namespaces = Mono.fromCallable(() -> {
                logger.info("inside getting all namespaces");
                return general.listAllNamespaces(kubeconfigString);
            })
            .onErrorResume(e -> Mono.empty())
            .flatMapMany(Flux::fromIterable);

        return namespaces.flatMap(namespace -> getNamespacePodsInErrorState(namespace, kubeconfigString)
                .onErrorResume(e -> {
                    logger.error("error getting pods in namespace " + namespace,e);
                    return Flux.empty();
                }));

        // List<String> namespaces = general.listAllNamespaces(kubeconfigString);
        // logger.info("after the namespace has been called...");
        // List<V1Pod> pods = new ArrayList<>();
        // for (String namespace : namespaces) {
        //     pods.addAll(getNamespacePodsInErrorState(namespace,kubeconfigString));            
        // }
        // return Flux.fromIterable(general.listAllNamespaces(kubeconfigString)).flatMap(namespace -> { 
        // return Flux.fromIterable(namespaces).flatMap(namespace -> { 
        //     try {
        //         return getNamespacePodsInErrorState(namespace,kubeconfigString);
        //     } catch (Exception e) { 
        //         return null;
        //     }});

        //return
        // return Flux.fromIterable(pods);
    }

    // public List<V1Pod> getNamespacePodsInErrorState(@ToolParam(description="namespace to search")String namespace,@ToolParam(description="kubeconfig")String kubeconfigString) throws Exception {

    @Tool(name="getAllErrorPodsByNS",description="list all the pods in error in a namespace")
    public Flux<V1Pod> getNamespacePodsInErrorState(@ToolParam(description="namespace to search")String namespace,@ToolParam(description="kubeconfig")String kubeconfigString) {

        logger.info("Called getAllErrorPodsByNS " + namespace);

        //convert the string kubeconfig to a kubeconfig object
        KubeConfig kubeConfig = KubernetesUtils.kubeConfigFromString(kubeconfigString);

        // ApiClient client = Config.defaultClient();
        ApiClient client = null;
        // CoreV1Api api = null;
        // V1PodList podList = null;
        try {
            client = Config.fromConfig(kubeConfig);
            CoreV1Api api = new CoreV1Api(client);
            // podList = api.listNamespacedPod(namespace).execute();

            return Mono.fromCallable(() -> {
                    logger.info("inside calling api for namespace");
                    return api.listNamespacedPod(namespace).execute();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(podList -> Flux.fromIterable(podList.getItems())
                    .filter(pod -> {
                        String phase = pod.getStatus().getPhase();
                        if ("Failed".equalsIgnoreCase(phase)) return true; //filter

                        if (pod.getStatus().getContainerStatuses() != null) {
                            for (V1ContainerStatus status : pod.getStatus().getContainerStatuses()) {
                                V1ContainerState state = status.getState();
                                if (state != null && state.getWaiting() != null) {
                                    String reason = state.getWaiting().getReason();
                                    if ("CrashLoopBackOff".equals(reason) || "Error".equals(reason)) {
                                        return true; //filter
                                    }
                                }
                            }
                        } //end if

                        return false;//default
                    })
                );
        }
        catch (Exception e) {
            throw new IllegalArgumentException();
        }
        // ApiClient client = Config.fromConfig(kubeConfig);
        // CoreV1Api api = new CoreV1Api(client);

        // List all pods in the given namespace
        // V1PodList podList = api.listNamespacedPod(namespace).execute();

        // List<V1Pod> errorPods = new ArrayList<>();

        // for (V1Pod pod : podList.getItems()) {
        //     String phase = pod.getStatus().getPhase();
        //     // Check for Failed phase
        //     if ("Failed".equalsIgnoreCase(phase)) {
        //         errorPods.add(pod);
        //         continue;
        //     }

        //     // Check for container-level errors (e.g., CrashLoopBackOff, Error)
        //     if (pod.getStatus().getContainerStatuses() != null) {
        //         for (V1ContainerStatus status : pod.getStatus().getContainerStatuses()) {
        //             V1ContainerState state = status.getState();
        //             if (state != null && state.getWaiting() != null) {
        //                 String reason = state.getWaiting().getReason();
        //                 if ("CrashLoopBackOff".equals(reason) || "Error".equals(reason)) {
        //                     errorPods.add(pod);
        //                     break;
        //                 }
        //             }
        //         }
        //     }
        // }
        // return Flux.fromIterable(errorPods);
    }


}
