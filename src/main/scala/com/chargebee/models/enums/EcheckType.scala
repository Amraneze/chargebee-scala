package com.chargebee.models.enums 
sealed trait EcheckType 
case object WEB extends EcheckType 
case object PPD extends EcheckType 
case object CCD extends EcheckType 
case object _UNKNOWN extends EcheckType 
