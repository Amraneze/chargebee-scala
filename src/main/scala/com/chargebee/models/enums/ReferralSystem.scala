package com.chargebee.models.enums 
sealed trait ReferralSystem 
case object REFERRAL_CANDY extends ReferralSystem 
case object REFERRAL_SAASQUATCH extends ReferralSystem 
case object FRIENDBUY extends ReferralSystem 
case object _UNKNOWN extends ReferralSystem 
