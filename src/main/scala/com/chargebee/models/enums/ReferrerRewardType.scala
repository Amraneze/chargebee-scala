package com.chargebee.models.enums 
sealed trait ReferrerRewardType 
case object NONE extends ReferrerRewardType 
case object REFERRAL_DIRECT_REWARD extends ReferrerRewardType 
case object CUSTOM_PROMOTIONAL_CREDIT extends ReferrerRewardType 
case object CUSTOM_REVENUE_PERCENT_BASED extends ReferrerRewardType 
case object _UNKNOWN extends ReferrerRewardType 
