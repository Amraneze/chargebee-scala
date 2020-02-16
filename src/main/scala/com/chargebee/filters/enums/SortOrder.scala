package com.chargebee.filters.enums

sealed trait SortOrder
case object ASC extends SortOrder
case object DESC extends SortOrder
case object _UNKNOWN extends SortOrder