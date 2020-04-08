package com.chargebee.models.enums 
sealed trait TaxjarExemptionCategory 
case object WHOLESALE extends TaxjarExemptionCategory 
case object GOVERNMENT extends TaxjarExemptionCategory 
case object OTHER extends TaxjarExemptionCategory 
case object _UNKNOWN extends TaxjarExemptionCategory 
