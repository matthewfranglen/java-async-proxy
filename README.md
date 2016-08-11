Example Java Async Proxy
========================

This just tests out using Jax-RS to create an async proxy using Completable Futures.

Documentation
-------------

This uses the Java 8 Rx Client extension to create the completable future
directly. This is covered in the documentation
[here](https://jersey.java.net/documentation/latest/rx-client.html#rx-client.java8).

Fudges
------

Currently to avoid the chunked encoding error the Transfer-Encoding header is
dropped. This error manifests itself in curl with the following error:

```
➜ curl http://localhost:8080/foo -v
...
* Illegal or missing hexadecimal sequence in chunked-encoding
* Closing connection 0
```

Remaining Tasks
---------------

### HTTP Method and Request Body

These arn't being copied at the moment.

### Restricted Headers

When performing a proxy for the request:

```
curl http://localhost:8080/foo
```

The following message appears in the logs:

```
Attempt to send restricted header(s) while the [sun.net.http.allowRestrictedHeaders] system property not set. Header(s) will possibly be ignored.
```
