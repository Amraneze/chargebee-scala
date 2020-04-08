package com.chargebee.models.enums 
sealed trait AccountHolderType 
case object INDIVIDUAL extends AccountHolderType 
case object COMPANY extends AccountHolderType 
case object _UNKNOWN extends AccountHolderType 
