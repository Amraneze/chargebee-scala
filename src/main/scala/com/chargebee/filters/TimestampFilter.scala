package com.chargebee.filters

import java.sql.Timestamp
import com.chargebee.internal.RequestBase

class TimestampFilter[U <: RequestBase[U]](paramName: String, req: U) extends DateFilter[Timestamp, U](paramName, req) {}