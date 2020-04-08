package com.chargebee.models.enums 
sealed trait UnpaidInvoicesHandling 
case object NO_ACTION extends UnpaidInvoicesHandling 
case object SCHEDULE_PAYMENT_COLLECTION extends UnpaidInvoicesHandling 
case object _UNKNOWN extends UnpaidInvoicesHandling 
