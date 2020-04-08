package com.chargebee.models.enums 
sealed trait PricingModel 
case object FLAT_FEE extends PricingModel 
case object PER_UNIT extends PricingModel 
case object TIERED extends PricingModel 
case object VOLUME extends PricingModel 
case object STAIRSTEP extends PricingModel 
case object _UNKNOWN extends PricingModel 
