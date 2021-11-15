# spring boot slice

##Slice

This application shows how to use **slice** to process data in chunks

**Slice** is a sized chunk of data with an indication of whether there is more data available.

Spring supports **Slice** as a return type of the query method. Pageable parameter must be specified by the same query method.

**Slice** avoids triggering a count query to calculate the overall number of pages as that might be expensive. 

A **Slice** only knows about whether a next or previous Slice is available, which might be sufficient when walking through a larger result set.

````java
    @Query("SELECT e FROM Employee e WHERE dateCreated <= :dateCreated")
    Slice<Employee> findByDateCreated(Date dateCreated, Pageable pageable);
````

##Stream

You can also use stream to query data

````java
    @QueryHints(value = {
            @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE),
            @QueryHint(name = HINT_CACHEABLE, value = "false"),
            @QueryHint(name = READ_ONLY, value = "true")
    })
    @Query("SELECT e FROM Employee e WHERE emailAddress IS NULL")
    Stream<Employee> findByNullEmailAddress();
````