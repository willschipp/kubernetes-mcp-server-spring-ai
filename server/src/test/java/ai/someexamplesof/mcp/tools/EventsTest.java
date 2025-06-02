package ai.someexamplesof.mcp.tools;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class EventsTest {

    String kubeConfig = "apiVersion: v1\n" + //
                "clusters:\n" + //
                "- cluster:\n" + //
                "    \n" + //
                "    server: https://36aecc14-e07e-442d-bc06-6d744a41e9e4.k8s.ondigitalocean.com\n" + //
                "  name: do-nyc3-k8s-1-32-2-do-1-nyc3-1748828214595\n" + //
                "contexts:\n" + //
                "- context:\n" + //
                "    cluster: do-nyc3-k8s-1-32-2-do-1-nyc3-1748828214595\n" + //
                "    user: do-nyc3-k8s-1-32-2-do-1-nyc3-1748828214595-admin\n" + //
                "  name: do-nyc3-k8s-1-32-2-do-1-nyc3-1748828214595\n" + //
                "current-context: do-nyc3-k8s-1-32-2-do-1-nyc3-1748828214595\n" + //
                "kind: Config\n" + //
                "preferences: {}\n" + //
                "users:\n" + //
                "- name: do-nyc3-k8s-1-32-2-do-1-nyc3-1748828214595-admin\n" + //
                "  user:\n" + //
                "    \n" + //
                "";

    // @Test
    public void testGetAllPodsInErrorState() throws Exception {
        Events events = new Events();
        General general = new General();
        ReflectionTestUtils.setField(events, "general", general);
        //invoke
        assertTrue(events.getAllPodsInErrorState(kubeConfig).hasElements().block());
    }
}
