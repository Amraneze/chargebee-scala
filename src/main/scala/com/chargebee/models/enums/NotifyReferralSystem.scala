package com.chargebee.models.enums 
sealed trait NotifyReferralSystem 
case object NONE extends NotifyReferralSystem 
case object FIRST_PAID_CONVERSION extends NotifyReferralSystem 
case object ALL_INVOICES extends NotifyReferralSystem 
case object _UNKNOWN extends NotifyReferralSystem 
