package com.chargebee.models.enums 
sealed trait AccountReceivablesHandling 
case object NO_ACTION extends AccountReceivablesHandling 
case object SCHEDULE_PAYMENT_COLLECTION extends AccountReceivablesHandling 
case object WRITE_OFF extends AccountReceivablesHandling 
case object _UNKNOWN extends AccountReceivablesHandling 
