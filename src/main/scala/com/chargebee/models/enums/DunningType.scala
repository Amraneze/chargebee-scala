package com.chargebee.models.enums 
sealed trait DunningType 
case object AUTO_COLLECT extends DunningType 
case object OFFLINE extends DunningType 
case object DIRECT_DEBIT extends DunningType 
case object _UNKNOWN extends DunningType 
