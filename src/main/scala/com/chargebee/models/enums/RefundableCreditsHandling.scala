package com.chargebee.models.enums 
sealed trait RefundableCreditsHandling 
case object NO_ACTION extends RefundableCreditsHandling 
case object SCHEDULE_REFUND extends RefundableCreditsHandling 
case object _UNKNOWN extends RefundableCreditsHandling 
