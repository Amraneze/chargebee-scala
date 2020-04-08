package com.chargebee.filters

import com.chargebee.internal.RequestBase

class BooleanFilter[U <: RequestBase[U]](paramName: String, req: U) extends Filter[BooleanFilter[U], U] {}