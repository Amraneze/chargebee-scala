package com.chargebee.models.enums 
sealed trait CreditOptionForCurrentTermCharges 
case object NONE extends CreditOptionForCurrentTermCharges 
case object PRORATE extends CreditOptionForCurrentTermCharges 
case object FULL extends CreditOptionForCurrentTermCharges 
case object _UNKNOWN extends CreditOptionForCurrentTermCharges 
