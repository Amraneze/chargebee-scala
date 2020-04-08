package com.chargebee.models.enums 
sealed trait BillingDayOfWeekMode 
case object USING_DEFAULTS extends BillingDayOfWeekMode 
case object MANUALLY_SET extends BillingDayOfWeekMode 
case object _UNKNOWN extends BillingDayOfWeekMode 
