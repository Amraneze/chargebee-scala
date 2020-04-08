package com.chargebee.models.enums 
sealed trait ReportBy 
case object CUSTOMER extends ReportBy 
case object INVOICE extends ReportBy 
case object PRODUCT extends ReportBy 
case object SUBSCRIPTION extends ReportBy 
case object _UNKNOWN extends ReportBy 
