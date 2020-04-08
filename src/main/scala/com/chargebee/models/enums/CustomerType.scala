package com.chargebee.models.enums 
sealed trait CustomerType 
case object RESIDENTIAL extends CustomerType 
case object BUSINESS extends CustomerType 
case object SENIOR_CITIZEN extends CustomerType 
case object INDUSTRIAL extends CustomerType 
case object _UNKNOWN extends CustomerType 
