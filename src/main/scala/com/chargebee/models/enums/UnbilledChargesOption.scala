package com.chargebee.models.enums 
sealed trait UnbilledChargesOption 
case object INVOICE extends UnbilledChargesOption 
case object DELETE extends UnbilledChargesOption 
case object _UNKNOWN extends UnbilledChargesOption 
