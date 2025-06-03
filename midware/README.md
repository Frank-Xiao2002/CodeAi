# CodeAi - Midware

## Tips

Create the certs directory in the resources folder.

Use the following command to create *.pem certificate files.

```bash
openssl genrsa -out private.pem 2048
openssl rsa -in private.pem -pubout -out public.pem
```