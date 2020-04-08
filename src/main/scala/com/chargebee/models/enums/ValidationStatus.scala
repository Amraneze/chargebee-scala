package com.chargebee.models.enums 
sealed trait ValidationStatus 
case object NOT_VALIDATED extends ValidationStatus 
case object VALID extends ValidationStatus 
case object PARTIALLY_VALID extends ValidationStatus 
case object INVALID extends ValidationStatus 
case object _UNKNOWN extends ValidationStatus 
