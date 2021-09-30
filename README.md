# simple-local-cache

Simple implementation for a local cache using jdk 17

## Characterists:

- a limit to cached entries;

- a `min-heap` to purge timeouts;

- a `min-heap` to purge LRU;

- a `min-heap` to purge LRU + LCU;

- a concurrent `hashmap` to keep the entries;

- this cache can work only with String keys, but any kind of value;

- all items are used as weak references, so in the memory limit GC can clean the cache to keep the software running;
