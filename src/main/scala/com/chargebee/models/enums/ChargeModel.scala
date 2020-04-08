package com.chargebee.models.enums 
sealed trait ChargeModel 
case object FULL_CHARGE extends ChargeModel 
case object PRORATE extends ChargeModel 
case object _UNKNOWN extends ChargeModel 
