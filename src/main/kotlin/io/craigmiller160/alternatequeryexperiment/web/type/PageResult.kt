package io.craigmiller160.alternatequeryexperiment.web.type

data class PageResult<T>(val contents: List<T>, val totalRecords: Long)
