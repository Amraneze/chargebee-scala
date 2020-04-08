package com.chargebee.models.enums 
sealed trait InvoiceDunningHandling 
case object CONTINUE extends InvoiceDunningHandling 
case object STOP extends InvoiceDunningHandling 
case object _UNKNOWN extends InvoiceDunningHandling 
