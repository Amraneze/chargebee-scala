package com.chargebee.models.enums 
sealed trait CreditType 
case object LOYALTY_CREDITS extends CreditType 
case object REFERRAL_REWARDS extends CreditType 
case object GENERAL extends CreditType 
case object _UNKNOWN extends CreditType 
