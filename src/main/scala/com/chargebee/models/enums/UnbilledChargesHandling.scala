package com.chargebee.models.enums 
sealed trait UnbilledChargesHandling 
case object NO_ACTION extends UnbilledChargesHandling 
case object INVOICE extends UnbilledChargesHandling 
case object _UNKNOWN extends UnbilledChargesHandling 
