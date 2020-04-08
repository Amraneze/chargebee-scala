package com.chargebee.models.enums 
sealed trait ChargeOn 
case object IMMEDIATELY extends ChargeOn 
case object ON_EVENT extends ChargeOn 
case object _UNKNOWN extends ChargeOn 
