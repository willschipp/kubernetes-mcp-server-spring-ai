### Setting to run google vertex as a test


`GOOGLE_APPLICATION_CREDENTIALS=(path to service_account_key.json)`


### Running locally

`curl -XPOST localhost:8080/api/kubeconfig -H "Content-type: multipart/form-data" -F "file=@kubectl_do"`
`curl localhost:8080/api/status/kubectl_do`
`curl localhost:8080/api/tools | jq`