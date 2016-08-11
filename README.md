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
