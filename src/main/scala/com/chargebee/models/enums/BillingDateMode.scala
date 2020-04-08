package com.chargebee.models.enums 
sealed trait BillingDateMode 
case object USING_DEFAULTS extends BillingDateMode 
case object MANUALLY_SET extends BillingDateMode 
case object _UNKNOWN extends BillingDateMode 
