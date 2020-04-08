package com.chargebee.models.enums 
sealed trait DispositionType 
case object ATTACHMENT extends DispositionType 
case object INLINE extends DispositionType 
case object _UNKNOWN extends DispositionType 
