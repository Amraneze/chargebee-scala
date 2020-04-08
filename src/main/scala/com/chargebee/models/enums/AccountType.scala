package com.chargebee.models.enums 
sealed trait AccountType 
case object CHECKING extends AccountType 
case object SAVINGS extends AccountType 
case object BUSINESS_CHECKING extends AccountType 
case object _UNKNOWN extends AccountType 
