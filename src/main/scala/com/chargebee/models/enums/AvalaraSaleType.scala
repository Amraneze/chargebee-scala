package com.chargebee.models.enums 
sealed trait AvalaraSaleType 
case object WHOLESALE extends AvalaraSaleType 
case object RETAIL extends AvalaraSaleType 
case object CONSUMED extends AvalaraSaleType 
case object VENDOR_USE extends AvalaraSaleType 
case object _UNKNOWN extends AvalaraSaleType 
