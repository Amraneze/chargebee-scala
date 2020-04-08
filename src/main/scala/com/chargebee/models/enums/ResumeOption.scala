package com.chargebee.models.enums 
sealed trait ResumeOption 
case object IMMEDIATELY extends ResumeOption 
case object SPECIFIC_DATE extends ResumeOption 
case object _UNKNOWN extends ResumeOption 
