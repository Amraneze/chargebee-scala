package com.chargebee.models.enums 
sealed trait PauseOption 
case object IMMEDIATELY extends PauseOption 
case object END_OF_TERM extends PauseOption 
case object SPECIFIC_DATE extends PauseOption 
case object _UNKNOWN extends PauseOption 
