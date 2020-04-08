package com.chargebee.models.enums 
sealed trait TaxOverrideReason 
case object ID_EXEMPT extends TaxOverrideReason 
case object CUSTOMER_EXEMPT extends TaxOverrideReason 
case object EXPORT extends TaxOverrideReason 
case object _UNKNOWN extends TaxOverrideReason 
