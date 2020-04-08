package com.chargebee.models.enums 
sealed trait BillingAlignmentMode 
case object IMMEDIATE extends BillingAlignmentMode 
case object DELAYED extends BillingAlignmentMode 
case object _UNKNOWN extends BillingAlignmentMode 
