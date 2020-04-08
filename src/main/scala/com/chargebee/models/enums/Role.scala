package com.chargebee.models.enums 
sealed trait Role 
case object PRIMARY extends Role 
case object BACKUP extends Role 
case object NONE extends Role 
case object _UNKNOWN extends Role 
