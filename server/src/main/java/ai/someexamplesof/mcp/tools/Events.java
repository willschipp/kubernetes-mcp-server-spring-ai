package ai.someexamplesof.mcp.tools;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.someexamplesof.mcp.util.KubernetesUtils;

@Service
public class Events {

    @Autowired
    General general;
    
    @Tool(name="getAllPodsInErrorState",description="list all the pods in error in a cluster")
    public List<V1Pod> getAllPodsInErrorState(@ToolParam(description="kubeconfig")String kubeconfigString) throws Exception {
        List<String> namespaces = general.listAllNamespaces(kubeconfigString);
        List<V1Pod> pods = new ArrayList<>();
        for (String namespace : namespaces) {
            pods.addAll(getNamespacePodsInErrorState(namespace,kubeconfigString));            
        }
        //return
        return pods;
    }


    @Tool(name="getNamespacePodsInErrorState",description="list all the pods in error in a namespace")
    public List<V1Pod> getNamespacePodsInErrorState(@ToolParam(description="namespace to search")String namespace,@ToolParam(description="kubeconfig")String kubeconfigString) throws Exception {

        //convert the string kubeconfig to a kubeconfig object
        KubeConfig kubeConfig = KubernetesUtils.kubeConfigFromString(kubeconfigString);

        // ApiClient client = Config.defaultClient();
        ApiClient client = Config.fromConfig(kubeConfig);
        CoreV1Api api = new CoreV1Api(client);

        // List all pods in the given namespace
        V1PodList podList = api.listNamespacedPod(namespace).execute();

        List<V1Pod> errorPods = new ArrayList<>();

        for (V1Pod pod : podList.getItems()) {
            String phase = pod.getStatus().getPhase();
            // Check for Failed phase
            if ("Failed".equalsIgnoreCase(phase)) {
                errorPods.add(pod);
                continue;
            }

            // Check for container-level errors (e.g., CrashLoopBackOff, Error)
            if (pod.getStatus().getContainerStatuses() != null) {
                for (V1ContainerStatus status : pod.getStatus().getContainerStatuses()) {
                    V1ContainerState state = status.getState();
                    if (state != null && state.getWaiting() != null) {
                        String reason = state.getWaiting().getReason();
                        if ("CrashLoopBackOff".equals(reason) || "Error".equals(reason)) {
                            errorPods.add(pod);
                            break;
                        }
                    }
                }
            }
        }
        return errorPods;
    }


}
