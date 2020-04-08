package com.chargebee.models.enums 
sealed trait Status 
case object FUTURE extends Status 
case object IN_TRIAL extends Status 
case object ACTIVE extends Status 
case object NON_RENEWING extends Status 
case object CANCELLED extends Status 
case object _UNKNOWN extends Status 
