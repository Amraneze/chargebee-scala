package com.chargebee.models.enums 
sealed trait AutoCollection 
case object ON extends AutoCollection 
case object OFF extends AutoCollection 
case object _UNKNOWN extends AutoCollection 
