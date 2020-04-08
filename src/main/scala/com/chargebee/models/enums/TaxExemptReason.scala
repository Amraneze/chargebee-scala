package com.chargebee.models.enums 
sealed trait TaxExemptReason 
case object TAX_NOT_CONFIGURED extends TaxExemptReason 
case object REGION_NON_TAXABLE extends TaxExemptReason 
case object EXPORT extends TaxExemptReason 
case object CUSTOMER_EXEMPT extends TaxExemptReason 
case object PRODUCT_EXEMPT extends TaxExemptReason 
case object ZERO_RATED extends TaxExemptReason 
case object REVERSE_CHARGE extends TaxExemptReason 
case object HIGH_VALUE_PHYSICAL_GOODS extends TaxExemptReason 
case object _UNKNOWN extends TaxExemptReason 
