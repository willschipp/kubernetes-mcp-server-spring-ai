package ai.someexamplesof.mcp.tools;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class GeneralToolsTest {
    
    String kubeconfig = "apiVersion: v1\n" + //
                "clusters:\n" + //
                "- cluster:\n" + //
                "    certificate-authority-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURKekNDQWcrZ0F3SUJBZ0lDQm5Vd0RRWUpLb1pJaHZjTkFRRUxCUUF3TXpFVk1CTUdBMVVFQ2hNTVJHbG4KYVhSaGJFOWpaV0Z1TVJvd0dBWURWUVFERXhGck9ITmhZWE1nUTJ4MWMzUmxjaUJEUVRBZUZ3MHlOVEExTWpReQpNalExTWpsYUZ3MDBOVEExTWpReU1qUTFNamxhTURNeEZUQVRCZ05WQkFvVERFUnBaMmwwWVd4UFkyVmhiakVhCk1CZ0dBMVVFQXhNUmF6aHpZV0Z6SUVOc2RYTjBaWElnUTBFd2dnRWlNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0SUIKRHdBd2dnRUtBb0lCQVFETTV3azJhNXZJR2J1ckNxWmU5K1I0SlJIdGRsejJ3cUpaMFpKQWxYd1k0cy8xemJyMgpSVmFIRmU3TUZuSGVXcGVKSVRjbWdLMzlqa0haSlJpM3VCTnVtNEdSZ2JscWxFMldiSTA3Wk5ZRjE5OG1qMzVECmdLd1c2MW4zN1JEWjB0RzFJclMxSE1kdXRQM29BNU1hMFdjVXNQaEJrMGt6L2NXaThOVjVHVVI3WnRkSlJQL0IKaEJ1WWJqd3ltZnYwQU1BMGdMU0dDSmpGYXVJbktpV29uSXJKTkE1UzJwVm8yMEl0eE5pVlpmSGZ3NlJSRU5nawo1OHlUbytkdzBYdEN4TjViNjhOMjcxeW53bTZLVnU2WEN0Y1B6YTk4WDcyRHVXU1ptUkFWMVNmQWlCOGZZU3hBCklxUUkxcWZYcjZmeG9MYjg4d0tyZGRXTFlCejVvSVMva1ZvMUFnTUJBQUdqUlRCRE1BNEdBMVVkRHdFQi93UUUKQXdJQmhqQVNCZ05WSFJNQkFmOEVDREFHQVFIL0FnRUFNQjBHQTFVZERnUVdCQlJUQlVvSlFqdS9rMGRBbzhpNgpjL2NFSUdtR0xqQU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FRRUFIaW02NFFwZ21zeTNtQmVGQkRGcGdTVE5pZ3dyCnpnU1VyK1RDaHpCNzdDTWw3dzJSOUdwR2s3SUZhN3Z6M1NjbjB2UWlncW94eDdKa0lpd1MzVmlvWmlIYmZGeEgKQlFsZDNaUjVJTlJmTHJDYUdzMStNVnN6c2R6Y2dFVGJ1eW1lQmVvQ3ZOM1plN2NKbVdPdG9mYXRRM25IdW9mZgp5eUQ0SXNIeTRhSHM5cUI4N0NoTVV5RlFxc0lSQVZlYzNtdE80TCtnbWJGRzVPbEU1N1BHY1J5Z2w0THI2K0U0CnIzRnpFUzdPVVRzMkcyUjVpQUVma0lYNGFsUllRQzBJMEp2SjNwam5DeUF6T1Q1VEFYNHZreFI4Tjl5NlRjaDEKTlpsUDJuTGZQSjk3akhHZlVKT1BJQ2tueU5QNnVYVFpRc1FoSXhWeDhoeVNqeStzV2hpNTZaTjljZz09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K\n" + //
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
                "    token: dop_v1_218e58e593f1a631de04acb12f1859fe075306ee76a8198a05033028bbd8fd70";

    @Test
    public void testListAllNamespaces() throws Exception {
        //get the kubeconfig as a string and invoke
        List<String> namespaces = new General().listAllNamespaces(kubeconfig);
        assertFalse(namespaces.isEmpty());
        assertTrue(namespaces.size() > 0);
        System.out.println(namespaces);
    }
}