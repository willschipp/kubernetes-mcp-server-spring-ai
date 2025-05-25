package ai.someexamplesof.mcp.tools;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class GeneralToolsTest {
    
    String kubeconfig = "apiVersion: v1\n" + //
                "clusters:\n" + //
                "- cluster:\n" + //
                "    certificate-authority-data: \n" + //
                "    server: https://8bd1a495-fe99-4e89-9b99-482c0597de5c.k8s.ondigitalocean.com\n" + //
                "  name: do-sfo3-k8s-1-32-2-do-1-sfo3-1748126710053\n" + //
                "contexts:\n" + //
                "- context:\n" + //
                "    cluster: do-sfo3-k8s-1-32-2-do-1-sfo3-1748126710053\n" + //
                "    user: do-sfo3-k8s-1-32-2-do-1-sfo3-1748126710053-admin\n" + //
                "  name: do-sfo3-k8s-1-32-2-do-1-sfo3-1748126710053\n" + //
                "current-context: do-sfo3-k8s-1-32-2-do-1-sfo3-1748126710053\n" + //
                "kind: Config\n" + //
                "preferences: {}\n" + //
                "users:\n" + //
                "- name: do-sfo3-k8s-1-32-2-do-1-sfo3-1748126710053-admin\n" + //
                "  user:\n" + //
                "    token: ";

    @Test
    public void testListAllNamespaces() throws Exception {
        //get the kubeconfig as a string and invoke
        List<String> namespaces = new General().listAllNamespaces(kubeconfig);
        assertFalse(namespaces.isEmpty());
        assertTrue(namespaces.size() > 0);
        System.out.println(namespaces);
    }
}