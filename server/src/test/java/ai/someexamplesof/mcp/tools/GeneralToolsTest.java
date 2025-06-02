package ai.someexamplesof.mcp.tools;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class GeneralToolsTest {
    
    String kubeConfig = "apiVersion: v1\n" + //
                "clusters:\n" + //
                "- cluster:\n" + //
                "    certificate-authority-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURKekNDQWcrZ0F3SUJBZ0lDQm5Vd0RRWUpLb1pJaHZjTkFRRUxCUUF3TXpFVk1CTUdBMVVFQ2hNTVJHbG4KYVhSaGJFOWpaV0Z1TVJvd0dBWURWUVFERXhGck9ITmhZWE1nUTJ4MWMzUmxjaUJEUVRBZUZ3MHlOVEEyTURJdwpNVE0zTVRsYUZ3MDBOVEEyTURJd01UTTNNVGxhTURNeEZUQVRCZ05WQkFvVERFUnBaMmwwWVd4UFkyVmhiakVhCk1CZ0dBMVVFQXhNUmF6aHpZV0Z6SUVOc2RYTjBaWElnUTBFd2dnRWlNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0SUIKRHdBd2dnRUtBb0lCQVFESFoxNEg1SmdSL0Z5SXVWN1lDY21yMDJUM0g1TDhpODgzdmZtQUF5UVFVSGx1RTNIWQoySFNRZGlmWExZMGxGTW14RXo1SlBLT1ZzamxHQXZhZGtITjQzQVQ0REZzbDdkdWtHQWp1NWU0T2JtbW14UjYwClBodkRFSytvVTlIQXdUa2lnd05QRGJYQklaOTZUSEZLYndGc3RTaUt6S2tPaEFTTVh3cGxTekFudW9hRGJud1kKemppSGsrWGFaczJvUGpXUFRjM09TM1I1M3o0bXAwdDR0MVdHNTdHYjVlQk5ZZTluTTZGZ3VkVUIvbkk3ZkF5Tgo0dmFiaUN6a1NlUHVOTzJvTC9RQkt0ZFIwbDE1SWQ4dlFxWXNGdlA5SkF6a1NrTmZOa0kyQmtETUtXOWNxa0ZaCkRVVFhwVVBBSTJ0czJZTnNZeTIzUlA3RHgvWFVLdEFrdDZJdEFnTUJBQUdqUlRCRE1BNEdBMVVkRHdFQi93UUUKQXdJQmhqQVNCZ05WSFJNQkFmOEVDREFHQVFIL0FnRUFNQjBHQTFVZERnUVdCQlFRMCt0UkNKVzZrTUIrV1BTYgpMalY4aytFZytUQU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FRRUFtSVRWNXhhdWNBMEVVeFJ1TXhFM3lzb2pmRGowCm5iWHQ5OVBrUml5cFgzYmI4NU5MdCtpMGRvQ01ad3VuTGhJUGNvbE9BWGJaOFFEbVd0M3Y2Tnc3dEw3OFl3a2MKVTlmMGlBMU1WVlF6UURDRXdsZG5iQVYzVlY4RXJ6UzVicFpKRVNZMlNUR2FQSks5Z3ZDSHlhQ1k1OVFEek55MgpFWTJva2R5Sk01ZkJsNm1QMlBuSXluS0pCa0ZENG83eVFJS3RFZzJMbXQyU0xBamZpeGpyaTJ4dytDNWZEbytpCkxRYitCZzY0bVQvdnhHcDhJTXJvQXBMZEQ5bUxhTzJoNUxhM2R3L3ppbExOVUdwVU5hNkJIVW5TUXRYcXFWU1MKWjhJK3lzMlVUbGQyVEVzdllSa3E0cDlDNWhteTFGWjVSc1hqeU9uUHpzSy93cThxNVZZMUhHVVZyZz09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K\n" + //
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
                "    token: dop_v1_5bf0b2026c5c8c459e085b8d10d25aeca5fa239524b3926420bf3ae3ce5a3944\n" + //
                "";

    @Test
    public void testListAllNamespaces() throws Exception {
        //get the kubeconfig as a string and invoke
        List<String> namespaces = new General().listAllNamespaces(kubeConfig).block();
        assertFalse(namespaces.isEmpty());
        assertTrue(namespaces.size() > 0);
        System.out.println(namespaces);
    }
}