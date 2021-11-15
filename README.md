# spring boot slice

This application shows how to use **slice** to process data in chunks

**Slice** is a sized chunk of data with an indication of whether there is more data available.

Spring supports **Slice** as a return type of the query method. Pageable parameter must be specified by the same query method.

**Slice** avoids triggering a count query to calculate the overall number of pages as that might be expensive. \

A **Slice** only knows about whether a next or previous Slice is available, which might be sufficient when walking through a larger result set.
