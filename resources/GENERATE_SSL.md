# Generating a self-signed certificate using OpenSSL

Last Updated: 2023-06-29
OpenSSL is an open source implementation of the SSL and TLS protocols. It provides the transport layer security over the
normal communications layer, allowing it to be intertwined with many network applications and services.

## CREDITS

This document is extracted
from [IBM's Documentation](https://www.ibm.com/docs/en/api-connect/2018.x?topic=overview-generating-self-signed-certificate-using-openssl)

## Procedure

To generate a self-signed SSL certificate using the OpenSSL, complete the following steps:

Write down the Common Name (CN) for your SSL Certificate. The CN is the fully qualified name for the system that uses
the certificate. For static DNS, use the hostname or IP address set in your Gateway Cluster (for example. 192.16.183.131
or dp1.acme.com).
Run the following OpenSSL command to generate your private key and public certificate. Answer the questions and enter
the Common Name when prompted.

```
openssl req -newkey rsa:2048 -nodes -keyout key.pem -x509 -days 365 -out certificate.pem
```

Review the created certificate:

```
openssl x509 -text -noout -in certificate.pem
```

Combine your key and certificate in a PKCS#12 (P12) bundle:

```
openssl pkcs12 -inkey key.pem -in certificate.pem -export -out certificate.p12
```

Validate your P2 file.

```
openssl pkcs12 -in certificate.p12 -noout -info
```

Once the certificate file is created, it can be uploaded to a keystore.

## After

Convert the certificate.pem file to a Java KeyStore (JKS) format. Java applications commonly use JKS as a truststore.

```
openssl x509 -outform der -in certificate.pem -out certificate.der
keytool -import -alias yourAlias -file certificate.der -keystore truststore.jks
```

You should now have these files:

```
certificate.der   certificate.p12   certificate.pem   key.pem   truststore.jks
```

Place the ```certificate.pem``` and ```truststore.jks``` files in the client folder, in a subfolder named ```ssl```.

Write in the .env file the password for the truststore.jks file:

```
TRUSTSTORE_PASSWORD=yourPassword
```
