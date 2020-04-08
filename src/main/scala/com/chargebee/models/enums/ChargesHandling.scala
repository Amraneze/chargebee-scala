package com.chargebee.models.enums 
sealed trait ChargesHandling 
case object INVOICE_IMMEDIATELY extends ChargesHandling 
case object ADD_TO_UNBILLED_CHARGES extends ChargesHandling 
case object _UNKNOWN extends ChargesHandling 
