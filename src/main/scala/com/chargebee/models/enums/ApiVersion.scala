package com.chargebee.models.enums 
sealed trait ApiVersion 
case object V1 extends ApiVersion 
case object V2 extends ApiVersion 
case object _UNKNOWN extends ApiVersion 
