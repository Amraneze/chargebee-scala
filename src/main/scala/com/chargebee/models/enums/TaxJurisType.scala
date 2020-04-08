package com.chargebee.models.enums 
sealed trait TaxJurisType 
case object COUNTRY extends TaxJurisType 
case object FEDERAL extends TaxJurisType 
case object STATE extends TaxJurisType 
case object COUNTY extends TaxJurisType 
case object CITY extends TaxJurisType 
case object SPECIAL extends TaxJurisType 
case object UNINCORPORATED extends TaxJurisType 
case object OTHER extends TaxJurisType 
case object _UNKNOWN extends TaxJurisType 
