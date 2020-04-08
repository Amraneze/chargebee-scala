package com.chargebee.models.enums 
sealed trait PriceType 
case object TAX_EXCLUSIVE extends PriceType 
case object TAX_INCLUSIVE extends PriceType 
case object _UNKNOWN extends PriceType 
